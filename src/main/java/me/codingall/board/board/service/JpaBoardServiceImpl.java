package me.codingall.board.board.service;

import me.codingall.board.board.entity.BoardEntity;
import me.codingall.board.board.entity.BoardFileEntity;
import me.codingall.board.board.repository.JpaBoardRepository;
import me.codingall.board.common.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Optional;

@Service
public class JpaBoardServiceImpl implements JpaBoardService{
	
	private final JpaBoardRepository jpaBoardRepository;
	
	private final FileUtils fileUtils;

	public JpaBoardServiceImpl(JpaBoardRepository jpaBoardRepository, FileUtils fileUtils) {
		this.jpaBoardRepository = jpaBoardRepository;
		this.fileUtils = fileUtils;
	}

	@Override
	public List<BoardEntity> selectBoardList() throws Exception {
		return jpaBoardRepository.findAllByOrderByBoardIdxDesc();
	}

	@Override
	public void saveBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		board.setCreatorId("admin");
		List<BoardFileEntity> list = fileUtils.parseFileInfo(multipartHttpServletRequest);
		if(!CollectionUtils.isEmpty(list)){
			board.setFileList(list);
		}
		jpaBoardRepository.save(board);
	}
	
	@Override
	public BoardEntity selectBoardDetail(int boardIdx) throws Exception{
		Optional<BoardEntity> optional = jpaBoardRepository.findById(boardIdx);
		if(optional.isPresent()){
			BoardEntity board = optional.get();
			board.setHitCnt(board.getHitCnt() + 1);
			jpaBoardRepository.save(board);
			
			return board;
		}
		else {
			throw new NullPointerException();
		}
	}

	@Override
	public void deleteBoard(int boardIdx) {
		jpaBoardRepository.deleteById(boardIdx);
	}

	@Override
	public BoardFileEntity selectBoardFileInformation(int boardIdx, int idx) throws Exception {
		BoardFileEntity boardFile = jpaBoardRepository.findBoardFile(boardIdx, idx);
		return boardFile;
	}

	@Override
	public Page<BoardEntity> getBoardList(Pageable pageable, Integer count) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
		PageRequest pageRequest = PageRequest.of(page, count);
		return jpaBoardRepository.findAll(pageRequest);
	}
}
