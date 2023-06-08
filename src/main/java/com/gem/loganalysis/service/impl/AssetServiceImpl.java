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
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.M4SsoUser;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import com.gem.loganalysis.model.vo.ImportRespVO;
import com.gem.loganalysis.model.vo.asset.*;
import com.gem.loganalysis.service.*;
import com.gem.loganalysis.util.AESUtil;
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
    private IAssetGroupService assetGroupService;
    @Resource
    private IM4SsoOrgService orgService;
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
                if(StringUtils.isBlank(e.getAssetType())){
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
                asset.setAssetType(assetTypeService.getAssetTypeId(e.getAssetType()));
                if(assetTypeService.getAssetTypeId(e.getAssetType())==null){
                    throw new ServiceException("请导入资产类型配置中存在的资产类型，若不存在请先添加");
                }
                if(!StringUtils.isBlank(e.getAssetManager())){
                    M4SsoUser user = userService.getOne(new LambdaQueryWrapper<M4SsoUser>().eq(M4SsoUser::getUserName, e.getAssetManager()).last("LIMIT 1"));
                    if(user==null){
                        throw new ServiceException("您导入的资产管理人系统中不存在，请先添加用户");
                    }
                    asset.setAssetManager(user.getUserId());
                }
                asset.setAssetOrg(getLoginUserOrgId());
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
                if(StringUtils.isBlank(e.getAssetType())){
                    throw new ServiceException("导入的资产类型不能为空");
                }
                if(StringUtils.isBlank(e.getIpAddress())){
                    throw new ServiceException("导入的IP地址为空");
                }
                if(StringUtils.isBlank(e.getNmPort())){
                    throw new ServiceException("导入的网管端口不能为空");
                }
                if(StringUtils.isBlank(e.getNmProcotol())){
                    throw new ServiceException("导入的网管协议不能为空");
                }
                if(StringUtils.isBlank(e.getNmAccount())){
                    throw new ServiceException("导入的网管账号不能为空");
                }
                if(StringUtils.isBlank(e.getNmPassword())){
                    throw new ServiceException("导入的网管密码不能为空");
                }
                if(!Pattern.matches(regex,e.getIpAddress())){
                    //效验IP地址格式是否正确
                    throw new ServiceException("请输入正确的IP格式");
                }
                if(!StringUtils.isBlank(e.getServicePort())&&!e.getServicePort().matches("\\d+")){
                    throw new ServiceException("请确保服务端口号为纯数字");
                }
                if(!StringUtils.isBlank(e.getNmPort())&&!e.getNmPort().matches("\\d+")){
                    throw new ServiceException("请确保网管端口号为纯数字");
                }
                if(StringUtils.isBlank(e.getAssetName())){
                    throw new ServiceException("导入的资产名称不能为空");
                }

                Asset asset = AssetConvert.INSTANCE.convert(e);
                asset.setAssetClass(AssetClass.PHYSICAL.getId());
                asset.setAssetType(assetTypeService.getAssetTypeId(e.getAssetType()));
                if(assetTypeService.getAssetTypeId(e.getAssetType())==null){
                    throw new ServiceException("请导入资产类型配置中存在的资产类型，若不存在请先添加");
                }
                if(!StringUtils.isBlank(e.getAssetManager())){
                    M4SsoUser user = userService.getOne(new LambdaQueryWrapper<M4SsoUser>().eq(M4SsoUser::getUserName, e.getAssetManager()).last("LIMIT 1"));
                    if(user==null){
                        throw new ServiceException("您导入的资产管理人系统中不存在，请先添加用户");
                    }
                    asset.setAssetManager(user.getUserId());
                }
                //对于密码加密,不需要可注释
                if(!StringUtils.isBlank(e.getNmPassword())){
                    String password = AESUtil.aesEncrypt(e.getNmPassword(), businessConfigInfo.getAESKey());
                    asset.setNmPassword(password);
                }
                asset.setAssetOrg(getLoginUserOrgId());
                assetMapper.insert(asset);
                respVO.getSuccessNames().add(e.getAssetName());
            }catch (Exception ex){
                respVO.getFailNames().put(e.getAssetName()!=null?e.getAssetName():"空资产名", ex.getMessage());
            }
        });
        return respVO;
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
                .filter(e -> e.getAssetClass().equals("0"))
                .collect(Collectors.groupingBy(AssetRespVO::getAssetTypeName));
        assetOverviewVO.setLogicalAssetDistribution(logicalAsset);
        //物理资产类型分布
        Map<String, List<AssetRespVO>> physicalType = assetList.stream()
                .filter(e -> e.getAssetClass().equals("1"))
                .collect(Collectors.groupingBy(AssetRespVO::getAssetTypeName));
        assetOverviewVO.setPhysicalAssetTypeDistribution(physicalType);
        //资产状态分布
        Map<String, List<AssetRespVO>> assetStatus = assetList.stream()
                .collect(Collectors.groupingBy(AssetRespVO::getAssetStatusName));
        assetOverviewVO.setAssetStatusDistribution(assetStatus);
        assetOverviewVO.setAssetOnlineStatusDistribution(assetStatus);
        //最近新增资产(10条)
        List<AssetRespVO> sorted = assetList.stream()
                .sorted(Comparator.comparing(AssetRespVO::getCreateTime,Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
        assetOverviewVO.setNewAssetList(AssetConvert.INSTANCE.convertList11(sorted));
        //最近资产发现(5条)
        List<PhysicalAssetTemp> newAssetScanner = physicalAssetTempService.list(new LambdaQueryWrapperX<PhysicalAssetTemp>()
                .eq(PhysicalAssetTemp::getAssetStatus, 1)
                .orderByDesc(PhysicalAssetTemp::getScanTime)
                .last("LIMIT 5"));
        assetOverviewVO.setNewAssetScanList(AssetConvert.INSTANCE.convertList06(newAssetScanner));
        //主机开放端口Top5
        Map<String, List<AssetRespVO>> ip = assetList.stream()
                .filter(e -> e.getAssetClass().equals("0")&&StringUtils.isNotEmpty(e.getIpAddress()))
                .collect(Collectors.groupingBy(AssetRespVO::getIpAddress));
        assetOverviewVO.setIpTop5(ip);
        //主机端口Top5
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


    private AssetRespVO changeAssetName(AssetRespVO respVO){
        //全部是从缓存里取的数据，缓存里取不到的转义都连表查了
        respVO.setAssetClassName(dictItemService.getDictData(DictType.ASSET_CLASS.getType(),respVO.getAssetClass()));
        respVO.setAssetStatusName(dictItemService.getDictData(DictType.ASSET_STATUS.getType(),respVO.getAssetStatus()));
        if(respVO.getAssetClass().equals(AssetClass.LOGICAL.getId())){//逻辑资产
            respVO.setAssetTypeName(dictItemService.getDictData(DictType.LOGICAL_ASSET_TYPE.getType(),respVO.getAssetType()));
        }
        if(respVO.getAssetClass().equals(AssetClass.PHYSICAL.getId())){//物理资产
            respVO.setAssetTypeName(assetTypeService.getAssetTypeName(Integer.valueOf(respVO.getAssetType())));
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
