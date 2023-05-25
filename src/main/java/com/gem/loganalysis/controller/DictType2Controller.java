package com.gem.loganalysis.controller;

import cn.hutool.core.lang.tree.TreeNodeConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.query.DictTypeQueryPageDTO;
import com.gem.loganalysis.model.entity.DictType2;
import com.gem.loganalysis.service.DictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 字典类型控制层
 *
 * @author czw
 * @since 2023-02-27
 */
@Api(tags = "字典类型")
@RestController
@RequestMapping("/sop/dictType")
@AllArgsConstructor
public class DictType2Controller {

    private final DictTypeService dictTypeService;


    @PostMapping("/create")
    @ApiOperation("创建字典类型")
    public Result<Object> createDictType(@RequestBody DictType2 reqVO) {
        //校验字典类型称是否唯一
        String s = dictTypeService.checkDictTypeUnique(reqVO);
        if (!"正确".equals(s)) {
            return Result.failed(s);
        }
        int i = dictTypeService.createDictType(reqVO);
        if (i > 0) {
            return Result.ok("创建成功");
        } else {
            return Result.failed("创建失败");
        }
    }

    @PostMapping("/update")
    @ApiOperation("修改字典类型")
    public Result<String> updateDictType(@RequestBody DictType2 reqVO) {
        if(reqVO.getId()==null){
            return Result.failed("请传入要修改的字典类型ID");
        }
        //校验字典类型称是否唯一
        String s = dictTypeService.checkDictTypeUnique(reqVO);
        if (!"正确".equals(s)) {
            return Result.failed(s);
        }
        int i = dictTypeService.updateDictType(reqVO);
        if (i > 0) {
            return Result.ok("修改成功");
        } else {
            return Result.failed("修改失败");
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除字典类型")
    public Result<String> deleteDictType(@Valid @RequestBody DeleteDTO dto) {
        //校验字典类型是否可删除
        String s = dictTypeService.deleteCheck(dto.getId());
        if (!"正确".equals(s)) {
            return Result.failed(s);
        }
        int i = dictTypeService.deleteDictType(dto.getId());
        if (i > 0) {
            return Result.ok("删除字典类型成功");
        } else {
            return Result.failed("删除字典类型失败");
        }
    }


    @ApiOperation("/获得字典类型的分页列表")
    @PostMapping("/page")
    public Result<IPage<DictType2>> pageDictTypes(@Valid @RequestBody DictTypeQueryPageDTO reqVO) {
        IPage<DictType2> list = dictTypeService.pageDictTypes(reqVO);
        return Result.ok(list);
    }

    @ApiOperation("/查询字典类型详细")
    @PostMapping(value = "/get")
    public Result<DictType2> getDictType(@Valid @RequestBody GetDTO dto) {
        //判断是否存在
        //校验字典类型称是否唯一
        String s = dictTypeService.checkDictTypeExist(dto.getId());
        if (!"正确".equals(s)) {
            return Result.failed(s);
        }
        return  Result.ok(dictTypeService.getDictType(dto.getId()));
    }

    @PostMapping("/list-all")
    @ApiOperation(value = "获得全部字典类型列表", notes = "包括开启 + 禁用的字典类型，主要用于前端的下拉选项")
    public Result<List<DictType2>> listDictTypes() {
        return Result.ok(dictTypeService.getDictTypeList());
    }

    @PostMapping("/changeStatus")
    @ApiOperation(value = "开启/关闭")
    public Result<String> changeStatus(@Valid @RequestBody GetDTO dto) {
        return Result.ok(dictTypeService.changeStatus(dto.getId()));
    }

}
