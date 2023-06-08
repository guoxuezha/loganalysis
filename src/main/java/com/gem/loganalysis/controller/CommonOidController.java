package com.gem.loganalysis.controller;


import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigDeleteDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigUpdateDTO;
import com.gem.loganalysis.model.dto.edit.CommonOidBaseDTO;
import com.gem.loganalysis.model.dto.edit.CommonOidDTO;
import com.gem.loganalysis.model.dto.query.AssetSnmpConfigQueryDTO;
import com.gem.loganalysis.model.dto.query.CommonOidQueryDTO;
import com.gem.loganalysis.model.vo.asset.AssetSnmpConfigRespVO;
import com.gem.loganalysis.model.vo.asset.CommonOidRespVO;
import com.gem.loganalysis.service.ICommonOidService;
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
 * 通用MIB库OID列表设置 前端控制器
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/sop/common-oid")
@Api(tags = "SNMP扫描 - 通用MIB库OID列表设置")
public class CommonOidController {

    @Resource
    private ICommonOidService commonOidService;

    @PostMapping("/insert")
    @ApiOperation("新增通用MIB库OID")
    public Result<String> insert(@RequestBody @Valid CommonOidDTO dto) {
        int i = commonOidService.insertOID(dto);
        return i > 0 ? Result.ok("新增成功!") : Result.failed("新增失败!");
    }

    @PostMapping("/delete")
    @ApiOperation("删除MIB库OID")
    public Result<String> insert(@RequestBody @Valid CommonOidBaseDTO dto) {
        int i = commonOidService.deleteOID(dto);
        return i > 0 ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    @PostMapping("/update")
    @ApiOperation("更新MIB库OID")
    public Result<String> update(@RequestBody @Valid CommonOidDTO dto) {
        int i = commonOidService.updateOID(dto);
        return i > 0 ? Result.ok("更新成功!") : Result.failed("更新失败!");
    }

    @PostMapping("/get")
    @ApiOperation("获得单个MIB库OID详情")
    public Result<CommonOidRespVO> get(@RequestBody @Valid CommonOidBaseDTO dto) {
        CommonOidRespVO respVO = commonOidService.getOID(dto);
        return respVO != null  ? Result.ok(respVO) : Result.failed("该OID信息不存在，请确认MIB版本和OID正确");
    }

    @PostMapping("/list")
    @ApiOperation("获得MIB库OID列表")
    public Result<List<CommonOidRespVO>> list(@RequestBody @Valid CommonOidQueryDTO dto) {
        return Result.ok(commonOidService.getList(dto));
    }

    @PostMapping("/page")
    @ApiOperation("获得MIB库OID分页")
    public Result<PageResponse<CommonOidRespVO>> page(@RequestBody PageRequest<CommonOidQueryDTO> dto) {
        return Result.ok(commonOidService.getPage(dto));
    }

}
