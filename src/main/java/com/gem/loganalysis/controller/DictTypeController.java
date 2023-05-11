package com.gem.loganalysis.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.convert.DictConvert;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.edit.DictTypeDTO;
import com.gem.loganalysis.model.dto.query.DictTypeQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.DictData;
import com.gem.loganalysis.model.entity.DictType;
import com.gem.loganalysis.model.vo.DictTypeRespVO;
import com.gem.loganalysis.service.IDictDataService;
import com.gem.loganalysis.service.IDictTypeService;
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
 * 字典类型表 前端控制器
 * </p>
 *
 * @author czw
 * @since 2023-05-10
 */
@RestController
@RequestMapping("/system/dict-type")
@Api(tags = "系统配置 - 数据字典类型")
public class DictTypeController {

    @Resource
    private IDictTypeService dictTypeService;
    @Resource
    private IDictDataService dictDataService;

    @PostMapping("/create")
    @ApiOperation("创建数据字典类型")
    public Result<String> createDictType(@Valid @RequestBody DictTypeDTO dto) {
        if(dto.getTypeId()!=null){
            Result.failed("请勿传入字典类型主键");
        }
        //效验名称是否存在
        DictType name = dictTypeService.getOne(new LambdaQueryWrapperX<DictType>().eq(DictType::getName, dto.getName()));
        if(name!=null){
            Result.failed("该字典类型名称已存在");
        }
        //效验字典类型是否存在
        DictType type = dictTypeService.getOne(new LambdaQueryWrapperX<DictType>().eq(DictType::getType, dto.getType()));
        if(type!=null){
            Result.failed("该字典类型已存在");
        }
        //效验类型是否存在
        return dictTypeService.save(DictConvert.INSTANCE.convert(dto))?Result.ok("操作成功!"):Result.failed("操作失败!");
    }

    @PostMapping("/update")
    @ApiOperation("修改数据字典类型")
    public Result<String> updateDictType(@Valid @RequestBody DictTypeDTO dto) {
        if(dto.getTypeId()==null){
            Result.failed("字典类型编码ID不能为空");
        }
        //效验原字典类型是否存在
        DictType byId = dictTypeService.getById(dto.getTypeId());
        if(byId==null){
            Result.failed("该字典类型不存在");
        }
        //效验名称是否存在
        DictType name = dictTypeService.getOne(new LambdaQueryWrapperX<DictType>().eq(DictType::getName, dto.getName()));
        if(name!=null&&!name.getTypeId().equals(dto.getTypeId())){
            Result.failed("该字典类型名称已存在");
        }
        //效验字典类型是否存在
        DictType type = dictTypeService.getOne(new LambdaQueryWrapperX<DictType>().eq(DictType::getType, dto.getType()));
        if(type!=null&&!type.getTypeId().equals(dto.getTypeId())){
            Result.failed("该字典类型已存在");
        }
        return dictTypeService.updateById(DictConvert.INSTANCE.convert(dto))?Result.ok("操作成功!"):Result.failed("操作失败!");
    }

    @PostMapping("/deleted")
    @ApiOperation("删除数据字典类型")
    public Result<String> deleteDictType(@Valid @RequestBody Integer typeId) {
        if(typeId==null){
            Result.failed("字典类型编码ID不能为空");
        }
        //效验原字典类型是否存在
        DictType byId = dictTypeService.getById(typeId);
        if(byId==null){
            Result.failed("改字典类型不存在");
        }
        //判断底下是否还存在数据data
        if(dictDataService.count(new LambdaQueryWrapperX<DictData>().eq(DictData::getDictType,byId.getType()))>0){
            Result.failed("请先删除该字典类型下的数据");
        };
        return dictTypeService.removeById(typeId)?Result.ok("操作成功!"):Result.failed("操作失败!");
    }

    @PostMapping("/pageList")
    @ApiOperation("字典类型分页列表")
    public Result<Page<DictTypeRespVO>> getDictTypePage(@Valid @RequestBody PageRequest<DictTypeQueryDTO> dto) {
        Page<DictType> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        DictTypeQueryDTO data = dto.getData();
        LambdaQueryWrapperX<DictType> wrapper = new LambdaQueryWrapperX<DictType>()
                .likeIfPresent(DictType::getType, data.getType())
                .likeIfPresent(DictType::getName, data.getName())
                .eqIfPresent(DictType::getStatus, data.getStatus())
                .orderByAsc(DictType::getTypeId);
        return Result.ok(DictConvert.INSTANCE.convertPage(dictTypeService.page(page, wrapper)));
    }

    @PostMapping("/list")
    @ApiOperation("字典类型列表(只展示开放的)")
    public Result<List<DictTypeRespVO>> getDictTypePage(@Valid @RequestBody DictTypeQueryDTO dto) {
        dto.setStatus(0);
        LambdaQueryWrapperX<DictType> wrapper = new LambdaQueryWrapperX<DictType>()
                .likeIfPresent(DictType::getType, dto.getType())
                .likeIfPresent(DictType::getName, dto.getName())
                .eqIfPresent(DictType::getStatus, dto.getStatus())
                .orderByAsc(DictType::getTypeId);
        return Result.ok(DictConvert.INSTANCE.convertList(dictTypeService.list(wrapper)));
    }

    @PostMapping("/get")
    @ApiOperation("获得某一数据字典类型详细")
    public Result<DictTypeRespVO> createDictType(@Valid @RequestBody Integer typeId) {
        if(typeId==null){
            Result.failed("字典类型编码ID不能为空");
        }
        return Result.ok(DictConvert.INSTANCE.convert(dictTypeService.getById(typeId)));
    }

}
