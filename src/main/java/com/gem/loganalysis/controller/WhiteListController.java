package com.gem.loganalysis.controller;


import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.BlackWhiteListDeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlackWhiteListDTO;
import com.gem.loganalysis.model.dto.query.BlackWhiteListQueryDTO;
import com.gem.loganalysis.model.vo.BlackWhiteListVO;
import com.gem.loganalysis.service.IBlackListService;
import com.gem.loganalysis.service.IWhiteListService;
import com.github.pagehelper.PageInfo;
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
 * 白名单 前端控制器
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@RestController
@RequestMapping("/sop/white-list")
@Api(tags = "白名单")
public class WhiteListController {

    @Resource
    private IWhiteListService whiteListService;

    @PostMapping("/create")
    @ApiOperation("创建白名单")
    public Result<String> createWhiteList(@Valid @RequestBody BlackWhiteListDTO dto) {
        return whiteListService.createWhiteList(dto)?Result.ok("新增成功"):Result.failed("新增失败");
    }

    @PostMapping("/update")
    @ApiOperation("更新白名单时间")
    public Result<String> updateWhiteList(@Valid @RequestBody BlackWhiteListDTO dto) {
        return whiteListService.updateWhiteList(dto)?Result.ok("更新成功"):Result.failed("更新失败");
    }

    @PostMapping("/delete")
    @ApiOperation("移出白名单")
    public Result<String> deleteWhiteList(@Valid @RequestBody BlackWhiteListDeleteDTO dto) {
        return whiteListService.deleteWhiteList(dto)?Result.ok("删除成功"):Result.failed("删除失败");
    }

    @PostMapping("/list")
    @ApiOperation("白名单列表")
    public Result<List<BlackWhiteListVO>> whiteList(@Valid @RequestBody BlackWhiteListQueryDTO dto) {
        return Result.ok(whiteListService.whiteList(dto));
    }

    @PostMapping("/page")
    @ApiOperation("白名单分页")
    public Result<PageInfo<BlackWhiteListVO>> whiteListPage(@RequestBody PageRequest<BlackWhiteListQueryDTO> dto) {
        return Result.ok(whiteListService.whiteListPage(dto));
    }





}
