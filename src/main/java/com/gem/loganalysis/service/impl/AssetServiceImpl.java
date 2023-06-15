package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.config.BusinessConfigInfo;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.enmu.AssetClass;
import com.gem.loganalysis.enmu.DictType;
import com.gem.loganalysis.exception.ServiceException;
import com.gem.loganalysis.mapper.AssetMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.*;
import com.gem.loganalysis.model.vo.AssetEventHomeOverviewVO;
import com.gem.loganalysis.model.vo.HomeOverviewVO;
import com.gem.loganalysis.model.vo.ImportRespVO;
import com.gem.loganalysis.model.vo.RiskAssetRankingVO;
import com.gem.loganalysis.model.vo.asset.*;
import com.gem.loganalysis.service.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.gem.loganalysis.util.UserUtil.getAuthorityUserId;
import static com.gem.loganalysis.util.UserUtil.getLoginUserOrgId;

/**
 * 安全管理资产 Service 实现类
 *
 * @author czw
 */
@Service
@Validated
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IAssetService {

    @Resource
    private AssetMapper assetMapper;
    @Resource
    private DictItemService dictItemService;
    @Resource
    private IM4SsoOrgService orgService;
    @Resource
    private IAssetGroupService groupService;
    @Resource
    private IPhysicalAssetTempService physicalAssetTempService;
    @Resource
    private IAssetTypeService assetTypeService;
    @Resource
    private IM4SsoUserService userService;
    @Resource
    private BusinessConfigInfo businessConfigInfo;


    @Override
    public Result<String> editAsset(AssetDTO dto) {
        if (dto.getIpAddress() != null && !dto.getIpAddress().trim().equals("")) {
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            if (!Pattern.matches(regex, dto.getIpAddress())) {
                //效验IP地址格式是否正确
                return Result.failed("请输入正确的IP地址格式");
            }
        }
        //如果资产状态没有传就默认填0
        if(StringUtils.isBlank(dto.getAssetStatus())){
            dto.setAssetStatus("0");
        }
/*        //前端已加密 后端无需AES加密
        if(!StringUtils.isBlank(dto.getNmPassword())){
            String password = AESUtil.aesEncrypt(dto.getNmPassword(), businessConfigInfo.getAESKey());
            dto.setNmPassword(password);
        }*/
        return this.saveOrUpdate(AssetConvert.INSTANCE.convert(dto)) ? Result.ok("操作成功!") : Result.failed("操作失败!");
    }

    @Override
    public PageResponse<AssetRespVO> getPageList(PageRequest<AssetQueryDTO> dto) {
        com.github.pagehelper.Page<AssetRespVO> result = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        assetMapper.getAssetList(dto.getData());
        //转义
        result.forEach(e->{
            //TODO 资产的安全状态目前先全部放安全，等待之后风险部分完成再放入
            e.setAssetSecurityStatus("2");
            changeAssetName(e);
            //TODO 资产评分
            Random rand = new Random();
            int randomNum = rand.nextInt((100 - 95) + 1) + 95;
            e.setScore(randomNum);
        });
        return new PageResponse<>(result);
    }


    @Override
    public List<AssetRespVO> getAssetList(AssetQueryDTO dto) {
        List<AssetRespVO> assetRespVOList = assetMapper.getAssetList(dto);
        //转义
        assetRespVOList.forEach(e->{
            //TODO 资产的安全状态目前先全部放安全，等待之后风险部分完成再放入
            e.setAssetSecurityStatus("2");
            changeAssetName(e);
            //TODO 资产评分
            Random rand = new Random();
            int randomNum = rand.nextInt((100 - 95) + 1) + 95;
            e.setScore(randomNum);
        });
        return assetRespVOList;
    }

    @Override
    public ImportRespVO importLogicalExcel(List<LogicalAssetExcelVO> list) {
        String regex = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
        ImportRespVO respVO = ImportRespVO.builder().successNames(new ArrayList<>())
                .failNames(new LinkedHashMap<>()).build();
        list.forEach(e->{
            try {
                if(StringUtils.isBlank(e.getAssetName())){
                    throw new ServiceException("导入的资产名称不能为空");
                }
                if(e.getAssetName().equals("逻辑资产示例")){
                    return;
                }
                if(StringUtils.isBlank(e.getAssetTypeName())){
                    throw new ServiceException("导入的资产类型不能为空");
                }
                if(StringUtils.isBlank(e.getIpAddress())){
                    throw new ServiceException("导入的IP地址为空");
                }
                if(!Pattern.matches(regex,e.getIpAddress())){
                    //效验IP地址格式是否正确
                    throw new ServiceException("请输入正确的IP格式");
                }
                if(!StringUtils.isBlank(e.getServicePort())&&!e.getServicePort().matches("\\d+")){
                    throw new ServiceException("请确保服务端口号为纯数字");
                }
                Asset asset = AssetConvert.INSTANCE.convert(e);
                asset.setAssetClass(AssetClass.LOGICAL.getId());
                //判断资产名称是否存在
                Asset one = this.getOne(new LambdaQueryWrapper<Asset>()
                        .eq(Asset::getAssetName, e.getAssetName()).last("LIMIT 1"));
                if(one!=null){
                    throw new ServiceException("该资产:"+e.getAssetName()+"已存在");
                }
                //资产类型导入
                if(!StringUtils.isBlank(e.getAssetTypeName())){
                    String value = dictItemService.getValueByText(DictType.LOGICAL_ASSET_TYPE.getType(), e.getAssetTypeName());
                    if(value==null){
                        throw new ServiceException("请导入数据字典中存在的逻辑资产的类型");
                    }
                    asset.setAssetType(value);
                }
                //资产部门导入
                if(!StringUtils.isBlank(e.getAssetOrgName())){
                    M4SsoOrg org = orgService.getOne(new LambdaQueryWrapper<M4SsoOrg>()
                            .eq(M4SsoOrg::getOrgName, e.getAssetOrgName()).last("LIMIT 1"));
                    if(org==null){
                        //优先放导入的部门，没有就放登录人的部门，如果导入的部门不存在，且登录人的部门也不存在，就报异常
                        if(!StringUtils.isBlank(getLoginUserOrgId())){
                            asset.setAssetOrg(getLoginUserOrgId());
                        }else{
                            throw new ServiceException("您导入的资产部门系统中不存在，请先添加部门");
                        }
                    }else{
                        asset.setAssetOrg(org.getOrgId());
                    }
                }
                //资产分组导入
                if(!StringUtils.isBlank(e.getAssetGroupName())){
                    AssetGroup group = groupService.getOne(new LambdaQueryWrapper<AssetGroup>()
                            .eq(AssetGroup::getGroupName, e.getAssetGroupName()).last("LIMIT 1"));
                    if(group==null){
                        throw new ServiceException("您导入的资产分组系统中不存在，请先添加分组");
                    }
                    asset.setAssetGroupId(group.getGroupId());
                }
                //负责人导入
                if(!StringUtils.isBlank(e.getAssetManagerName())){
                    M4SsoUser user = userService.getOne(new LambdaQueryWrapper<M4SsoUser>().eq(M4SsoUser::getAccount, e.getAssetManagerName()).last("LIMIT 1"));
                    if(user==null){
                        throw new ServiceException("您导入的资产管理人系统中不存在，请先添加用户");
                    }
                    asset.setAssetManager(user.getUserId());
                }
                assetMapper.insert(asset);
                respVO.getSuccessNames().add(e.getAssetName());
            }catch (Exception ex){
                respVO.getFailNames().put(e.getAssetName()!=null?e.getAssetName():"空资产名", ex.getMessage());
            }
        });
        return respVO;
    }

    @Override
    public ImportRespVO importPhysicalExcel(List<PhysicalAssetExcelVO> list) {
        String regex = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
        ImportRespVO respVO = ImportRespVO.builder().successNames(new ArrayList<>())
                .failNames(new LinkedHashMap<>()).build();
        list.forEach(e->{
            try {
                if(StringUtils.isBlank(e.getAssetName())){
                    throw new ServiceException("导入的资产名称不能为空");
                }
                if(e.getAssetName().equals("物理资产示例")){
                    return;
                }
                if(StringUtils.isBlank(e.getAssetCategory())){
                    throw new ServiceException("导入的资产类别不能为空");
                }
                if(StringUtils.isBlank(e.getTypeName())){
                    throw new ServiceException("导入的资产类型不能为空");
                }
                if(StringUtils.isBlank(e.getIpAddress())){
                    throw new ServiceException("导入的IP地址为空");
                }
                if(!Pattern.matches(regex,e.getIpAddress())){
                    //效验IP地址格式是否正确
                    throw new ServiceException("请输入正确的IP格式");
                }
                if(!StringUtils.isBlank(e.getServicePort())&&!e.getServicePort().matches("\\d+")){
                    throw new ServiceException("请确保服务端口号为纯数字");
                }
                //判断资产名称是否存在
                Asset one = this.getOne(new LambdaQueryWrapper<Asset>()
                        .eq(Asset::getAssetName, e.getAssetName()).last("LIMIT 1"));
                if(one!=null){
                    throw new ServiceException("该资产:"+e.getAssetName()+"已存在");
                }
                Asset asset = AssetConvert.INSTANCE.convert(e);
                asset.setAssetClass(AssetClass.PHYSICAL.getId());
                //资产类型导入
                if(!StringUtils.isBlank(e.getTypeName())){
                    String assetTypeId = assetTypeService.getAssetTypeId(e.getTypeName());
                    if(assetTypeId==null){
                        throw new ServiceException("您导入的资产类型不存在,请先去配置管理中添加");
                    }
                    asset.setAssetType(assetTypeId);
                }
                //资产部门导入
                if(!StringUtils.isBlank(e.getAssetOrgName())){
                    M4SsoOrg org = orgService.getOne(new LambdaQueryWrapper<M4SsoOrg>()
                            .eq(M4SsoOrg::getOrgName, e.getAssetOrgName()).last("LIMIT 1"));
                    if(org==null){
                        //优先放导入的部门，没有就放登录人的部门，如果导入的部门不存在，且登录人的部门也不存在，就报异常
                        if(!StringUtils.isBlank(getLoginUserOrgId())){
                            asset.setAssetOrg(getLoginUserOrgId());
                        }else{
                            throw new ServiceException("您导入的资产部门系统中不存在，请先添加部门");
                        }
                    }else{
                        asset.setAssetOrg(org.getOrgId());
                    }
                }
                //资产分组导入
                if(!StringUtils.isBlank(e.getAssetGroupName())){
                    AssetGroup group = groupService.getOne(new LambdaQueryWrapper<AssetGroup>()
                            .eq(AssetGroup::getGroupName, e.getAssetGroupName()).last("LIMIT 1"));
                    if(group==null){
                        throw new ServiceException("您导入的资产分组系统中不存在，请先添加分组");
                    }
                    asset.setAssetGroupId(group.getGroupId());
                }
                //负责人导入
                if(!StringUtils.isBlank(e.getAssetManagerName())){
                    M4SsoUser user = userService.getOne(new LambdaQueryWrapper<M4SsoUser>().eq(M4SsoUser::getAccount, e.getAssetManagerName()).last("LIMIT 1"));
                    if(user==null){
                        throw new ServiceException("您导入的资产管理人系统中不存在，请先添加用户");
                    }
                    asset.setAssetManager(user.getUserId());
                }
                assetMapper.insert(asset);
                respVO.getSuccessNames().add(e.getAssetName());
            }catch (Exception ex){
                respVO.getFailNames().put(e.getAssetName()!=null?e.getAssetName():"空资产名", ex.getMessage());
            }
        });
        return respVO;
    }

    @Override
    public HomeOverviewVO getHomeOverview() {

        HomeOverviewVO homeOverview = assetMapper.getAssetHomeOverview();
        List<AssetEventHomeOverviewVO> event = assetMapper.getEventHomeOverview();
        event.forEach(e->{
            if(e.getAssetType().equals("安全设备")){
                homeOverview.setSecurityDeviceEventsResolvedCount(e.getTotalEventCount()) // 当日安全设备事件总次数
                        .setSecurityDeviceEventsPendingCount(e.getPendingEventCount()); // 当日安全设备事件未处理次数
            }else if(e.getAssetType().equals("网络设备")){
                homeOverview.setNetworkDeviceEventsResolvedCount(e.getTotalEventCount()) // 当日网络设备事件已处理次数
                        .setNetworkDeviceEventsPendingCount(e.getPendingEventCount()); // 当日网络设备事件未处理次数
            }else if(e.getAssetType().equals("服务器")){
                homeOverview.setItDeviceEventsResolvedCount(e.getTotalEventCount()) // 当日IT设备事件总次数
                        .setItDeviceEventsPendingCount(e.getPendingEventCount()); // 当日IT设备事件未处理次数
            }
        });
        // 示例数据
        List<RiskAssetRankingVO> nonEndpointRiskAssetRanking = new ArrayList<>();
        List<RiskAssetRankingVO> endpointRiskAssetRanking = new ArrayList<>();

        RiskAssetRankingVO nonEndpointRiskAsset1 = new RiskAssetRankingVO("192.168.0.11", 99);
        RiskAssetRankingVO nonEndpointRiskAsset2 = new RiskAssetRankingVO("192.168.0.32", 95);
        RiskAssetRankingVO nonEndpointRiskAsset3 = new RiskAssetRankingVO("192.168.0.3", 92);
        RiskAssetRankingVO nonEndpointRiskAsset4 = new RiskAssetRankingVO("192.168.0.24", 87);
        RiskAssetRankingVO nonEndpointRiskAsset5 = new RiskAssetRankingVO("192.168.0.15", 85);

        RiskAssetRankingVO endpointRiskAsset1 = new RiskAssetRankingVO("192.168.1.41", 97);
        RiskAssetRankingVO endpointRiskAsset2 = new RiskAssetRankingVO("192.168.1.22", 97);
        RiskAssetRankingVO endpointRiskAsset3 = new RiskAssetRankingVO("192.168.1.36", 96);
        RiskAssetRankingVO endpointRiskAsset4 = new RiskAssetRankingVO("192.168.1.12", 88);
        RiskAssetRankingVO endpointRiskAsset5 = new RiskAssetRankingVO("192.168.1.5", 86);

        nonEndpointRiskAssetRanking.add(nonEndpointRiskAsset1);
        nonEndpointRiskAssetRanking.add(nonEndpointRiskAsset2);
        nonEndpointRiskAssetRanking.add(nonEndpointRiskAsset3);
        nonEndpointRiskAssetRanking.add(nonEndpointRiskAsset4);
        nonEndpointRiskAssetRanking.add(nonEndpointRiskAsset5);

        endpointRiskAssetRanking.add(endpointRiskAsset1);
        endpointRiskAssetRanking.add(endpointRiskAsset2);
        endpointRiskAssetRanking.add(endpointRiskAsset3);
        endpointRiskAssetRanking.add(endpointRiskAsset4);
        endpointRiskAssetRanking.add(endpointRiskAsset5);

        //TODO 明天把能取到的取到
        homeOverview
                .setAssetTotalScore(69.72) // 资产总评分
                .setLowVulnerabilityCount(4)//低危漏洞
                .setMediumVulnerabilityCount(16)//中危漏洞
                .setHighVulnerabilityCount(1)//高危漏洞
                .setSecurityVulnerabilityCount(21) // 安全漏洞数量
                .setComplianceVulnerabilityCount(0) // 合规漏洞数量
                .setCloudAssetRiskCount(0) // 云资产风险数量
                .setBaselineRiskCount(0) // 基线风险数量
                .setTotalRiskCount(21) // 风险总数
                .setDateList(weekDateList()) // 近七天日期集合
                .setLowRiskCount(Arrays.asList(5, 6, 3, 8, 4, 4, 4)) // 近七天低风险集合
                .setMediumRiskCount(Arrays.asList(15, 14, 13, 14, 14, 15, 16)) // 近七天中风险集合
                .setHighRiskCount(Arrays.asList(1, 0, 2, 1, 3, 0, 1)) // 近七天高风险集合
                .setExportDeviceLoad(Arrays.asList(590, 640, 850, 730, 800, 840, 900)) // 近七天出口设备负荷(流量)
                .setSecurityDeviceAssetScore(Arrays.asList(85, 90, 92, 88, 95 ,95, 89))//近七天安全设备资产评分集合
                .setNetworkDeviceAssetScore(Arrays.asList(82, 85, 89, 86, 88 ,90 , 88))//近七天网络设备资产评分集合
                .setItDeviceAssetScore(Arrays.asList(80, 85, 90, 88, 86 ,86, 91))//近七天IT设备资产评分集合
                .setLogicalAssetScore(Arrays.asList(90, 88, 92, 85, 91, 92, 89))//近七天逻辑资产评分集合
                .setNonEndpointRiskAssetRanking(nonEndpointRiskAssetRanking)//非终端风险资产排行
                .setEndpointRiskAssetRanking(endpointRiskAssetRanking);//终端风险资产排行
        return homeOverview;
    }

    private List<String> weekDateList(){
        List<String> dateList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        for (int i = 6; i >= 0 ; i--) {
            String date = currentDate.minusDays(i).format(formatter);
            dateList.add(date);
        }
        return dateList;
    }

    @Override
    public AssetRespVO getAsset(String id) {
        return  changeAssetName(assetMapper.getAssetById(id));
    }

    @Override
    public AssetAccountRespVO getAssetAccount(String id) {
        Asset asset = this.getById(id);
        AssetAccountRespVO account = new AssetAccountRespVO();
        account.setNmAccount(asset.getNmAccount());
        account.setNmPassword(asset.getNmPassword());
        return account;
    }

    @Override
    public AssetOverviewVO getOverviewInfo() {
        AssetOverviewVO assetOverviewVO = new AssetOverviewVO();
        List<AssetRespVO> assetList = getAssetList(new AssetQueryDTO());
        //内存中进行计算
        //物理资产总数
        assetOverviewVO.setPhysicalAssetNum((int)assetList.stream()
                .filter(e->e.getAssetClass().equals("1")).count());
        //逻辑资产总数
        assetOverviewVO.setLogicalAssetNum((int)assetList.stream()
                .filter(e->e.getAssetClass().equals("0")).count());
        //逻辑资产类型分布
        Map<String, List<AssetRespVO>> logicalAsset = assetList.stream()
                .filter(e -> e.getAssetClass().equals("0")&&StringUtils.isNotBlank(e.getAssetTypeName()))
                .collect(Collectors.groupingBy(AssetRespVO::getAssetTypeName));
        assetOverviewVO.setLogicalAssetDistribution(logicalAsset);
        //物理资产类型分布
        Map<String, List<AssetRespVO>> physicalType = assetList.stream()
                .filter(e -> e.getAssetClass().equals("1")&&StringUtils.isNotBlank(e.getTypeName()))
                .collect(Collectors.groupingBy(AssetRespVO::getTypeName));
        assetOverviewVO.setPhysicalAssetTypeDistribution(physicalType);
        //资产状态分布
        Map<String, List<AssetRespVO>> assetStatus = assetList.stream()
                .collect(Collectors.groupingBy(AssetRespVO::getAssetStatusName));
        //assetOverviewVO.setAssetStatusDistribution(assetStatus);
        assetOverviewVO.setAssetOnlineStatusDistribution(assetStatus);
        //资产类别分布
        Map<String, List<AssetRespVO>> assetCategory = assetList.stream()
                .filter(e -> e.getAssetClass().equals("1")&&StringUtils.isNotBlank(e.getAssetCategory()))
                .collect(Collectors.groupingBy(AssetRespVO::getAssetCategory));
        assetOverviewVO.setAssetCategoryDistribution(assetCategory);
/*        //最近新增资产(10条)
        //这个不要了 统计的没意义 先注释掉
        List<AssetRespVO> sorted = assetList.stream()
                .sorted(Comparator.comparing(AssetRespVO::getCreateTime,Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
        assetOverviewVO.setNewAssetList(AssetConvert.INSTANCE.convertList11(sorted));*/
        //最近资产发现(5条)
        assetOverviewVO.setNewAssetScanList(AssetConvert.INSTANCE.convertList06(physicalAssetTempService.getNewAssetScanList()));
        //主机开放端口Top5
        //TODO 改成了高危端口TOP5，不知道咋取值
        Map<String, List<AssetRespVO>> ip = assetList.stream()
                .filter(e -> e.getAssetClass().equals("0")&&StringUtils.isNotEmpty(e.getIpAddress()))
                .collect(Collectors.groupingBy(AssetRespVO::getIpAddress));
        assetOverviewVO.setIpTop5(ip);
        //主机端口Top5
        //TODO 改成了封堵设备TOP5 可以取但是还没取
        Map<Integer, List<AssetRespVO>> port = assetList.stream()
                .filter(e -> e.getAssetClass().equals("0")&&e.getServicePort()!=null)
                .collect(Collectors.groupingBy(AssetRespVO::getServicePort));
        assetOverviewVO.setIpPortTop5(port);
        //资产趋势
        List<AssetOverviewVO.AssetTrendsList> assetTrendsListList = new ArrayList<>();
        List<String> recentlyDateList = getRecentlyDateList(6);
        assetOverviewVO.setDateList(recentlyDateList);
        //物理资产趋势
        Map<String, Long> physicalMap = getAssetTrendsMap(assetList, "1");
        AssetOverviewVO.AssetTrendsList physicalList = new AssetOverviewVO.AssetTrendsList();
        physicalList.setAssetClass("物理资产");
        List<Integer> physicalDataList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : physicalMap.entrySet()) {
            physicalDataList.add(entry.getValue().intValue());
        }
        physicalList.setList(physicalDataList);
        assetTrendsListList.add(physicalList);
        //逻辑资产趋势
        Map<String, Long> logicalMap = getAssetTrendsMap(assetList, "0");
        AssetOverviewVO.AssetTrendsList logicalList = new AssetOverviewVO.AssetTrendsList();
        logicalList.setAssetClass("逻辑资产");
        List<Integer> logicalDataList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : logicalMap.entrySet()) {
            logicalDataList.add(entry.getValue().intValue());
        }
        logicalList.setList(logicalDataList);
        assetTrendsListList.add(logicalList);

        assetOverviewVO.setAssetTrendsList(assetTrendsListList);
        return assetOverviewVO;
    }

    /**
     *
     * @param assetList
     * @param assetType 0逻辑资产 1物理资产
     * @return
     */
    private Map<String, Long> getAssetTrendsMap(List<AssetRespVO> assetList,String assetType){
        List<String>  lastSixMonths = getRecentlyDateList(6);
        DateTimeFormatter fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, Long> countMap = assetList.stream()
                .filter(e->e.getAssetClass().equals(assetType)&&StringUtils.isNotEmpty(e.getCreateTime()))
                .map(asset->LocalDate.parse(asset.getCreateTime(),fullFormat).format(monthFormat))
                .filter(lastSixMonths::contains)// 只计算最近六个月的数据
                .collect(Collectors.groupingBy(date -> date, Collectors.counting()));
        for (String month : lastSixMonths) {
            countMap.putIfAbsent(month, 0L);
        }
        // 创建一个 LinkedHashMap，并将 countMap 中的键值对按照日期的顺序添加到 LinkedHashMap 中
        Map<String, Long> sortedCountMap = new LinkedHashMap<>();
        lastSixMonths.forEach(month -> sortedCountMap.put(month, countMap.get(month)));
        return sortedCountMap;
    }


    /**
     * 获取最近X个月日期
     *
     * @return 日期列表
     */
    private static List<String> getRecentlyDateList(int month) {
        LocalDate currentDate = LocalDate.now().minusMonths(month-1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        List<String> lastMonths = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String date = currentDate.plusMonths(i).format(formatter);
            lastMonths.add(date);
        }
        return lastMonths;
    }


    public AssetRespVO changeAssetName(AssetRespVO respVO){
        //全部是从缓存里取的数据，缓存里取不到的转义都连表查了
        respVO.setAssetClassName(dictItemService.getDictData(DictType.ASSET_CLASS.getType(),respVO.getAssetClass()));
        respVO.setAssetStatusName(dictItemService.getDictData(DictType.ASSET_STATUS.getType(),respVO.getAssetStatus()));
        if(respVO.getAssetClass().equals(AssetClass.LOGICAL.getId())){//逻辑资产
            respVO.setAssetTypeName(dictItemService.getDictData(DictType.LOGICAL_ASSET_TYPE.getType(),respVO.getAssetType()));
        }
        if(respVO.getAssetClass().equals(AssetClass.PHYSICAL.getId())){//物理资产
            AssetTypeRespVO type = assetTypeService.getAssetTypeById(Integer.valueOf(respVO.getAssetType()));
            if(type!=null){
                respVO.setAssetCategory(type.getAssetType());
                respVO.setTypeName(type.getTypeName());
                respVO.setAssetTypeName(type.getAssetType()+"-"+type.getTypeName());
            }
        }
        respVO.setAssetSecurityStatusName(dictItemService.getDictData(DictType.ASSET_SECURITY_STATUS.getType(),respVO.getAssetSecurityStatus()));
 /*       AssetGroup assetGroup = assetGroupService.getById(respVO.getAssetGroupId());
        respVO.setAssetGroupName(assetGroup==null?"":assetGroup.getGroupName());*/
        respVO.setAssetOrgName(orgService.changeOrgName(respVO.getAssetOrg()));
       /* if(!StringUtils.isBlank(respVO.getAssetManager())){
            M4SsoUser user = userService.getOne(new LambdaQueryWrapper<M4SsoUser>().eq(M4SsoUser::getUserId, respVO.getAssetManager()).last("LIMIT 1"));
            if(user!=null){
                respVO.setAssetManagerName(user.getUserName());
            }
        }*/
        return respVO;
    }
}
