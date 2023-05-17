package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.LogAnalysisRuleRelaMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.bo.MergeLog;
import com.gem.loganalysis.model.dto.query.AnalysisRuleQueryDTO;
import com.gem.loganalysis.model.entity.LogAnalysisRuleRela;
import com.gem.loganalysis.model.vo.AssetAnalysisRuleVO;
import com.gem.loganalysis.service.ILogAnalysisRuleRelaService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 日志解析规则映射关系 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Service
public class LogAnalysisRuleRelaServiceImpl extends ServiceImpl<LogAnalysisRuleRelaMapper, LogAnalysisRuleRela> implements ILogAnalysisRuleRelaService {

    @Resource
    private LogAnalysisRuleRelaMapper logAnalysisRuleRelaMapper;

    @Override
    public PageResponse<AssetAnalysisRuleVO> getAnalysisRules(PageRequest<AnalysisRuleQueryDTO> dto) {
        Page<AssetAnalysisRuleVO> result = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        logAnalysisRuleRelaMapper.getAnalysisRules(dto.getData());
        return new PageResponse<>(result);
    }

    @Override
    public PageResponse<MergeLog> getLogRecordsByAsset(PageRequest<String> dto) {
        Page<MergeLog> result = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        logAnalysisRuleRelaMapper.getLogRecordsByAsset(dto.getData());
        return new PageResponse<>(result);
    }

    @Override
    public PageResponse<MergeLog> getLogRecordsByRuleRela(PageRequest<String> dto) {
        Page<MergeLog> result = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        logAnalysisRuleRelaMapper.getLogRecordsByRuleRela(dto.getData());
        return new PageResponse<>(result);
    }
}
