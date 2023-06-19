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
import com.gem.loganalysis.model.vo.ScreeShowVO;
import com.gem.loganalysis.model.vo.asset.*;
import com.gem.loganalysis.service.*;
import com.gem.loganalysis.util.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gem.loganalysis.util.UserUtil.getAuthorityUserId;

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
        return assetService.saveBatch(AssetConvert.INSTANCE.convertList01(dto)) ?
                Result.ok("插入成功") : Result.failed("插入失败");
    }

    @PostMapping("/pageList")
    @ApiOperation("分页查询安全管理资产")
    public Result<PageResponse<AssetRespVO>> pageList(@RequestBody PageRequest<AssetQueryDTO> dto) throws JSONException{
        dto.getData().setAssetManagerId(getAuthorityUserId());//鉴权
        return Result.ok(assetService.getPageList(dto));
    }

    @PostMapping("/list")
    @ApiOperation("查询安全管理资产列表")
    public Result<List<AssetRespVO>> pageList(@RequestBody AssetQueryDTO dto) throws JSONException {
        dto.setAssetManagerId(getAuthorityUserId());//鉴权
        return Result.ok(assetService.getAssetList(dto));
    }

    @PostMapping("/get")
    @ApiOperation("根据ID获得单一资产信息")
    public Result<AssetRespVO> getAsset(@RequestBody GetDTO dto) {
        if (dto.getId() == null || dto.getId().trim().equals("")) {
            return Result.failed("请传入资产唯一编码ID");
        }
        return Result.ok(assetService.getAsset(dto.getId()));
    }

    @PostMapping("/getAccount")
    @ApiOperation("根据ID获得网管密码")
    public Result<AssetAccountRespVO> getAssetAccount(@RequestBody GetDTO dto) {
        if (dto.getId() == null || dto.getId().trim().equals("")) {
            return Result.failed("请传入资产唯一编码ID");
        }
        return Result.ok(assetService.getAssetAccount(dto.getId()));
    }

    @PostMapping("/delete")
    @ApiOperation("删除资产")
    public Result<String> deleteAsset(@Valid @RequestBody DeleteDTO dto) {
        if (StringUtils.isBlank(dto.getId())) {
            return Result.failed("请传入需要删除的资产ID");
        }
        return assetService.removeById(dto.getId()) ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    @PostMapping("/overview")
    @ApiOperation("资产总览")
    public Result<AssetOverviewVO> overview() throws JSONException {
        return Result.ok(assetService.getOverviewInfo());
    }

    @PostMapping("/addAssetTag")
    @ApiOperation("添加标签")
    public Result<String> addAssetTag(@Valid @RequestBody AssetUpdateDTO dto) {
        if (StringUtils.isBlank(dto.getAssetTag())) {
            return Result.failed("请传入需要添加的资产标签");
        }
        Asset asset = assetService.getById(dto.getAssetId());
        if (asset == null) {
            return Result.failed("该资产不存在");
        }
        asset.setAssetTag(dto.getAssetTag());
        return assetService.updateById(asset) ? Result.ok("添加成功") : Result.failed("修改失败");
    }

    @PostMapping("/updateAssetManager")
    @ApiOperation("更换负责人")
    public Result<String> updateAssetManager(@Valid @RequestBody AssetUpdateDTO dto) {
        if (StringUtils.isBlank(dto.getAssetManager())) {
            return Result.failed("请传入需要更换的负责人");
        }
        Asset asset = assetService.getById(dto.getAssetId());
        if (asset == null) {
            return Result.failed("该资产不存在");
        }
        asset.setAssetManager(dto.getAssetManager());
        return assetService.updateById(asset) ? Result.ok("更换成功") : Result.failed("更换失败");
    }

    @PostMapping("/updateGroupId")
    @ApiOperation("修改分组")
    public Result<String> updateGroupId(@Valid @RequestBody AssetUpdateDTO dto) {
        if (StringUtils.isBlank(dto.getAssetGroupId())) {
            return Result.failed("请传入更改后的分组ID");
        }
        if (StringUtils.isBlank(dto.getAssetOrg())) {
            return Result.failed("请传入组织部门ID");
        }
        Asset asset = assetService.getById(dto.getAssetId());
        if (asset == null) {
            return Result.failed("该资产不存在");
        }
        asset.setAssetGroupId(dto.getAssetGroupId());
        asset.setAssetOrg(dto.getAssetOrg());
        return assetService.updateById(asset) ? Result.ok("修改成功") : Result.failed("修改失败");
    }

    @PostMapping("/physical-import-template")
    @ApiOperation("物理资产导入模板")
    public void importPhysicalTemplate(HttpServletResponse response) throws IOException {
        Map<String, List<AssetTypeRespVO>> typeMap = assetTypeService.getTypeMap(new AssetTypeQueryDTO());
        // 所有类别
        List<String> assetTypeList = new ArrayList<String>();
        Map<String, List<String>> assetTypeMap = new HashMap<String, List<String>>();
        typeMap.forEach((m, n) -> {
            assetTypeList.add(m);
            assetTypeMap.put(m, n.stream().map(AssetTypeRespVO::getTypeName).collect(Collectors.toList()));
        });

        // 部门-分组级联
        List<String> orgList = new ArrayList<String>();
        List<M4SsoOrg> m4OrgList = orgService.list();
        m4OrgList.forEach(e -> orgList.add(e.getOrgName()));
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
                .registerWriteHandler(new DropDownWriteHandler(assetTypeList, assetTypeMap, 1))
                .registerWriteHandler(new DropDownWriteHandler(orgList, groupMap, 13))
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
        m4OrgList.forEach(e -> orgList.add(e.getOrgName()));
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
                .registerWriteHandler(new DropDownWriteHandler(dictList, null, 1))
                .registerWriteHandler(new DropDownWriteHandler(orgList, groupMap, 6))
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
    public void exportPhysical(HttpServletResponse response) throws IOException, JSONException {
        List<AssetRespVO> assetList = assetService.getAssetList(new AssetQueryDTO().setAssetClass(AssetClass.PHYSICAL.getId()));
        // 输出
        ExcelUtils.write(response, "物理资产列表.xlsx", "物理资产"
                , PhysicalAssetExcelVO.class, AssetConvert.INSTANCE.convertList14(assetList));
    }

    @PostMapping("/export-logical")
    @ApiOperation("导出逻辑资产")
    public void exportLogical(HttpServletResponse response) throws IOException, JSONException {
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
    public Result<HomeOverviewVO> getHomeOverview() throws JSONException {
        HomeOverviewVO homeOverview = assetService.getHomeOverview();
        return Result.ok(homeOverview);
    }

    @PostMapping("/screenShow")
    @ApiOperation("大屏展示")
    public Result<ScreeShowVO> screenShow(@RequestBody GetDTO dto) throws JSONException {
        return Result.ok(assetService.screenShow(dto.getId()));
    }

}
