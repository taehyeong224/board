package me.codingall.board.board.controller;


import lombok.extern.slf4j.Slf4j;
import me.codingall.board.board.entity.BoardEntity;
import me.codingall.board.board.entity.BoardFileEntity;
import me.codingall.board.board.service.JpaBoardService;
import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.Arrays;

@Slf4j
@Controller
public class JpaBoardController {
	
	private final JpaBoardService jpaBoardService;

	private final Environment env;

	public JpaBoardController(JpaBoardService jpaBoardService, Environment env) {
		this.jpaBoardService = jpaBoardService;
		this.env = env;
	}

	@RequestMapping(value="/jpa/board", method= RequestMethod.GET)
	public ModelAndView openBoardList(@PageableDefault(sort = "boardIdx", direction = Sort.Direction.DESC) Pageable pageable, ModelMap model) throws Exception{
		log.info("123123 env : " + Arrays.toString(env.getActiveProfiles()));
		log.info("pageable : " + pageable.toString());
		ModelAndView mv = new ModelAndView("board/jpaBoardList");
		Page<BoardEntity> boardList = jpaBoardService.getBoardList(pageable);
		log.info("총 element 수 : {}, 전체 page 수 : {}, 페이지에 표시할 element 수 : {}, 현재 페이지 index : {}, 현재 페이지의 element 수 : {}",
				boardList.getTotalElements(), boardList.getTotalPages(), boardList.getSize(),
				boardList.getNumber(), boardList.getNumberOfElements());
		mv.addObject("list", boardList);
		
		return mv;
	}
	
	@RequestMapping(value="/jpa/board/write", method= RequestMethod.GET)
	public String openBoardWrite() throws Exception{
		return "/board/jpaBoardWrite";
	}
	
	@RequestMapping(value="/jpa/board/write", method= RequestMethod.POST)
	public String writeBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
		jpaBoardService.saveBoard(board, multipartHttpServletRequest);
		return "redirect:/jpa/board";
	}
	
	@RequestMapping(value="/jpa/board/{boardIdx}", method= RequestMethod.GET)
	public ModelAndView openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception{
		ModelAndView mv = new ModelAndView("board/jpaBoardDetail");
		
		BoardEntity board = jpaBoardService.selectBoardDetail(boardIdx);
		mv.addObject("board", board);
		
		return mv;
	}
	
	@RequestMapping(value="/jpa/board/{boardIdx}", method= RequestMethod.PUT)
	public String updateBoard(BoardEntity board) throws Exception{
		jpaBoardService.saveBoard(board, null);
		return "redirect:/jpa/board";
	}
	
	@RequestMapping(value="/jpa/board/{boardIdx}", method= RequestMethod.DELETE)
	public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
		jpaBoardService.deleteBoard(boardIdx);
		return "redirect:/jpa/board";
	}
	
	@RequestMapping(value="/jpa/board/file", method= RequestMethod.GET)
	public void downloadBoardFile(int boardIdx, int idx, HttpServletResponse response) throws Exception{
		BoardFileEntity file = jpaBoardService.selectBoardFileInformation(boardIdx, idx);
		
		byte[] files = FileUtils.readFileToByteArray(new File(file.getStoredFilePath()));
		
		response.setContentType("application/octet-stream");
		response.setContentLength(files.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(file.getOriginalFileName(),"UTF-8")+"\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		response.getOutputStream().write(files);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
}
