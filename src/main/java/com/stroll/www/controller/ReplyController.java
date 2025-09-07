package com.stroll.www.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stroll.www.vo.ReplyVO;
import com.stroll.www.controller.ReplyService;

/*
@Controller
public class ReplyController {
	@Autowired
	private ReplyService service;
	
	@RequestMapping(value = "/insertReply", method = RequestMethod.POST)
	public String insertReply(ReplyVO vo, HttpServletRequest request, Model model, RedirectAttributes redirect){
		HttpSession session = request.getSession(false);
		redirect.addAttribute("no", vo.getPlaceNo());
		if(session == null)
			return "redirect:detail";
		vo.setUserId((String)session.getAttribute("id"));
		System.out.println(vo);
		service.insertReply(vo);
		System.out.println("댓글 등록 서비스 끝");
		return "redirect:detail";
	}
	@RequestMapping(value="/deleteReply")
	public String delete(ReplyVO vo, HttpSession session, RedirectAttributes redirectAttr) {
		vo.setUserId((String)session.getAttribute("id"));
		redirectAttr.addAttribute("no", vo.getPlaceNo());
		service.deleteReply(vo);
		return "redirect:detail";
	}
}
*/