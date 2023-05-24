package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.BlackWhiteListDeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlackWhiteListDTO;
import com.gem.loganalysis.model.dto.query.BlackWhiteListQueryDTO;
import com.gem.loganalysis.model.entity.WhiteList;
import com.gem.loganalysis.model.vo.BlackWhiteListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 白名单 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Mapper
public interface WhiteListMapper extends BaseMapper<WhiteList> {


    boolean updateWhiteList(BlackWhiteListDTO dto);

    boolean deleteWhiteList(BlackWhiteListDeleteDTO dto);

    List<BlackWhiteListVO> whiteList(BlackWhiteListQueryDTO dto);
}
