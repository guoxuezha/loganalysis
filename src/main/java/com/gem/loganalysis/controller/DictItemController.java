package com.gem.loganalysis.controller;


import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.query.DictItemQueryDTO;
import com.gem.loganalysis.model.entity.DictItem;
import com.gem.loganalysis.model.vo.DictItemRespVO;
import com.gem.loganalysis.service.DictItemService;
import com.gem.loganalysis.service.DictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 字典数据控制层
 *
 * @author czw
 * @since 2023-02-27
 */
@Api(tags = "字典数据")
@RestController
@RequestMapping("/dictItem")
@AllArgsConstructor
public class DictItemController {

    private final DictItemService dictItemService;
    private final DictTypeService dictTypeService;

    @PostMapping("/create")
    @ApiOperation("创建字典数据")
    public Result<String> createDictItem(@RequestBody DictItem reqVO) {
        //校验字典数据的字典类型的有效以及VALUE的唯一性
        String s = dictItemService.checkCreateOrUpdate(reqVO);
        if (!"正确".equals(s)) {
            return Result.failed(s);
        }
        int i = dictItemService.createDictItem(reqVO);
        if (i > 0) {
            return Result.ok("创建成功");
        } else {
            return Result.failed("创建失败");
        }
    }

    @PostMapping("/update")
    @ApiOperation("修改字典数据")
    public Result<String> updateDictItem(@RequestBody DictItem reqVO) {
        if(reqVO.getId()==null){
            return Result.failed( "请传入要修改的字典数据ID");
        }
        //校验字典数据的字典类型的有效以及VALUE的唯一性
        String s = dictItemService.checkCreateOrUpdate(reqVO);
        if (!"正确".equals(s)) {
            return Result.failed(s);
        }
        int i = dictItemService.updateDictType(reqVO);
        if (i > 0) {
            return Result.ok("修改成功");
        } else {
            return Result.failed("修改失败");
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除字典数据")
    public Result<String> deleteDictItem(@Valid @RequestBody DeleteDTO dto) {
        //校验字典类型是否可删除
        String s = dictItemService.deleteCheck(dto.getId());
        if (!"正确".equals(s)) {
            return Result.failed(s);
        }
        int i = dictItemService.deleteDictItem(dto.getId());
        if (i > 0) {
            return Result.ok("删除字典数据成功");
        } else {
            return Result.failed("删除字典数据失败");
        }
    }


    @ApiOperation("/根据字典类型ID查询字典数据多级详细")
    @PostMapping(value = "/get")
    public Result<List<DictItemRespVO>> getDictItem(@Valid @RequestBody GetDTO dto) {
        //判断是否存在
        String s = dictTypeService.checkDictTypeExist(dto.getId());
        if (!"正确".equals(s)) {
            return Result.failed(s);
        }
        return Result.ok(dictItemService.getDictItem(dto.getId()));
    }

    @PostMapping("/list")
    @ApiOperation(value = "获得单层数据", notes = "条件查询单层数据，非树形结构")
    public Result<List<DictItem> > listDictItems(@Valid @RequestBody DictItemQueryDTO reqVO) {
        return Result.ok(dictItemService.getDictItemList(reqVO));
    }

    @PostMapping("/changeStatus")
    @ApiOperation(value = "开启/关闭")
    public Result<String> changeStatus(@Valid @RequestBody GetDTO dto) {
        return Result.ok(dictItemService.changeStatus(dto.getId()));
    }


    @ApiOperation("/获取开启状态的所有数据结构并排序,主要用于前端缓存")
    @PostMapping(value = "/getSimple")
    public Result<Object> getSimpleDictItem() {
        return Result.ok(dictItemService.getSimpleDictItem());
    }

}
