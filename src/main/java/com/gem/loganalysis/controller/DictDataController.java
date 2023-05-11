package com.gem.loganalysis.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.convert.DictConvert;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.edit.DictDataDTO;
import com.gem.loganalysis.model.dto.edit.DictTypeDTO;
import com.gem.loganalysis.model.dto.query.DictDataQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.DictData;
import com.gem.loganalysis.model.entity.DictType;
import com.gem.loganalysis.model.vo.DictDataRespVO;
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
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author czw
 * @since 2023-05-10
 */
@RestController
@RequestMapping("/system/dict-data")
@Api(tags = "系统配置 - 数据字典数据")
public class DictDataController {
    @Resource
    private IDictDataService dictDataService;
    @Resource
    private IDictTypeService dictTypeService;

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<DictData> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(DictData::getDictType)
            .thenComparingInt(DictData::getSort);

    @PostMapping("/create")
    @ApiOperation("创建数据字典数据")
    public Result<String> createDictData(@Valid @RequestBody DictDataDTO dto) {
        if(dto.getDataId()!=null){
            return Result.failed("请勿传入字典数据主键");
        }
        //效验类型存在
        if(dictTypeService.count(new LambdaQueryWrapperX<DictType>().eq(DictType::getType,dto.getDictType()))==0){
            return Result.failed("该字类型不存在");
        }
        //效验值唯一
        DictData one = dictDataService.getOne(new LambdaQueryWrapperX<DictData>().eq(DictData::getValue, dto.getValue()));
        if(one!=null){
            return  Result.failed("该字典键值已存在");
        }
        return dictDataService.save(DictConvert.INSTANCE.convert(dto))?Result.ok("操作成功!"):Result.failed("操作失败!");
    }

    @PostMapping("/update")
    @ApiOperation("修改数据字典数据")
    public Result<String> updateDictType(@Valid @RequestBody DictDataDTO dto) {
        if(dto.getDataId()==null){
            return Result.failed("字典数据编码ID不能为空");
        }
        //判断原数据字典是否存在
        DictData byId = dictDataService.getById(dto.getDataId());
        if(byId==null){
            return  Result.failed("字典数据不存在");
        }
        //效验类型存在
        if(dictTypeService.count(new LambdaQueryWrapperX<DictType>().eq(DictType::getType,dto.getDictType()))==0){
            return  Result.failed("该字类型不存在");
        }
        //效验值唯一
        DictData one = dictDataService.getOne(new LambdaQueryWrapperX<DictData>().eq(DictData::getValue, dto.getValue()));
        if(one!=null&&!byId.getDataId().equals(dto.getDataId())){
            return Result.failed("该字典键值已存在");
        }
        return dictDataService.updateById(DictConvert.INSTANCE.convert(dto))?Result.ok("操作成功!"):Result.failed("操作失败!");
    }

    @PostMapping("/deleted")
    @ApiOperation("删除数据字典数据")
    public Result<String> deleteDictData(@Valid @RequestBody Integer dataId) {
        if(dataId==null){
            return  Result.failed("字典数据编码ID不能为空");
        }
        //判断原数据字典是否存在
        DictData byId = dictDataService.getById(dataId);
        if(byId==null){
            return Result.failed("字典数据不存在");
        }
        return dictDataService.removeById(dataId)?Result.ok("操作成功!"):Result.failed("操作失败!");
    }

    @PostMapping("/pageList")
    @ApiOperation("字典数据分页列表")
    public Result<Page<DictDataRespVO>> getDictTypePage(@Valid @RequestBody PageRequest<DictDataQueryDTO> dto) {
        Page<DictData> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        DictDataQueryDTO data = dto.getData();
        LambdaQueryWrapperX<DictData> wrapper = new LambdaQueryWrapperX<DictData>()
                .eqIfPresent(DictData::getDictType, data.getDictType())
                .eqIfPresent(DictData::getStatus, data.getStatus());
        Page<DictData> pageResp = dictDataService.page(page, wrapper);
        pageResp.getRecords().sort(COMPARATOR_TYPE_AND_SORT);
        return Result.ok(DictConvert.INSTANCE.convertPage01(pageResp));
    }

    @PostMapping("/list")
    @ApiOperation("字典数据列表(只展示开放的)")
    public Result<List<DictDataRespVO>> getDictTypePage(@Valid @RequestBody DictDataQueryDTO dto) {
        dto.setStatus(0);
        LambdaQueryWrapperX<DictData> wrapper = new LambdaQueryWrapperX<DictData>()
                .eqIfPresent(DictData::getDictType, dto.getDictType())
                .eqIfPresent(DictData::getStatus, dto.getStatus());
        List<DictData> list = dictDataService.list(wrapper);
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return Result.ok(DictConvert.INSTANCE.convertList01(list));
    }

    @PostMapping("/get")
    @ApiOperation("获得某一数据字典数据详细")
    public Result<DictDataRespVO> getDictData(@Valid @RequestBody Integer dataId) {
        if(dataId==null){
            return  Result.failed("请勿传入字典数据主键");
        }
        return Result.ok(DictConvert.INSTANCE.convert(dictDataService.getById(dataId)));
    }


}
