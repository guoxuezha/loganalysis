package com.gem.loganalysis.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.mapper.LogAnalysisRuleRelaMapper;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.bo.LogNormalFormTree;
import com.gem.loganalysis.model.bo.MergeLog;
import com.gem.loganalysis.model.dto.edit.LogFieldMappingDTO;
import com.gem.loganalysis.model.dto.query.AnalysisRuleQueryDTO;
import com.gem.loganalysis.model.entity.LogAnalysisRuleRela;
import com.gem.loganalysis.model.vo.AssetAnalysisRuleVO;
import com.gem.loganalysis.model.vo.SopLogNormalFormNode;
import com.gem.loganalysis.service.ILogAnalysisRuleRelaService;
import com.gem.loganalysis.util.MapToBeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Resource
    private DAO dao;

    @Override
    public LogNormalFormTree showLogNormalForm(String ruleRelaId) {
        String querySql;
        if (StrUtil.isNotEmpty(ruleRelaId)) {
            querySql = "SELECT `FIELD_ID`, `FIELD_NAME`, `FIELD_DESC`, `PID`, `LEVEL`, `FIELD_TAG`, `SOURCE_FIELD_NAME` FROM `SOP_LOG_FIELD_MAPPING` A " +
                    "RIGHT JOIN `SOP_LOG_NORMAL_FORM` B ON A.TARGET_FIELD_ID = B.FIELD_ID " +
                    "WHERE RULE_RELA_ID = '" + ruleRelaId + "' ORDER BY LEVEL";
        } else {
            querySql = "SELECT `FIELD_ID`, `FIELD_NAME`, `FIELD_DESC`, `PID`, `LEVEL`, `FIELD_TAG` FROM `SOP_LOG_NORMAL_FORM` ORDER BY LEVEL";
        }
        ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql);
        if (CollUtil.isNotEmpty(dataSet)) {
            List<SopLogNormalFormNode> list = MapToBeanUtil.execute(dataSet, SopLogNormalFormNode.class);
            return new LogNormalFormTree(list);
        }
        return null;
    }

    @Override
    public Boolean editLogFieldMapping(LogFieldMappingDTO dto) {
        String ruleRelaId = dto.getRuleRelaId();
        // 先删除
        dao.execCommand(BaseConstant.DEFAULT_POOL_NAME, "DELETE FROM SOP_LOG_FIELD_MAPPING WHERE RULE_RELA_ID = '" + ruleRelaId + "'");
        // 再新增
        String insertSql = "INSERT INTO `SOP_LOG_FIELD_MAPPING` (`RULE_RELA_ID`, `SOURCE_FIELD_NAME`, `SOURCE_FIELD_DESC`, `TARGET_FIELD_ID`, `CREATE_TIME`) VALUES (?, ?, ?, ?, ?)";
        int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
        ArrayList<Object[]> records = new ArrayList<>();
        String now = DateUtil.now();
        for (LogFieldMappingDTO.FieldMapping mapping : dto.getFieldMappingList()) {
            records.add(new Object[]{ruleRelaId, mapping.getSourceFieldName(), mapping.getSourceFieldDesc(), mapping.getTargetFieldId(), now});
        }
        return dao.execBatch(BaseConstant.DEFAULT_POOL_NAME, insertSql, types, records, 100);
    }

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
