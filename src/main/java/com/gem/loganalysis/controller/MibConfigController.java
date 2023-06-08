package com.gem.loganalysis.controller;


import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.edit.CommonOidBaseDTO;
import com.gem.loganalysis.model.dto.edit.CommonOidDTO;
import com.gem.loganalysis.model.dto.edit.MibConfigBaseDTO;
import com.gem.loganalysis.model.dto.edit.MibConfigDTO;
import com.gem.loganalysis.model.dto.query.CommonOidQueryDTO;
import com.gem.loganalysis.model.dto.query.MibConfigQueryDTO;
import com.gem.loganalysis.model.vo.asset.CommonOidRespVO;
import com.gem.loganalysis.model.vo.asset.MibConfigRespVO;
import com.gem.loganalysis.service.IMibConfigService;
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
 * MIB指令集设置 前端控制器
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/sop/mib-config")
@Api(tags = "SNMP扫描 - MIB指令集设置")
public class MibConfigController {

    @Resource
    private IMibConfigService mibConfigService;

    @PostMapping("/insert")
    @ApiOperation("新增MIB指令集设置")
    public Result<String> insert(@RequestBody @Valid MibConfigDTO dto) {
        int i = mibConfigService.insertConfig(dto);
        return i > 0 ? Result.ok("新增成功!") : Result.failed("新增失败!");
    }

    @PostMapping("/delete")
    @ApiOperation("删除MIB指令集设置")
    public Result<String> insert(@RequestBody @Valid MibConfigBaseDTO dto) {
        int i = mibConfigService.deleteConfig(dto);
        return i > 0 ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    @PostMapping("/update")
    @ApiOperation("更新MIB指令集设置")
    public Result<String> update(@RequestBody @Valid MibConfigDTO dto) {
        int i = mibConfigService.updateConfig(dto);
        return i > 0 ? Result.ok("更新成功!") : Result.failed("更新失败!");
    }

    @PostMapping("/get")
    @ApiOperation("获得单MIB指令集设置详情")
    public Result<MibConfigRespVO> get(@RequestBody @Valid MibConfigBaseDTO dto) {
        MibConfigRespVO respVO = mibConfigService.getConfig(dto);
        return respVO != null  ? Result.ok(respVO) : Result.failed("该MIB指令不存在，请确认MIB版本和OID正确");
    }

    @PostMapping("/list")
    @ApiOperation("获得MIB指令集设置列表")
    public Result<List<MibConfigRespVO>> list(@RequestBody @Valid MibConfigQueryDTO dto) {
        return Result.ok(mibConfigService.getList(dto));
    }

    @PostMapping("/page")
    @ApiOperation("获得MIB指令集设置分页")
    public Result<PageResponse<MibConfigRespVO>> page(@RequestBody PageRequest<MibConfigQueryDTO> dto) {
        return Result.ok(mibConfigService.getPage(dto));
    }







}
