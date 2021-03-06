package com.couzon.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douzon.mvc.action.Action;
import com.douzon.mvc.util.WebUtils;
import com.douzon.mysite.repository.BoardDao;
import com.douzon.mysite.vo.BoardVo;

public class ModifyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String page = request.getParameter("page");
		String no = request.getParameter("no");
		
		BoardVo vo = new BoardDao().get(Long.parseLong(no));
		
		request.setAttribute("vo", vo);
		request.setAttribute("page", page);
		
		WebUtils.forward(request, response, "/WEB-INF/views/board/modify.jsp");
	}

}
