package com.gem.loganalysis.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.enmu.AssetClass;
import com.gem.loganalysis.enmu.DictType;
import com.gem.loganalysis.exception.ServiceException;
import com.gem.loganalysis.handler.DropDownWriteHandler;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.asset.*;
import com.gem.loganalysis.model.dto.query.DictItemQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.M4SsoOrg;
import com.gem.loganalysis.model.vo.DictItemRespVO;
import com.gem.loganalysis.model.vo.HomeOverviewVO;
import com.gem.loganalysis.model.vo.ImportRespVO;
import com.gem.loganalysis.model.vo.RiskAssetRankingVO;
import com.gem.loganalysis.model.vo.asset.*;
import com.gem.loganalysis.service.*;
import com.gem.loganalysis.util.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "资产模块 - 安全管理资产")
@RestController
@RequestMapping("/sop/asset")
@Validated
public class AssetController {

    @Resource
    private IAssetService assetService;
    @Resource
    private IAssetTypeService assetTypeService;
    @Resource
    private IAssetGroupService assetGroupService;
    @Resource
    private IM4SsoOrgService orgService;
    @Resource
    private DictItemService dictItemService;


    @PostMapping("/edit")
    @ApiOperation("创建/编辑安全管理资产")
    public Result<String> editAsset(@Valid @RequestBody AssetDTO dto) {
        return assetService.editAsset(dto);
    }

    @PostMapping("/batchInsert")
    @ApiOperation("批量插入安全管理资产")
    public Result<String> editAsset(@Valid @RequestBody List<AssetDTO> dto) {
        return assetService.saveBatch(AssetConvert.INSTANCE.convertList01(dto))?
                Result.ok("插入成功"):Result.failed("插入失败");
    }

    @PostMapping("/pageList")
    @ApiOperation("分页查询安全管理资产")
    public Result<PageResponse<AssetRespVO>> pageList(@RequestBody PageRequest<AssetQueryDTO> dto) {
        return Result.ok(assetService.getPageList(dto));
    }

    @PostMapping("/list")
    @ApiOperation("查询安全管理资产列表")
    public Result<List<AssetRespVO>> pageList(@RequestBody AssetQueryDTO dto) {
        return Result.ok(assetService.getAssetList(dto));
    }

    @PostMapping("/get")
    @ApiOperation("根据ID获得单一资产信息")
    public Result<AssetRespVO> getAsset(@RequestBody GetDTO dto) {
        if(dto.getId()==null||dto.getId().trim().equals("")){
            return Result.failed("请传入资产唯一编码ID");
        }
        return Result.ok(assetService.getAsset(dto.getId()));
    }

    @PostMapping("/getAccount")
    @ApiOperation("根据ID获得网管密码")
    public Result<AssetAccountRespVO> getAssetAccount(@RequestBody GetDTO dto) {
        if(dto.getId()==null||dto.getId().trim().equals("")){
            return Result.failed("请传入资产唯一编码ID");
        }
        return Result.ok(assetService.getAssetAccount(dto.getId()));
    }

    @PostMapping("/delete")
    @ApiOperation("删除资产")
    public Result<String> deleteAsset(@Valid @RequestBody DeleteDTO dto) {
        if(StringUtils.isBlank(dto.getId())){
            return Result.failed("请传入需要删除的资产ID");
        }
        return assetService.removeById(dto.getId()) ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    @PostMapping("/overview")
    @ApiOperation("资产总览")
    public Result<AssetOverviewVO> overview() {
        return Result.ok(assetService.getOverviewInfo());
    }

    @PostMapping("/addAssetTag")
    @ApiOperation("添加标签")
    public Result<String> addAssetTag(@Valid @RequestBody AssetUpdateDTO dto) {
        if(StringUtils.isBlank(dto.getAssetTag())){
            return Result.failed("请传入需要添加的资产标签");
        }
        Asset asset = assetService.getById(dto.getAssetId());
        if(asset==null){
            return Result.failed("该资产不存在");
        }
        asset.setAssetTag(dto.getAssetTag());
        return assetService.updateById(asset)?Result.ok("添加成功"):Result.failed("修改失败");
    }

    @PostMapping("/updateAssetManager")
    @ApiOperation("更换负责人")
    public Result<String> updateAssetManager(@Valid @RequestBody AssetUpdateDTO dto) {
        if(StringUtils.isBlank(dto.getAssetManager())){
            return Result.failed("请传入需要更换的负责人");
        }
        Asset asset = assetService.getById(dto.getAssetId());
        if(asset==null){
            return Result.failed("该资产不存在");
        }
        asset.setAssetManager(dto.getAssetManager());
        return assetService.updateById(asset)?Result.ok("更换成功"):Result.failed("更换失败");
    }

    @PostMapping("/updateGroupId")
    @ApiOperation("修改分组")
    public Result<String> updateGroupId(@Valid @RequestBody AssetUpdateDTO dto) {
        if(StringUtils.isBlank(dto.getAssetGroupId())){
            return Result.failed("请传入更改后的分组ID");
        }
        if(StringUtils.isBlank(dto.getAssetOrg())){
            return Result.failed("请传入组织部门ID");
        }
        Asset asset = assetService.getById(dto.getAssetId());
        if(asset==null){
            return Result.failed("该资产不存在");
        }
        asset.setAssetGroupId(dto.getAssetGroupId());
        asset.setAssetOrg(dto.getAssetOrg());
        return assetService.updateById(asset)?Result.ok("修改成功"):Result.failed("修改失败");
    }

    @PostMapping("/physical-import-template")
    @ApiOperation("物理资产导入模板")
    public void importPhysicalTemplate(HttpServletResponse response) throws IOException {
        Map<String, List<AssetTypeRespVO>> typeMap = assetTypeService.getTypeMap(new AssetTypeQueryDTO());
        // 所有类别
        List<String> assetTypeList = new ArrayList<String>();
        Map<String, List<String>> assetTypeMap = new HashMap<String, List<String>>();
        typeMap.forEach((m,n)->{
            assetTypeList.add(m);
            assetTypeMap.put(m,n.stream().map(AssetTypeRespVO::getTypeName).collect(Collectors.toList()));
        });

        // 部门-分组级联
        List<String> orgList = new ArrayList<String>();
        List<M4SsoOrg> m4OrgList = orgService.list();
        m4OrgList.forEach(e->orgList.add(e.getOrgName()));
        List<AssetGroupRespVO> list = assetGroupService.getList(new AssetGroupQueryDTO());
        Map<String, List<String>> groupMap = list.stream()
                .filter(vo -> !StringUtils.isBlank(vo.getAssetOrgName())) // 过滤掉 AssetOrgName 为空的项
                .collect(Collectors.groupingBy(
                        AssetGroupRespVO::getAssetOrgName,
                        Collectors.mapping(AssetGroupRespVO::getGroupName, Collectors.toList())
                ));

        //构建导入实例
        List<PhysicalAssetExcelVO> exampleList = new ArrayList<>();
        PhysicalAssetExcelVO physicalAssetExcelVO = PhysicalAssetExcelVO.builder()
                .assetName("物理资产示例")//资产名称
                .assetCategory("服务器")//资产类别
                .typeName("云平台虚拟机")//资产类型
                .ipAddress("192.168.0.104")//IP地址
                .assetBrand("Vmware")//品牌
                .assetModel("VC6.7")//型号
                .osVersion("CentOS 7.6")//操作系统版本
                .assetManagerName("管理员")//资产管理人
                .assetOrgName("资产管理部")//资产部门
                .assetGroupName("动态资产分组")//资产分组
                .assetTag("服务器资产")
                .build();
        exampleList.add(physicalAssetExcelVO);
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), PhysicalAssetExcelVO.class)
                .registerWriteHandler(new DropDownWriteHandler(assetTypeList,assetTypeMap,1))
                .registerWriteHandler(new DropDownWriteHandler(orgList,groupMap,13))
                .autoCloseStream(false) // 不要自动关闭，交给 Servlet 自己处理
                .sheet("物理资产").doWrite(exampleList);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("物理资产导入模板.xlsx", "UTF-8"));
        //response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    }

    @PostMapping("/logical-import-export")
    @ApiOperation("逻辑资产导入模板")
    public void importLogicalTemplate(HttpServletResponse response) throws IOException {
        //逻辑资产类型(从字典里取)
        List<DictItemRespVO> dictItemList = dictItemService.getDictItemList(new DictItemQueryDTO().setCode(DictType.LOGICAL_ASSET_TYPE.getType()));
        List<String> dictList = dictItemList.stream().map(DictItemRespVO::getText).collect(Collectors.toList());
        // 部门-分组级联
        List<String> orgList = new ArrayList<String>();
        List<M4SsoOrg> m4OrgList = orgService.list();
        m4OrgList.forEach(e->orgList.add(e.getOrgName()));
        List<AssetGroupRespVO> list = assetGroupService.getList(new AssetGroupQueryDTO());
        Map<String, List<String>> groupMap = list.stream()
                .filter(vo -> !StringUtils.isBlank(vo.getAssetOrgName())) // 过滤掉 AssetOrgName 为空的项
                .collect(Collectors.groupingBy(
                        AssetGroupRespVO::getAssetOrgName,
                        Collectors.mapping(AssetGroupRespVO::getGroupName, Collectors.toList())
                ));
        //构建导入示例
        List<LogicalAssetExcelVO> exampleList = new ArrayList<>();
        LogicalAssetExcelVO logicalAssetExcelVO = LogicalAssetExcelVO.builder()
                .assetName("逻辑资产示例")//资产名称
                .assetTypeName("网络应用")//资产类型
                .ipAddress("192.168.0.104")//IP地址
                .servicePort("8080")//端口号
                .assetManagerName("管理员")//资产管理人
                .assetOrgName("资产管理部")//资产部门
                .assetGroupName("动态资产分组")//资产分组
                .assetTag("网络应用资产")
                .build();
        exampleList.add(logicalAssetExcelVO);
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), LogicalAssetExcelVO.class)
                .registerWriteHandler(new DropDownWriteHandler(dictList,null,1))
                .registerWriteHandler(new DropDownWriteHandler(orgList,groupMap,6))
                .autoCloseStream(false) // 不要自动关闭，交给 Servlet 自己处理
                .sheet("逻辑资产").doWrite((exampleList));
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("逻辑资产导入模板.xlsx", "UTF-8"));
        //response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @PostMapping("/export-physical")
    @ApiOperation("导出物理资产")
    public void exportPhysical(HttpServletResponse response) throws IOException {
        List<AssetRespVO> assetList = assetService.getAssetList(new AssetQueryDTO().setAssetClass(AssetClass.PHYSICAL.getId()));
        // 输出
        ExcelUtils.write(response, "物理资产列表.xlsx", "物理资产"
                , PhysicalAssetExcelVO.class, AssetConvert.INSTANCE.convertList14(assetList));
    }

    @PostMapping("/export-logical")
    @ApiOperation("导出逻辑资产")
    public void exportLogical(HttpServletResponse response) throws IOException {
        List<AssetRespVO> assetList = assetService.getAssetList(new AssetQueryDTO().setAssetClass(AssetClass.LOGICAL.getId()));
        // 输出
        ExcelUtils.write(response, "逻辑资产列表.xlsx", "逻辑资产"
                , LogicalAssetExcelVO.class, AssetConvert.INSTANCE.convertList15(assetList));
    }

    @PostMapping("/import-physical")
    @ApiOperation("导入物理资产")
    @ApiImplicitParam(name = "file", value = "Excel 文件", required = true)
    public Result<ImportRespVO> importPhysicalExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<PhysicalAssetExcelVO> list = ExcelUtils.read(file, PhysicalAssetExcelVO.class);
        if (CollUtil.isEmpty(list)) {
            throw new ServiceException("导入的资产数据不能为空");
        }
        return Result.ok(assetService.importPhysicalExcel(list));
    }

    @PostMapping("/import-logical")
    @ApiOperation("导入逻辑资产")
    @ApiImplicitParam(name = "file", value = "Excel 文件", required = true)
    public Result<ImportRespVO> importLogicalExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<LogicalAssetExcelVO> list = ExcelUtils.read(file, LogicalAssetExcelVO.class);
        if (CollUtil.isEmpty(list)) {
            throw new ServiceException("导入的资产数据不能为空");
        }
        return Result.ok(assetService.importLogicalExcel(list));
    }

    @PostMapping("/homeOverview")
    @ApiOperation("首页总览")
    public Result<HomeOverviewVO> getHomeOverview(){
        List<String> dateList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        for (int i = 6; i >= 0 ; i--) {
            String date = currentDate.minusDays(i).format(formatter);
            dateList.add(date);
        }
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
        HomeOverviewVO homeOverview = new HomeOverviewVO()
                .setAssetTotalCount(138) // 资产总数
                .setAssetTotalScore(96) // 资产总评分
                .setSecurityDeviceTotalCount(15) // 安全设备全部数量
                .setSecurityDeviceAliveCount(14) // 安全设备存活数量
                .setItDeviceTotalCount(60) // IT设备全部数量
                .setItDeviceAliveCount(58) // IT设备存活数量
                .setNetworkDeviceTotalCount(30) // 网络设备全部数量
                .setNetworkDeviceAliveCount(28) // 网络设备存活数量
                .setLogicalAssetCount(10) // 逻辑资产数量
                .setSecurityVulnerabilityCount(30) // 安全漏洞数量
                .setComplianceVulnerabilityCount(25) // 合规漏洞数量
                .setCloudAssetRiskCount(52) // 云资产风险数量
                .setBaselineRiskCount(10) // 基线风险数量
                .setTotalRiskCount(117) // 风险总数
                .setNetworkDeviceEventsResolvedCount(2) // 网络设备事件已处理次数
                .setNetworkDeviceEventsPendingCount(1) // 网络设备事件未处理次数
                .setSecurityDeviceEventsResolvedCount(1) // 安全设备事件已处理次数
                .setSecurityDeviceEventsPendingCount(0) // 安全设备事件未处理次数
                .setItDeviceEventsResolvedCount(3) // IT设备事件已处理次数
                .setItDeviceEventsPendingCount(3) // IT设备事件未处理次数
                .setEndpointDeviceTotalCount(12) // 终端设备总数
                .setEndpointDeviceOnlineCount(12) // 终端设备在线数量
                .setDateList(dateList) // 近七天日期集合
                .setLowRiskCount(Arrays.asList(5, 6, 3, 8, 4, 4, 3)) // 近七天低风险集合
                .setMediumRiskCount(Arrays.asList(2, 3, 1, 4, 2, 2, 1)) // 近七天中风险集合
                .setHighRiskCount(Arrays.asList(1, 0, 2, 1, 3, 0, 1)) // 近七天高风险集合
                .setExportDeviceLoad(Arrays.asList(590, 640, 850, 730, 800, 840, 900)) // 近七天出口设备负荷(流量)
                .setSecurityDeviceAssetScore(Arrays.asList(85, 90, 92, 88, 95 ,95, 89))//近七天安全设备资产评分集合
                .setNetworkDeviceAssetScore(Arrays.asList(82, 85, 89, 86, 88 ,90 , 88))//近七天网络设备资产评分集合
                .setItDeviceAssetScore(Arrays.asList(80, 85, 90, 88, 86 ,86, 91))//近七天IT设备资产评分集合
                .setLogicalAssetScore(Arrays.asList(90, 88, 92, 85, 91, 92, 89))//近七天逻辑资产评分集合
                .setNonEndpointRiskAssetRanking(nonEndpointRiskAssetRanking)//非终端风险资产排行
                .setEndpointRiskAssetRanking(endpointRiskAssetRanking);//终端风险资产排行
        return Result.ok(homeOverview);
    }


}
