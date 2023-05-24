package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.BlackWhiteListDeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlackWhiteListDTO;
import com.gem.loganalysis.model.dto.query.BlackWhiteListQueryDTO;
import com.gem.loganalysis.model.entity.BlackList;
import com.gem.loganalysis.model.vo.BlackWhiteListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 黑名单 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Mapper
public interface BlackListMapper extends BaseMapper<BlackList> {

    /**
     * 移出黑名单
     * @param dto
     * @return
     */
    boolean deleteBlackList(BlackWhiteListDeleteDTO dto);

    /**
     * 修改时间
     * @param dto
     * @return
     */
    boolean updateBlackList(BlackWhiteListDTO dto);

    /**
     * 黑名单列表
     * @param dto
     * @return
     */
    List<BlackWhiteListVO> blackList(BlackWhiteListQueryDTO dto);
}
