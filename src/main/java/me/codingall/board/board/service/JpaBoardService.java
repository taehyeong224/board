package me.codingall.board.board.service;

import me.codingall.board.board.entity.BoardEntity;
import me.codingall.board.board.entity.BoardFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface JpaBoardService {

	void saveBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;
	
	BoardEntity selectBoardDetail(int boardIdx) throws Exception;

	void deleteBoard(int boardIdx);

	BoardFileEntity selectBoardFileInformation(int boardIdx, int idx) throws Exception;

	Page<BoardEntity> getBoardList(Pageable pageable);
}
