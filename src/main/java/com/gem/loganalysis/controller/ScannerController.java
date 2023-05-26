package com.gem.loganalysis.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.IpDTO;
import com.gem.loganalysis.model.dto.IpSectionDTO;
import com.gem.loganalysis.model.dto.asset.*;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.OrgVlan;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import com.gem.loganalysis.model.vo.asset.LogicalAssetScannerRespVO;
import com.gem.loganalysis.model.vo.asset.OrgVlanRespVO;
import com.gem.loganalysis.model.vo.asset.PhysicalAssetScannerRespVO;
import com.gem.loganalysis.scanner.IpScanner;
import com.gem.loganalysis.scanner.Scanner;
import com.gem.loganalysis.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.gem.loganalysis.util.UserUtil.getLoginUserOrgId;

@Api(tags = "资产模块 - 资产扫描")
@RestController
@RequestMapping("/sop/scanner")
@Validated
public class ScannerController {

    @Resource
    private IAssetService assetService;

    @Resource
    private ILogicalAssetTempService logicalAssetTempService;

    @Resource
    private IPhysicalAssetTempService physicalAssetTempService;

    @Resource
    private IOrgVlanService orgVlanService;

    @PostMapping("/scannerPort")
    @ApiOperation("资产端口扫描，需提供某个资产的编码ID")
    public Result<String> scannerPort(@Valid @RequestBody GetDTO dto) {
        Asset byId = assetService.getById(dto.getId());
        if(byId==null){
            return Result.failed("该资产不存在");
        }
        //TODO 改成异步 先返回扫描成功再开启扫描
        Scanner.start(byId.getIpAddress(),"1-65535",DateUtil.format(new Date(),"yyyyMMddHHmmss"));
        return Result.ok("扫描成功");
    }

    @PostMapping("/scannerIpPort")
    @ApiOperation("IP端口扫描，需提供单个IP")
    public Result<String> scannerPort(@Valid @RequestBody IpDTO dto) {
        String regex = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
        if(!Pattern.matches(regex,dto.getIp())){
            //效验IP地址格式是否正确
            return Result.failed("请输入正确的IP格式");
        }
        //TODO 改成异步 先返回扫描成功再开启扫描
        Scanner.start(dto.getIp(),"1-65535",DateUtil.format(new Date(),"yyyyMMddHHmmss"));
        return Result.ok("扫描成功");
    }

    @PostMapping("/scannerIpSection")
    @ApiOperation("IP区段扫描")
    public Result<String> scannerIpSection(@Valid @RequestBody IpSectionDTO dto) {
        String regex = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
        List<VlanDTO> vlanList = dto.getVlanList();
        if(vlanList.size()==0){
            return Result.failed("IP区段不能为空");
        }
        for (VlanDTO vlanDTO : vlanList) {
            if(!Pattern.matches(regex,vlanDTO.getBeginIp())){
                //效验IP地址格式是否正确
                return Result.failed("请输入正确的IP格式");
            }
            if(!Pattern.matches(regex,vlanDTO.getEndIp())){
                //效验IP地址格式是否正确
                return Result.failed("请输入正确的IP格式");
            }
        }
        //TODO 改成异步 先返回扫描成功再开启扫描
        IpScanner.scannerIpSection(dto.getVlanList(),DateUtil.format(new Date(),"yyyyMMddHHmmss"),getLoginUserOrgId());
        return Result.ok("扫描成功");
    }

    @PostMapping("/logicalAssetPage")
    @ApiOperation("逻辑资产扫描结果分页")
    public Result<Page<LogicalAssetScannerRespVO>> getLogicalAssetPage(@RequestBody PageRequest<LogicalAssetQueryDTO> dto) {
        return Result.ok(AssetConvert.INSTANCE.convertPage02(logicalAssetTempService.getLogicalAssetPage(dto)));
    }

    @PostMapping("/physicalAssetPage")
    @ApiOperation("IP物理资产扫描结果分页")
    public Result<Page<PhysicalAssetScannerRespVO>> getPhysicalAssetPage(@RequestBody PageRequest<PhysicalAssetQueryDTO> dto) {
        return Result.ok(AssetConvert.INSTANCE.convertPage03(physicalAssetTempService.getPhysicalAssetPage(dto)));
    }

    @PostMapping("/logicalAssetList")
    @ApiOperation("逻辑资产扫描结果列表")
    public Result<List<LogicalAssetScannerRespVO>> getLogicalAssetList(@RequestBody LogicalAssetQueryDTO dto) {
        return Result.ok(AssetConvert.INSTANCE.convertList02(logicalAssetTempService.getLogicalAssetList(dto)));
    }

    @PostMapping("/physicalAssetList")
    @ApiOperation("IP物理资产扫描结果列表")
    public Result<List<PhysicalAssetScannerRespVO>> getPhysicalAssetList(@RequestBody PhysicalAssetQueryDTO dto) {
        return Result.ok(AssetConvert.INSTANCE.convertList03(physicalAssetTempService.getPhysicalAssetList(dto)));
    }


    @PostMapping("/vlanPage")
    @ApiOperation("VLAN设置界面分页")
    public Result<Page<OrgVlanRespVO>> getVlanPage(@Valid @RequestBody PageRequest<OrgVlanQueryDTO> dto) {
        return Result.ok(orgVlanService.getVlanPage(dto));
    }

    @PostMapping("/vlanList")
    @ApiOperation("VLAN设置界面列表")
    public Result<List<OrgVlanRespVO>> getVlanList(@Valid @RequestBody OrgVlanQueryDTO dto) {
        return Result.ok(orgVlanService.getVlanList(dto));
    }

    @PostMapping("/editVlan")
    @ApiOperation("新增编辑VLAN设置")
    public Result<String> editVlan(@Valid @RequestBody OrgVlanDTO dto) {
        String regex = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
        List<VlanDTO> vlanList = dto.getVlanList();
        if(vlanList.size()==0){
            return Result.failed("IP区段不能为空");
        }
        for (VlanDTO vlanDTO : vlanList) {
            if(!Pattern.matches(regex,vlanDTO.getBeginIp())){
                //效验IP地址格式是否正确
                return Result.failed("请输入正确的IP格式");
            }
            if(!Pattern.matches(regex,vlanDTO.getEndIp())){
                //效验IP地址格式是否正确
                return Result.failed("请输入正确的IP格式");
            }
        }
        return orgVlanService.editVlan(dto)?Result.ok("操作成功"):Result.failed("操作失败");
    }



}
