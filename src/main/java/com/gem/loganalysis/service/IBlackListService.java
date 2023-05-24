package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.BlackWhiteListDeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlackWhiteListDTO;
import com.gem.loganalysis.model.dto.query.BlackWhiteListQueryDTO;
import com.gem.loganalysis.model.entity.BlackList;
import com.gem.loganalysis.model.vo.BlackWhiteListVO;

import java.util.List;

/**
 * <p>
 * 黑名单 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
public interface IBlackListService extends IService<BlackList> {
    /**
     * 创建黑名单
     * @param dto
     * @return
     */
    Result<String> createBlackList(BlackWhiteListDTO dto);

    /**
     * 更新黑名单
     * @param dto
     * @return
     */
    Result<String> updateBlackList(BlackWhiteListDTO dto);

    /**
     * 移出黑名单
     * @param dto
     * @return
     */
    Result<String> deleteBlackList(BlackWhiteListDeleteDTO dto);

    /**
     * 黑名单列表
     * @return
     */
    List<BlackWhiteListVO> blackList(BlackWhiteListQueryDTO dto);
}
