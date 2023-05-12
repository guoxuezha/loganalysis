package com.gem.loganalysis.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.IpSectionDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.asset.LogicalAssetQueryDTO;
import com.gem.loganalysis.model.dto.asset.PhysicalAssetQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import com.gem.loganalysis.model.vo.asset.LogicalAssetScannerRespVO;
import com.gem.loganalysis.model.vo.asset.PhysicalAssetScannerRespVO;
import com.gem.loganalysis.scanner.Scanner;
import com.gem.loganalysis.service.IAssetService;
import com.gem.loganalysis.service.ILogAnalysisRuleRelaService;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import com.gem.loganalysis.service.IPhysicalAssetTempService;
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
import java.util.regex.Pattern;

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

    @PostMapping("/scannerPort")
    @ApiOperation("资产端口扫描，需提供某个资产的编码ID")
    public Result<String> scannerPort(@Valid @RequestBody GetDTO dto) {
        Asset byId = assetService.getById(dto.getId());
        if(byId==null){
            return Result.failed("该资产不存在");
        }
        Scanner.start(byId.getIpAddress(),"1-65535");
        return Result.ok("扫描成功");
    }

    @PostMapping("/scannerIpSection")
    @ApiOperation("IP区段扫描")
    public Result<String> scannerIpSection(@Valid @RequestBody IpSectionDTO ipSection) {
        String regex = "^(?=(\\b|\\D))(((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))(?=(\\b|\\D))/([1-2][0-9]|3[0-2]|[1-9])$";
        if(!Pattern.matches(regex,ipSection.getIpSection())){
            //效验IP地址格式是否正确
            return Result.failed("请输入正确的网段格式，例如192.168.1.0/24");
        }
        String s = StringUtils.substringAfterLast(ipSection.getIpSection(), ".");
        String before = StringUtils.substringBeforeLast(s, "/");
        String after = StringUtils.substringAfterLast(s, "/");
        if(Integer.parseInt(before)>=Integer.parseInt(after)){
            //效验IP地址格式是否正确
            return Result.failed("请输入正确的网段格式，例如192.168.1.0/24");
        }
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


}
