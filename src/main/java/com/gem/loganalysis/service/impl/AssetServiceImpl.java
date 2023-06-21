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
import com.gem.loganalysis.model.bo.SNMPCollectInfo;
import com.gem.loganalysis.model.dto.IpDTO;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.*;
import com.gem.loganalysis.model.vo.*;
import com.gem.loganalysis.model.vo.asset.*;
import com.gem.loganalysis.model.vo.vulnerability.VulnerabilityScanningVO;
import com.gem.loganalysis.service.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.text.DecimalFormat;
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
    private IM4SsoOrgService orgService;
    @Resource
    private IAssetGroupService groupService;
    @Resource
    private IPhysicalAssetTempService physicalAssetTempService;
    @Resource
    private ILogicalAssetTempService logicalAssetTempService;
    @Resource
    private IAssetTypeService assetTypeService;
    @Resource
    private IM4SsoUserService userService;
    @Resource
    private BusinessConfigInfo businessConfigInfo;
    @Resource
    private VulnerabilityService vulnerabilityService;
    @Resource
    private IAssetRiskService riskService;
    @Resource
    private IDailyDataService dailyDataService;

    /**
     * 获取最近X个月日期
     *
     * @return 日期列表
     */
    private static List<String> getRecentlyDateList(int month) {
        LocalDate currentDate = LocalDate.now().minusMonths(month - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        List<String> lastMonths = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String date = currentDate.plusMonths(i).format(formatter);
            lastMonths.add(date);
        }
        return lastMonths;
    }

    //计算资产评分
    public static double calculateOverallSecurityScore(List<Double> vulnerabilities, double mediumWeight, double highWeight) {
        if (vulnerabilities == null || vulnerabilities.isEmpty()) {
            return 100.00; // 如果 vulnerabilities 列表为空，直接返回 100 分
        }
        double totalVulnerabilities = vulnerabilities.size();

        double maxLowVulnerability = 0;
        double maxMediumVulnerability = 0;
        double maxHighVulnerability = 0;

        for (double vulnerability : vulnerabilities) {
            if (vulnerability >= 0 && vulnerability < 4) {
                if (vulnerability > maxLowVulnerability) {
                    maxLowVulnerability = vulnerability;
                }
            } else if (vulnerability >= 4 && vulnerability < 7) {
                if (vulnerability > maxMediumVulnerability) {
                    maxMediumVulnerability = vulnerability;
                }
            } else if (vulnerability >= 7 && vulnerability <= 10) {
                if (vulnerability > maxHighVulnerability) {
                    maxHighVulnerability = vulnerability;
                }
            }
        }

        double mediumScore = maxMediumVulnerability * mediumWeight * (1 + getVulnerabilityCount(vulnerabilities, 4, 7) / totalVulnerabilities);
        double highScore = maxHighVulnerability * highWeight * (1 + getVulnerabilityCount(vulnerabilities, 7, 10) / totalVulnerabilities);

        double overallSecurityScore = 100 - maxLowVulnerability - mediumScore - highScore;

        // 格式化分数并保留两位小数
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedScore = decimalFormat.format(overallSecurityScore);
        overallSecurityScore = Double.parseDouble(formattedScore);

        return overallSecurityScore;
    }

    //计算范围内的漏洞数量
    private static double getVulnerabilityCount(List<Double> vulnerabilities, double minScore, double maxScore) {
        double count = 0;
        for (double vulnerability : vulnerabilities) {
            if (vulnerability >= minScore && vulnerability <= maxScore) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {

        // 漏洞脆弱性的分数列表
        List<Double> vulnerabilities = Arrays.asList(1.5, 2.0, 5.5, 8.0, 9.5);

        // 中危加权系数和高危加权系数
        double mediumWeight = 1.2;
        double highWeight = 2;

        double overallSecurityScore = calculateOverallSecurityScore(vulnerabilities, mediumWeight, highWeight);

        System.out.println("整体安全性得分: " + overallSecurityScore);
    }

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
        if (StringUtils.isBlank(dto.getAssetStatus())) {
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
    public PageResponse<AssetRespVO> getPageList(PageRequest<AssetQueryDTO> dto) throws JSONException {
        com.github.pagehelper.Page<AssetRespVO> result = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        assetMapper.getAssetList(dto.getData());
        //漏洞列表
        List<VulnerabilityScanningVO> severity = vulnerabilityService.getAggregateForResultBySeverity();
        //构成IP为KEY，severity值数组为VALUE的形式
        Map<String, List<Double>> collect = severity.stream()
                .filter(item -> item.getHost_ip() != null && item.getSeverity() != null)
                .collect(Collectors.groupingBy(VulnerabilityScanningVO::getHost_ip,
                        Collectors.mapping(VulnerabilityScanningVO::getSeverity, Collectors.toList())));
        result.forEach(e -> {
            //资产评分
            //权重系数为中1.2，高2
            e.setScore(calculateOverallSecurityScore(collect.get(e.getIpAddress()), 1.2, 2));
            //资产的安全状态80-100给正常 60-80给告警 0-60给危险
            e.setAssetSecurityStatus(getSecurityStatus(e.getScore()));
            //转义
            changeAssetName(e);
        });
        return new PageResponse<>(result);
    }

    private String getSecurityStatus(Double score) {
        if (score >= 80 && score <= 100) {
            return "2";
        } else if (score >= 60 && score < 80) {
            return "1";
        } else if (score >= 0 && score < 60) {
            return "0";
        } else {
            return "3";
        }
    }

    @Override
    public List<AssetRespVO> getAssetList(AssetQueryDTO dto) throws JSONException {
        List<AssetRespVO> assetRespVOList = assetMapper.getAssetList(dto);
        //漏洞列表
        List<VulnerabilityScanningVO> severity = vulnerabilityService.getAggregateForResultBySeverity();
        //构成IP为KEY，severity值数组为VALUE的形式
        Map<String, List<Double>> collect = severity.stream()
                .filter(item -> item.getHost_ip() != null && item.getSeverity() != null)
                .collect(Collectors.groupingBy(VulnerabilityScanningVO::getHost_ip,
                        Collectors.mapping(VulnerabilityScanningVO::getSeverity, Collectors.toList())));
        assetRespVOList.forEach(e -> {
            //资产评分
            //权重系数为中1.2，高2
            e.setScore(calculateOverallSecurityScore(collect.get(e.getIpAddress()), 1.2, 2));
            //资产的安全状态80-100给正常 60-80给告警 0-60给危险
            e.setAssetSecurityStatus(getSecurityStatus(e.getScore()));
            //转义
            changeAssetName(e);
        });
        return assetRespVOList;
    }

    @Override
    public ImportRespVO importLogicalExcel(List<LogicalAssetExcelVO> list) {
        String regex = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
        ImportRespVO respVO = ImportRespVO.builder().successNames(new ArrayList<>())
                .failNames(new LinkedHashMap<>()).build();
        list.forEach(e -> {
            try {
                if (StringUtils.isBlank(e.getAssetName())) {
                    throw new ServiceException("导入的资产名称不能为空");
                }
                if (e.getAssetName().equals("逻辑资产示例")) {
                    return;
                }
                if (StringUtils.isBlank(e.getAssetTypeName())) {
                    throw new ServiceException("导入的资产类型不能为空");
                }
                if (StringUtils.isBlank(e.getIpAddress())) {
                    throw new ServiceException("导入的IP地址为空");
                }
                if (!Pattern.matches(regex, e.getIpAddress())) {
                    //效验IP地址格式是否正确
                    throw new ServiceException("请输入正确的IP格式");
                }
                if (!StringUtils.isBlank(e.getServicePort()) && !e.getServicePort().matches("\\d+")) {
                    throw new ServiceException("请确保服务端口号为纯数字");
                }
                Asset asset = AssetConvert.INSTANCE.convert(e);
                asset.setAssetClass(AssetClass.LOGICAL.getId());
                //判断资产名称是否存在
                Asset one = this.getOne(new LambdaQueryWrapper<Asset>()
                        .eq(Asset::getIpAddress, e.getIpAddress())
                        .eq(Asset::getServicePort,e.getServicePort())
                        .last("LIMIT 1"));
                if (one != null) {
                    throw new ServiceException("该资产:" + e.getAssetName() + "已存在");
                }
                //资产类型导入
                if (!StringUtils.isBlank(e.getAssetTypeName())) {
                    String value = dictItemService.getValueByText(DictType.LOGICAL_ASSET_TYPE.getType(), e.getAssetTypeName());
                    if (value == null) {
                        throw new ServiceException("请导入数据字典中存在的逻辑资产的类型");
                    }
                    asset.setAssetType(value);
                }
                //资产部门导入
                if (!StringUtils.isBlank(e.getAssetOrgName())) {
                    M4SsoOrg org = orgService.getOne(new LambdaQueryWrapper<M4SsoOrg>()
                            .eq(M4SsoOrg::getOrgName, e.getAssetOrgName()).last("LIMIT 1"));
                    if (org == null) {
                        //优先放导入的部门，没有就放登录人的部门，如果导入的部门不存在，且登录人的部门也不存在，就报异常
                        if (!StringUtils.isBlank(getLoginUserOrgId())) {
                            asset.setAssetOrg(getLoginUserOrgId());
                        } else {
                            throw new ServiceException("您导入的资产部门系统中不存在，请先添加部门");
                        }
                    } else {
                        asset.setAssetOrg(org.getOrgId());
                    }
                }
                //资产分组导入
                if (!StringUtils.isBlank(e.getAssetGroupName())) {
                    AssetGroup group = groupService.getOne(new LambdaQueryWrapper<AssetGroup>()
                            .eq(AssetGroup::getGroupName, e.getAssetGroupName()).last("LIMIT 1"));
                    if (group == null) {
                        throw new ServiceException("您导入的资产分组系统中不存在，请先添加分组");
                    }
                    asset.setAssetGroupId(group.getGroupId());
                }
                //负责人导入
                if (!StringUtils.isBlank(e.getAssetManagerName())) {
                    M4SsoUser user = userService.getOne(new LambdaQueryWrapper<M4SsoUser>().eq(M4SsoUser::getAccount, e.getAssetManagerName()).last("LIMIT 1"));
                    if (user == null) {
                        throw new ServiceException("您导入的资产管理人系统中不存在，请先添加用户");
                    }
                    asset.setAssetManager(user.getUserId());
                }
                assetMapper.insert(asset);
                respVO.getSuccessNames().add(e.getAssetName());
            } catch (Exception ex) {
                respVO.getFailNames().put(e.getAssetName() != null ? e.getAssetName() : "空资产名", ex.getMessage());
            }
        });
        return respVO;
    }

    @Override
    public ImportRespVO importPhysicalExcel(List<PhysicalAssetExcelVO> list) {
        String regex = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
        ImportRespVO respVO = ImportRespVO.builder().successNames(new ArrayList<>())
                .failNames(new LinkedHashMap<>()).build();
        list.forEach(e -> {
            try {
                if (StringUtils.isBlank(e.getAssetName())) {
                    throw new ServiceException("导入的资产名称不能为空");
                }
                if (e.getAssetName().equals("物理资产示例")) {
                    return;
                }
                if (StringUtils.isBlank(e.getAssetCategory())) {
                    throw new ServiceException("导入的资产类别不能为空");
                }
                if (StringUtils.isBlank(e.getTypeName())) {
                    throw new ServiceException("导入的资产类型不能为空");
                }
                if (StringUtils.isBlank(e.getIpAddress())) {
                    throw new ServiceException("导入的IP地址为空");
                }
                if (!Pattern.matches(regex, e.getIpAddress())) {
                    //效验IP地址格式是否正确
                    throw new ServiceException("请输入正确的IP格式");
                }
                if (!StringUtils.isBlank(e.getServicePort()) && !e.getServicePort().matches("\\d+")) {
                    throw new ServiceException("请确保服务端口号为纯数字");
                }
                //判断资产名称是否存在
                Asset one = this.getOne(new LambdaQueryWrapper<Asset>()
                        .eq(Asset::getAssetName, e.getAssetName()).last("LIMIT 1"));
                if (one != null) {
                    throw new ServiceException("该资产:" + e.getAssetName() + "已存在");
                }
                Asset asset = AssetConvert.INSTANCE.convert(e);
                asset.setAssetClass(AssetClass.PHYSICAL.getId());
                //资产类型导入
                if (!StringUtils.isBlank(e.getTypeName())) {
                    String assetTypeId = assetTypeService.getAssetTypeId(e.getTypeName());
                    if (assetTypeId == null) {
                        throw new ServiceException("您导入的资产类型不存在,请先去配置管理中添加");
                    }
                    asset.setAssetType(assetTypeId);
                }
                //资产部门导入
                if (!StringUtils.isBlank(e.getAssetOrgName())) {
                    M4SsoOrg org = orgService.getOne(new LambdaQueryWrapper<M4SsoOrg>()
                            .eq(M4SsoOrg::getOrgName, e.getAssetOrgName()).last("LIMIT 1"));
                    if (org == null) {
                        //优先放导入的部门，没有就放登录人的部门，如果导入的部门不存在，且登录人的部门也不存在，就报异常
                        if (!StringUtils.isBlank(getLoginUserOrgId())) {
                            asset.setAssetOrg(getLoginUserOrgId());
                        } else {
                            throw new ServiceException("您导入的资产部门系统中不存在，请先添加部门");
                        }
                    } else {
                        asset.setAssetOrg(org.getOrgId());
                    }
                }
                //资产分组导入
                if (!StringUtils.isBlank(e.getAssetGroupName())) {
                    AssetGroup group = groupService.getOne(new LambdaQueryWrapper<AssetGroup>()
                            .eq(AssetGroup::getGroupName, e.getAssetGroupName()).last("LIMIT 1"));
                    if (group == null) {
                        throw new ServiceException("您导入的资产分组系统中不存在，请先添加分组");
                    }
                    asset.setAssetGroupId(group.getGroupId());
                }
                //负责人导入
                if (!StringUtils.isBlank(e.getAssetManagerName())) {
                    M4SsoUser user = userService.getOne(new LambdaQueryWrapper<M4SsoUser>().eq(M4SsoUser::getAccount, e.getAssetManagerName()).last("LIMIT 1"));
                    if (user == null) {
                        throw new ServiceException("您导入的资产管理人系统中不存在，请先添加用户");
                    }
                    asset.setAssetManager(user.getUserId());
                }
                assetMapper.insert(asset);
                respVO.getSuccessNames().add(e.getAssetName());
            } catch (Exception ex) {
                respVO.getFailNames().put(e.getAssetName() != null ? e.getAssetName() : "空资产名", ex.getMessage());
            }
        });
        return respVO;
    }

    @Override
    public HomeOverviewVO getHomeOverview() throws JSONException {

        HomeOverviewVO homeOverview = assetMapper.getAssetHomeOverview();
        homeOverview.setLogicalAssetCount((int)this.count(new LambdaQueryWrapper<Asset>()
                .eq(Asset::getAssetClass,"0")));
        /*List<AssetEventHomeOverviewVO> event = assetMapper.getEventHomeOverview();
        event.forEach(e -> {
            if (e.getAssetType().equals("安全设备")) {
                homeOverview.setSecurityDeviceEventsResolvedCount(e.getTotalEventCount()) // 当日安全设备事件总次数
                        .setSecurityDeviceEventsPendingCount(e.getPendingEventCount()); // 当日安全设备事件未处理次数
            } else if (e.getAssetType().equals("网络设备")) {
                homeOverview.setNetworkDeviceEventsResolvedCount(e.getTotalEventCount()) // 当日网络设备事件已处理次数
                        .setNetworkDeviceEventsPendingCount(e.getPendingEventCount()); // 当日网络设备事件未处理次数
            } else if (e.getAssetType().equals("服务器")) {
                homeOverview.setItDeviceEventsResolvedCount(e.getTotalEventCount()) // 当日IT设备事件总次数
                        .setItDeviceEventsPendingCount(e.getPendingEventCount()); // 当日IT设备事件未处理次数
            }
        });*/
        // 数据
        List<RiskAssetRankingVO> nonEndpointRiskAssetRanking = new ArrayList<>();//网络安全设备脆弱性
        List<RiskAssetRankingVO> endpointRiskAssetRanking = new ArrayList<>();//IT设备脆弱性

/*        //备用数据
        RiskAssetRankingVO asset15 = new RiskAssetRankingVO();
        asset15.setName("172.16.208.31");
        asset15.setScore(10.0);
        endpointRiskAssetRanking.add(asset15);

        RiskAssetRankingVO asset25 = new RiskAssetRankingVO();
        asset25.setName("192.168.24.30");
        asset25.setScore(7.5);
        endpointRiskAssetRanking.add(asset25);

        RiskAssetRankingVO asset35 = new RiskAssetRankingVO();
        asset35.setName("192.168.5.93");
        asset35.setScore(7.5);
        endpointRiskAssetRanking.add(asset35);

        RiskAssetRankingVO asset45 = new RiskAssetRankingVO();
        asset45.setName("192.168.6.15");
        asset45.setScore(6.4);
        endpointRiskAssetRanking.add(asset45);

        RiskAssetRankingVO asset55 = new RiskAssetRankingVO();
        asset55.setName("192.168.6.12");
        asset55.setScore(5.9);
        endpointRiskAssetRanking.add(asset55);

        RiskAssetRankingVO asset1 = new RiskAssetRankingVO();
        asset1.setName("172.16.200.75");
        asset1.setScore(6.4);
        nonEndpointRiskAssetRanking.add(asset1);

        RiskAssetRankingVO asset2 = new RiskAssetRankingVO();
        asset2.setName("172.16.200.77");
        asset2.setScore(6.4);
        nonEndpointRiskAssetRanking.add(asset2);

        RiskAssetRankingVO asset3 = new RiskAssetRankingVO();
        asset3.setName("172.16.200.78");
        asset3.setScore(6.4);
        nonEndpointRiskAssetRanking.add(asset3);

        RiskAssetRankingVO asset4 = new RiskAssetRankingVO();
        asset4.setName("172.16.200.76");
        asset4.setScore(5.3);
        nonEndpointRiskAssetRanking.add(asset4);

        RiskAssetRankingVO asset5 = new RiskAssetRankingVO();
        asset5.setName("172.16.200.54");
        asset5.setScore(5.3);
        nonEndpointRiskAssetRanking.add(asset5);*/
        List<Map.Entry<String, Double>> netSecurityDeviceTop5 = vulnerabilityService.getNetSecurityDeviceTop5();
        for (Map.Entry<String, Double> entry : netSecurityDeviceTop5) {
            String ipAddress = entry.getKey();
            Double severity = entry.getValue();
            nonEndpointRiskAssetRanking.add(new RiskAssetRankingVO(ipAddress, severity));
        }

        List<HostsSeverityVO> deviceTop = vulnerabilityService.getDeviceTop();
        List<AssetRespVO> assetList = getAssetList(new AssetQueryDTO());
        //所有数据和IT设备比对，放入IT设备
        List<HostsSeverityVO> filteredData = deviceTop.stream()
                .filter(device -> assetList.stream()
                        .anyMatch(asset -> asset.getIpAddress().equals(device.getIp()) && asset.getAssetCategory().equals("服务器")))
                .limit(5)
                .collect(Collectors.toList());
        filteredData.forEach(e -> {
            endpointRiskAssetRanking.add(new RiskAssetRankingVO(e.getIp(), e.getSeverity()));
        });
        //漏洞
        VulnDataVO vulnDataVO = vulnerabilityService.getAggregateForVulnBySeverity();
        Integer totalVuln = vulnDataVO.getLow() + vulnDataVO.getMiddle() + vulnDataVO.getHigh();
        //风险 目前漏洞没有放到风险里 所以风险个数为漏洞+风险
        int riskCount = (int) riskService.count();
        homeOverview
                .setAssetTotalScore(vulnDataVO.getScore()) // 资产总评分
                .setLowVulnerabilityCount(vulnDataVO.getLow())//低危漏洞
                .setMediumVulnerabilityCount(vulnDataVO.getMiddle())//中危漏洞
                .setHighVulnerabilityCount(vulnDataVO.getHigh())//高危漏洞
                .setSecurityVulnerabilityCount(totalVuln) // 安全漏洞数量
                .setTotalRiskCount(riskCount + totalVuln); // 风险总数

        //备用数据
    /*    int riskCount =(int) riskService.count();
        homeOverview
                .setAssetTotalScore(68.92) // 资产总评分
                .setLowVulnerabilityCount(5)//低危漏洞
                .setMediumVulnerabilityCount(24)//中危漏洞
                .setHighVulnerabilityCount(3)//高危漏洞
                .setSecurityVulnerabilityCount(32) // 安全漏洞数量
                .setTotalRiskCount(riskCount+32); // 风险总数*/

        List<DailyData> dataList = dailyDataService.list(new LambdaQueryWrapperX<DailyData>().orderByDesc(DailyData::getDateTime).last("LIMIT 7"));
        //变年份去掉并且根据时间排序
        List<DailyData> list = dataList.stream()
                .sorted(Comparator.comparing(dailyData -> LocalDate.parse(dailyData.getDateTime())))
                .map(dailyData -> {
                    String formattedDate = LocalDate.parse(dailyData.getDateTime())
                            .format(DateTimeFormatter.ofPattern("MM-dd"));
                    dailyData.setDateTime(formattedDate);
                    return AssetConvert.INSTANCE.convert(dailyData);
                })
                .collect(Collectors.toList());
        List<String> dateList = new ArrayList<>(); // 近七天日期集合
        List<Integer> lowRiskCountList = new ArrayList<>(); // 近七天低风险集合
        List<Integer> mediumRiskCountList = new ArrayList<>(); // 近七天中风险集合
        List<Integer> highRiskCountList = new ArrayList<>(); // 近七天高风险集合
        List<Double> exportDeviceLoadList = new ArrayList<>(); // 近七天出口设备负荷(流量)
        List<Double> securityDeviceAssetScoreList = new ArrayList<>(); // 近七天安全设备资产评分集合
        List<Double> networkDeviceAssetScoreList = new ArrayList<>(); // 近七天网络设备资产评分集合
        List<Double> itDeviceAssetScoreList = new ArrayList<>(); // 近七天IT设备资产评分集合
        //List<Double> logicalAssetScoreList = new ArrayList<>(); // 近七天逻辑资产评分集合 逻辑资产暂无评分
        list.forEach(e -> {
            dateList.add(e.getDateTime() != null ? e.getDateTime() : "0");
            lowRiskCountList.add(e.getLowRiskCount() != null ? e.getLowRiskCount() : 0);
            mediumRiskCountList.add(e.getMediumRiskCount() != null ? e.getMediumRiskCount() : 0);
            highRiskCountList.add(e.getHighRiskCount() != null ? e.getHighRiskCount() : 0);
            exportDeviceLoadList.add(e.getExportDeviceLoad() != null ? e.getExportDeviceLoad() : 0.0);
            securityDeviceAssetScoreList.add(e.getSecurityDeviceAssetScore() != null ? e.getSecurityDeviceAssetScore() : 0.0);
            networkDeviceAssetScoreList.add(e.getNetworkDeviceAssetScore() != null ? e.getNetworkDeviceAssetScore() : 0.0);
            itDeviceAssetScoreList.add(e.getItDeviceAssetScore() != null ? e.getItDeviceAssetScore() : 0.0);
            //logicalAssetScoreList.add(e.getLogicalAssetScore() != null ? e.getLogicalAssetScore() : 0.0); //逻辑资产暂无评分
        });

        homeOverview
                .setDateList(dateList) // 近七天日期集合
                .setLowRiskCount(lowRiskCountList) // 近七天低风险集合
                .setMediumRiskCount(mediumRiskCountList) // 近七天中风险集合
                .setHighRiskCount(highRiskCountList) // 近七天高风险集合
                .setExportDeviceLoad(exportDeviceLoadList) // 近七天出口设备负荷(流量)
                .setSecurityDeviceAssetScore(securityDeviceAssetScoreList)//近七天安全设备资产评分集合
                .setNetworkDeviceAssetScore(networkDeviceAssetScoreList)//近七天网络设备资产评分集合
                .setItDeviceAssetScore(itDeviceAssetScoreList)//近七天IT设备资产评分集合
                //.setLogicalAssetScore(logicalAssetScoreList)//近七天逻辑资产评分集合
                .setNonEndpointRiskAssetRanking(nonEndpointRiskAssetRanking)//网络安全设备脆弱性
                .setEndpointRiskAssetRanking(endpointRiskAssetRanking);//IT设备脆弱性

        //备用演示数据
      /*  homeOverview
                .setDateList(weekDateList()) // 近七天日期集合
                .setLowRiskCount(Arrays.asList(5, 6, 3, 8, 4, 4, 4)) // 近七天低风险集合
                .setMediumRiskCount(Arrays.asList(15, 14, 13, 14, 14, 15, 16)) // 近七天中风险集合
                .setHighRiskCount(Arrays.asList(1, 0, 2, 1, 3, 0, 1)) // 近七天高风险集合
                .setExportDeviceLoad(Arrays.asList(590, 640, 850, 730, 800, 840, 900)) // 近七天出口设备负荷(流量)
                .setSecurityDeviceAssetScore(Arrays.asList(85, 90, 92, 88, 95 ,95, 89))//近七天安全设备资产评分集合
                .setNetworkDeviceAssetScore(Arrays.asList(82, 85, 89, 86, 88 ,90 , 88))//近七天网络设备资产评分集合
                .setItDeviceAssetScore(Arrays.asList(80, 85, 90, 88, 86 ,86, 91))//近七天IT设备资产评分集合
                .setLogicalAssetScore(Arrays.asList(90, 88, 92, 85, 91, 92, 89))//近七天逻辑资产评分集合
                .setNonEndpointRiskAssetRanking(nonEndpointRiskAssetRanking)//网络安全设备脆弱性
                .setEndpointRiskAssetRanking(endpointRiskAssetRanking);//IT设备脆弱性*/
        return homeOverview;
    }

    @Override
    public HomeOverviewVO getHomeOverviewMonitor() {
        HomeOverviewVO homeOverview = assetMapper.getAssetHomeOverview();
        homeOverview.setLogicalAssetCount((int)this.count(new LambdaQueryWrapper<Asset>()
                .eq(Asset::getAssetClass,"0")));
        List<AssetEventHomeOverviewVO> event = assetMapper.getEventHomeOverview();
        event.forEach(e -> {
            if (e.getAssetType().equals("安全设备")) {
                homeOverview.setSecurityDeviceEventsResolvedCount(e.getTotalEventCount()) // 当日安全设备事件总次数
                        .setSecurityDeviceEventsPendingCount(e.getPendingEventCount()); // 当日安全设备事件未处理次数
            } else if (e.getAssetType().equals("网络设备")) {
                homeOverview.setNetworkDeviceEventsResolvedCount(e.getTotalEventCount()) // 当日网络设备事件已处理次数
                        .setNetworkDeviceEventsPendingCount(e.getPendingEventCount()); // 当日网络设备事件未处理次数
            } else if (e.getAssetType().equals("服务器")) {
                homeOverview.setItDeviceEventsResolvedCount(e.getTotalEventCount()) // 当日IT设备事件总次数
                        .setItDeviceEventsPendingCount(e.getPendingEventCount()); // 当日IT设备事件未处理次数
            }
        });
        return homeOverview;
    }

    @Override
    public ScreeShowVO screenShow(String type) throws JSONException {
        if (StringUtils.isBlank(type)) {
            throw new ServiceException("请传入风险TOP10类型,0为显示全部");
        }
        ScreeShowVO result = new ScreeShowVO();
        HomeOverviewVO homeOverview = assetMapper.getAssetHomeOverview();
        result.setSafeEquipmentNum(homeOverview.getSecurityDeviceTotalCount());
        result.setNetworkEquipmentNum(homeOverview.getNetworkDeviceTotalCount());
        result.setITEquipmentNum(homeOverview.getItDeviceTotalCount());
        result.setTerminalEquipmentNum(homeOverview.getEndpointDeviceTotalCount());

        List<HostsSeverityVO> deviceTop = vulnerabilityService.getDeviceTop();
        List<AssetRespVO> assetList = getAssetList(new AssetQueryDTO());
        //如果传入为0，则显示全部

        Map<String, AssetRespVO> ipToAssetNameMap = new HashMap<>();
        // 创建一个Map，以IP作为键，资产名称作为值
        for (AssetRespVO asset : assetList) {
            ipToAssetNameMap.put(asset.getIpAddress(), asset);
        }
        // 遍历deviceTop列表，并根据IP匹配资产名称
        for (HostsSeverityVO device : deviceTop) {
            String ipAddress = device.getIp();
            AssetRespVO asset = ipToAssetNameMap.get(ipAddress);
            if (asset != null) {
                device.setAssetName(asset.getAssetName());
                device.setAssetCategory(asset.getAssetCategory());
            }
            // 将匹配到的资产名称设置到device对象中
        }
        //如果为0不过滤，显示全部
        System.out.println(deviceTop);
        List<HostsSeverityVO> collect = deviceTop.stream()
                .filter(device -> type.equals("0") || (device.getAssetCategory()!=null&&device.getAssetCategory().equals(type)))
                .limit(10)
                .collect(Collectors.toList());
        result.setRiskAsset(collect);

     /*   LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Asset::getIpAddress, Asset::getAssetName, Asset::getAssetType)
                .eq(Asset::getAssetClass, "1")
                .eq(Asset::getDeleteState, 0);
        List<Asset> assetList = this.list(wrapper);
        // 创建一个Map,以IP作为键,资产对象作为值
        Map<String, List<Asset>> assetMap = assetList.stream().collect(Collectors.groupingBy(Asset::getIpAddress));
        List<HostsSeverityVO> typeDeviceTop = new ArrayList<>();
        AssetTypeTree typeTreeInstance = AssetTypeTree.getInstance();
        // 遍历deviceTop列表，并根据IP匹配资产名称
        for (HostsSeverityVO device : deviceTop) {
            String ipAddress = device.getIp();
            List<Asset> assets = assetMap.get(ipAddress);
            if (CollUtil.isNotEmpty(assets)) {
                Asset asset = assets.get(0);
                String assetName = asset.getAssetName();
                String assetType = asset.getAssetType();
                if (allType) {
                    device.setAssetName(assetName);
                } else {
                    Integer parentNodeId = typeTreeInstance.getParentNodeId(Integer.valueOf(assetType));
                    // 判断资产类型是否匹配
                    if (type.equals(parentNodeId.toString())) {
                        // 将匹配到的资产名称设置到device对象中
                        typeDeviceTop.add(device);
                        if (typeDeviceTop.size() == 10) {
                            deviceTop = typeDeviceTop;
                            break;
                        }
                    }
                }
            }
        }
        result.setRiskAsset(deviceTop);*/

        //备用数据
       /* List<HostsSeverityVO> hostSeverityList = new ArrayList<>();

        HostsSeverityVO host1 = new HostsSeverityVO();
        host1.setAssetName("办公-windows7");
        host1.setIp("172.16.208.31");
        host1.setSeverity(10);
        hostSeverityList.add(host1);

        HostsSeverityVO host2 = new HostsSeverityVO();
        host2.setAssetName("Master节点");
        host2.setIp("192.168.24.30");
        host2.setSeverity(7.5);
        hostSeverityList.add(host2);

        HostsSeverityVO host3 = new HostsSeverityVO();
        host3.setAssetName("疫情防控系统004");
        host3.setIp("192.168.5.93");
        host3.setSeverity(7.5);
        hostSeverityList.add(host3);

        HostsSeverityVO host4 = new HostsSeverityVO();
        host4.setAssetName("绿盟综合威胁探针");
        host4.setIp("172.16.200.75");
        host4.setSeverity(6.4);
        hostSeverityList.add(host4);

        HostsSeverityVO host5 = new HostsSeverityVO();
        host5.setAssetName("绿盟入侵防御1");
        host5.setIp("172.16.200.77");
        host5.setSeverity(6.4);
        hostSeverityList.add(host5);

        HostsSeverityVO host6 = new HostsSeverityVO();
        host6.setAssetName("翼安居平台服务器5");
        host6.setIp("192.168.6.15");
        host6.setSeverity(6.4);
        hostSeverityList.add(host6);

        HostsSeverityVO host7 = new HostsSeverityVO();
        host7.setAssetName("绿盟入侵防御2");
        host7.setIp("172.16.200.78");
        host7.setSeverity(6.4);
        hostSeverityList.add(host7);

        HostsSeverityVO host8 = new HostsSeverityVO();
        host8.setAssetName("devops004");
        host8.setIp("192.168.5.121");
        host8.setSeverity(5.3);
        hostSeverityList.add(host8);

        HostsSeverityVO host9 = new HostsSeverityVO();
        host9.setAssetName("一期存储磁阵");
        host9.setIp("172.16.200.60");
        host9.setSeverity(5.3);
        hostSeverityList.add(host9);

        HostsSeverityVO host10 = new HostsSeverityVO();
        host10.setAssetName("门禁系统001");
        host10.setIp("192.168.5.174");
        host10.setSeverity(5.3);
        hostSeverityList.add(host10);
        result.setRiskAsset(hostSeverityList);*/

        return result;
    }

    @Override
    public PageResponse<PhysicalAssetScannerRespVO> getUnmanagedPage(PageRequest<IpDTO> dto) {
        Page<PhysicalAssetScannerRespVO> result = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        physicalAssetTempService.getUnmanagedList(dto.getData());
        return new PageResponse<PhysicalAssetScannerRespVO>(result);
    }

    @Override
    public PageResponse<LogicalAssetScannerRespVO> getUnmanagedLogicalPage(PageRequest<IpDTO> dto) {
        Page<LogicalAssetScannerRespVO> result = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        logicalAssetTempService.getUnmanagedList(dto.getData());
        return new PageResponse<LogicalAssetScannerRespVO>(result);
    }

    @Override
    public List<VulnerabilityScanningVO> getVulnerabilityScanningList() {
        List<VulnerabilityScanningVO> severity = vulnerabilityService.getAggregateForResultBySeverity();
        return severity;
    }

    private List<String> weekDateList() {
        List<String> dateList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        for (int i = 6; i >= 0; i--) {
            String date = currentDate.minusDays(i).format(formatter);
            dateList.add(date);
        }
        return dateList;
    }

    @Override
    public AssetRespVO getAsset(String id) {
        return changeAssetName(assetMapper.getAssetById(id));
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
    public AssetOverviewVO getOverviewInfo() throws JSONException {
        AssetOverviewVO assetOverviewVO = new AssetOverviewVO();
        List<AssetRespVO> assetList = getAssetList(new AssetQueryDTO());
        //内存中进行计算
        //物理资产总数
        int physicalNum = (int) assetList.stream()
                .filter(e -> e.getAssetClass().equals("1")).count();
        assetOverviewVO.setPhysicalAssetNum(physicalNum);
        //逻辑资产总数
        assetOverviewVO.setLogicalAssetNum((int) assetList.stream()
                .filter(e -> e.getAssetClass().equals("0")).count());
        //逻辑资产类型分布
        Map<String, List<AssetRespVO>> logicalAsset = assetList.stream()
                .filter(e -> e.getAssetClass().equals("0") && StringUtils.isNotBlank(e.getAssetTypeName()))
                .collect(Collectors.groupingBy(AssetRespVO::getAssetTypeName));
        assetOverviewVO.setLogicalAssetDistribution(logicalAsset);
        //物理资产类型分布
        Map<String, List<AssetRespVO>> physicalType = assetList.stream()
                .filter(e -> e.getAssetClass().equals("1") && StringUtils.isNotBlank(e.getTypeName()))
                .collect(Collectors.groupingBy(AssetRespVO::getTypeName));
        assetOverviewVO.setPhysicalAssetTypeDistribution(physicalType);
        //资产状态分布
        Map<String, List<AssetRespVO>> assetStatus = assetList.stream()
                .filter(e -> e.getAssetClass().equals("1") && StringUtils.isNotBlank(e.getAssetStatusName()))
                .collect(Collectors.groupingBy(AssetRespVO::getAssetStatusName));
        //assetOverviewVO.setAssetStatusDistribution(assetStatus);
        assetOverviewVO.setAssetOnlineStatusDistribution(assetStatus);
        //资产类别分布
        Map<String, List<AssetRespVO>> assetCategory = assetList.stream()
                .filter(e -> e.getAssetClass().equals("1") && StringUtils.isNotBlank(e.getAssetCategory()))
                .collect(Collectors.groupingBy(AssetRespVO::getAssetCategory));
        assetOverviewVO.setAssetCategoryDistribution(assetCategory);
        //资产纳管状态1.已纳管 就是资产表物理资产的总数据 2.未纳管就是资产扫描出来的物理资产表里还不存在的的总数
        Integer unmanagedCount = physicalAssetTempService.getUnmanagedCount();
        assetOverviewVO.setAssetStatusDistribution(physicalNum, unmanagedCount);
/*        //最近新增资产(10条)
        //这个不要了 统计的没意义 先注释掉
        List<AssetRespVO> sorted = assetList.stream()
                .sorted(Comparator.comparing(AssetRespVO::getCreateTime, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
        assetOverviewVO.setNewAssetList(AssetConvert.INSTANCE.convertList11(sorted));*/
        //最近资产发现(5条)
        assetOverviewVO.setNewAssetScanList(AssetConvert.INSTANCE.convertList06(physicalAssetTempService.getNewAssetScanList()));
        //端口应用TOP5
        Map<Integer, List<AssetRespVO>> port = assetList.stream()
                .filter(e -> e.getAssetClass().equals("0") && e.getServicePort() != null)
                .collect(Collectors.groupingBy(AssetRespVO::getServicePort));
        assetOverviewVO.setIpPortTop5(port);


        // 运行负荷Top5
        List<AssetOverviewVO.TypeNum> loadAssetTop5 = new ArrayList<>();
        List<ITAssetMonitorInfoVO> itLoadInfoList = SNMPCollectInfo.getInstance().getITLoadInfoList();
        for (int i = 0; i < Math.min(5, itLoadInfoList.size()); i++) {
            ITAssetMonitorInfoVO infoVO = itLoadInfoList.get(i);
            loadAssetTop5.add(new AssetOverviewVO.TypeNum(infoVO.getAssetName(), infoVO.getLoad()));
        }
        assetOverviewVO.setLoadAssetTop5(loadAssetTop5);

        //每日资产在线数量
        List<AssetOverviewVO.AssetTrendsList> assetTrendsListList = new ArrayList<>();
        //每日资产未纳管扫描数量
        List<AssetOverviewVO.AssetTrendsList> assetScanList = new ArrayList<>();
        //横轴
        List<String> dateList = new ArrayList<>();
        List<Integer> physicalScanList = new ArrayList<>();
        List<Integer> logicalScanList = new ArrayList<>();
        List<Integer> physicalOnlineList = new ArrayList<>();
        List<Integer> logicalOnlineList = new ArrayList<>();
        //每日数据
        List<DailyData> dataList = dailyDataService.list(new LambdaQueryWrapperX<DailyData>().orderByDesc(DailyData::getDateTime).last("LIMIT 7"));
        //变年份去掉并且根据时间排序
        List<DailyData> list = dataList.stream()
                .sorted(Comparator.comparing(dailyData -> LocalDate.parse(dailyData.getDateTime())))
                .map(dailyData -> {
                    String formattedDate = LocalDate.parse(dailyData.getDateTime())
                            .format(DateTimeFormatter.ofPattern("MM-dd"));
                    dailyData.setDateTime(formattedDate);
                    return AssetConvert.INSTANCE.convert(dailyData);
                })
                .collect(Collectors.toList());
        list.forEach(e -> {
            dateList.add(e.getDateTime() != null ? e.getDateTime() : "0");
            physicalScanList.add(e.getPhysicalAssetsScanCount() != null ? e.getPhysicalAssetsScanCount() : 0);
            logicalScanList.add(e.getLogicalAssetsScanCount() != null ? e.getLogicalAssetsScanCount() : 0);
            physicalOnlineList.add(e.getPhysicalAssetsOnlineCount() != null ? e.getPhysicalAssetsOnlineCount() : 0);
            logicalOnlineList.add(e.getLogicalAssetsOnlineCount() != null ? e.getLogicalAssetsOnlineCount() : 0);
        });
        assetScanList.add(new AssetOverviewVO.AssetTrendsList("物理资产",physicalScanList));
        assetScanList.add(new AssetOverviewVO.AssetTrendsList("逻辑资产",logicalScanList));
        assetTrendsListList.add(new AssetOverviewVO.AssetTrendsList("物理资产",physicalOnlineList));
        assetTrendsListList.add(new AssetOverviewVO.AssetTrendsList("逻辑资产",logicalOnlineList));

        assetOverviewVO.setDateList(dateList);
        assetOverviewVO.setAssetScanList(assetScanList);
        assetOverviewVO.setAssetTrendsList(assetTrendsListList);
        //不查实时的了，从每日数据里取
        /*List<String> recentlyDateList = getRecentlyDateList(6);
        assetOverviewVO.setDateList(recentlyDateList);
        //物理资产在线数量
        Map<String, Long> physicalMap = getAssetTrendsMap(assetList, "1");
        AssetOverviewVO.AssetTrendsList physicalList = new AssetOverviewVO.AssetTrendsList();
        physicalList.setAssetClass("物理资产");
        List<Integer> physicalDataList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : physicalMap.entrySet()) {
            physicalDataList.add(entry.getValue().intValue());
        }
        physicalList.setList(physicalDataList);
        assetTrendsListList.add(physicalList);
        //逻辑资产在线数量
        Map<String, Long> logicalMap = getAssetTrendsMap(assetList, "0");
        AssetOverviewVO.AssetTrendsList logicalList = new AssetOverviewVO.AssetTrendsList();
        logicalList.setAssetClass("逻辑资产");
        List<Integer> logicalDataList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : logicalMap.entrySet()) {
            logicalDataList.add(entry.getValue().intValue());
        }
        logicalList.setList(logicalDataList);
        assetTrendsListList.add(logicalList);
        assetOverviewVO.setAssetTrendsList(assetTrendsListList);*/
        return assetOverviewVO;
    }

    /**
     * @param assetList
     * @param assetType 0逻辑资产 1物理资产
     * @return
     */
    private Map<String, Long> getAssetTrendsMap(List<AssetRespVO> assetList, String assetType) {
        List<String> lastSixMonths = getRecentlyDateList(6);
        DateTimeFormatter fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, Long> countMap = assetList.stream()
                .filter(e -> e.getAssetClass().equals(assetType) && "0".equals(e.getAssetStatus()) && StringUtils.isNotEmpty(e.getCreateTime()))
                .map(asset -> LocalDate.parse(asset.getCreateTime(), fullFormat).format(monthFormat))
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

    public AssetRespVO changeAssetName(AssetRespVO respVO) {
        //全部是从缓存里取的数据，缓存里取不到的转义都连表查了
        respVO.setAssetClassName(dictItemService.getDictData(DictType.ASSET_CLASS.getType(), respVO.getAssetClass()));
        respVO.setAssetStatusName(dictItemService.getDictData(DictType.ASSET_STATUS.getType(), respVO.getAssetStatus()));
        if (respVO.getAssetClass().equals(AssetClass.LOGICAL.getId())) {//逻辑资产
            respVO.setAssetTypeName(dictItemService.getDictData(DictType.LOGICAL_ASSET_TYPE.getType(), respVO.getAssetType()));
        }
        if (respVO.getAssetClass().equals(AssetClass.PHYSICAL.getId())) {//物理资产
            AssetTypeRespVO type = assetTypeService.getAssetTypeById(Integer.valueOf(respVO.getAssetType()));
            if (type != null) {
                respVO.setAssetCategory(type.getAssetType());
                respVO.setTypeName(type.getTypeName());
                respVO.setAssetTypeName(type.getAssetType() + "-" + type.getTypeName());
            }
        }
        respVO.setAssetSecurityStatusName(dictItemService.getDictData(DictType.ASSET_SECURITY_STATUS.getType(), respVO.getAssetSecurityStatus()));
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
