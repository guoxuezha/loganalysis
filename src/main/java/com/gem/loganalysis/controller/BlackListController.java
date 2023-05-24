package com.gem.loganalysis.controller;


import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.BlackWhiteListDeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlackWhiteListDTO;
import com.gem.loganalysis.model.dto.query.BlackWhiteListQueryDTO;
import com.gem.loganalysis.model.vo.BlackWhiteListVO;
import com.gem.loganalysis.service.IBlackListService;
import com.github.pagehelper.Page;
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
 * 黑名单 前端控制器
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@RestController
@RequestMapping("/sop/black-list")
@Api(tags = "黑名单")
public class BlackListController {

    @Resource
    private IBlackListService blackListService;

    @PostMapping("/create")
    @ApiOperation("创建黑名单")
    public Result<String> createBlackList(@Valid @RequestBody BlackWhiteListDTO dto) {
        return blackListService.createBlackList(dto)?Result.ok("新增成功"):Result.failed("新增失败");
    }

    @PostMapping("/update")
    @ApiOperation("更新黑名单时间")
    public Result<String> updateBlackList(@Valid @RequestBody BlackWhiteListDTO dto) {
        return blackListService.updateBlackList(dto)?Result.ok("更新成功"):Result.failed("更新失败");
    }

    @PostMapping("/delete")
    @ApiOperation("移出黑名单")
    public Result<String> deleteBlackList(@Valid @RequestBody BlackWhiteListDeleteDTO dto) {
        return blackListService.deleteBlackList(dto)?Result.ok("删除成功"):Result.failed("删除失败");
    }

    @PostMapping("/list")
    @ApiOperation("黑名单列表")
    public Result<List<BlackWhiteListVO>> blackList(@Valid @RequestBody BlackWhiteListQueryDTO dto) {
        return Result.ok(blackListService.blackList(dto));
    }

    @PostMapping("/page")
    @ApiOperation("黑名单分页")
    public Result<PageInfo<BlackWhiteListVO>> blackListPage(@RequestBody PageRequest<BlackWhiteListQueryDTO> dto) {
        return Result.ok(blackListService.blackListPage(dto));
    }


}
