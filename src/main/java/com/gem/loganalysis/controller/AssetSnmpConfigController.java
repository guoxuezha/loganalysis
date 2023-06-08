package com.gem.loganalysis.controller;


import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigDeleteDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigUpdateDTO;
import com.gem.loganalysis.model.dto.edit.EventTypeInsertDTO;
import com.gem.loganalysis.model.dto.query.AssetSnmpConfigQueryDTO;
import com.gem.loganalysis.model.entity.EventType;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import com.gem.loganalysis.model.vo.asset.AssetSnmpConfigRespVO;
import com.gem.loganalysis.service.IAssetSnmpConfigService;
import com.gem.loganalysis.service.impl.AssetSnmpConfigServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * SNMP参数设置 前端控制器
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/sop/asset-snmp-config")
@Api(tags = "SNMP参数设置")
public class AssetSnmpConfigController {

    @Resource
    private IAssetSnmpConfigService snmpConfigService;

    @PostMapping("/insert")
    @ApiOperation("新增SNMP参数设置")
    public Result<String> insert(@RequestBody @Valid AssetSnmpConfigDTO dto) {
        int i = snmpConfigService.insertConfig(dto);
        return i > 0 ? Result.ok("新增成功!") : Result.failed("新增失败!");
    }

    @PostMapping("/delete")
    @ApiOperation("删除SNMP参数设置")
    public Result<String> insert(@RequestBody @Valid AssetSnmpConfigDeleteDTO dto) {
        int i = snmpConfigService.deleteConfig(dto);
        return i > 0 ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    @PostMapping("/update")
    @ApiOperation("更新SNMP参数设置")
    public Result<String> update(@RequestBody @Valid AssetSnmpConfigUpdateDTO dto) {
        int i = snmpConfigService.updateConfig(dto);
        return i > 0 ? Result.ok("更新成功!") : Result.failed("更新失败!");
    }

    @PostMapping("/get")
    @ApiOperation("获得单个SNMP参数设置详情")
    public Result<AssetSnmpConfigRespVO> get(@RequestBody @Valid AssetSnmpConfigDeleteDTO dto) {
        AssetSnmpConfigRespVO respVO = snmpConfigService.getConfig(dto);
        return respVO != null  ? Result.ok(respVO) : Result.failed("该设置不存在，请确认部门ID和IP地址正确");
    }

    @PostMapping("/list")
    @ApiOperation("获得SNMP参数设置列表")
    public Result<List<AssetSnmpConfigRespVO>> list(@RequestBody @Valid AssetSnmpConfigQueryDTO dto) {
        return Result.ok(snmpConfigService.getList(dto));
    }

    @PostMapping("/page")
    @ApiOperation("获得SNMP参数设置分页")
    public Result<PageResponse<AssetSnmpConfigRespVO>> page(@RequestBody PageRequest<AssetSnmpConfigQueryDTO> dto) {
        return Result.ok(snmpConfigService.getPage(dto));
    }


}
