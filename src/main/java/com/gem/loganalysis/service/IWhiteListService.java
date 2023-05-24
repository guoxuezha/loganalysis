package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.BlackWhiteListDeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlackWhiteListDTO;
import com.gem.loganalysis.model.dto.query.BlackWhiteListQueryDTO;
import com.gem.loganalysis.model.entity.WhiteList;
import com.gem.loganalysis.model.vo.BlackWhiteListVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 白名单 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
public interface IWhiteListService extends IService<WhiteList> {


    boolean createWhiteList(BlackWhiteListDTO dto);

    boolean updateWhiteList(BlackWhiteListDTO dto);

    boolean deleteWhiteList(BlackWhiteListDeleteDTO dto);

    List<BlackWhiteListVO> whiteList(BlackWhiteListQueryDTO dto);

    PageInfo<BlackWhiteListVO> whiteListPage(PageRequest<BlackWhiteListQueryDTO> dto);
}
