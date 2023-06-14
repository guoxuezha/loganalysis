package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.bo.LogFormatter;
import com.gem.loganalysis.model.bo.LogNormalFormTree;
import com.gem.loganalysis.model.bo.MergeLog;
import com.gem.loganalysis.model.dto.edit.LogFieldMappingDTO;
import com.gem.loganalysis.model.dto.query.AnalysisRuleQueryDTO;
import com.gem.loganalysis.model.entity.LogAnalysisRuleRela;
import com.gem.loganalysis.model.vo.AssetAnalysisRuleVO;

/**
 * <p>
 * 日志解析规则映射关系 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-10
 */
public interface ILogAnalysisRuleRelaService extends IService<LogAnalysisRuleRela> {

    LogNormalFormTree showLogNormalForm(String ruleRelaId);

    LogFormatter.SimpleTreeNode buildSourceLogTree(String ruleRelaId);

    Boolean editLogFieldMapping(LogFieldMappingDTO dto);

    PageResponse<AssetAnalysisRuleVO> getAnalysisRules(PageRequest<AnalysisRuleQueryDTO> dto);

    PageResponse<MergeLog> getLogRecordsByAsset(PageRequest<String> dto);

    PageResponse<MergeLog> getLogRecordsByRuleRela(PageRequest<String> dto);
}
