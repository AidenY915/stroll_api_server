package com.stroll.www.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.stroll.www.vo.UserVO;
import com.stroll.www.vo.WishVO;
import com.stroll.www.controller.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService service;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(UserVO vo, @RequestParam("path") String path, HttpServletRequest request) {
		String id = service.login(vo);
		if (id != null) {
			HttpSession session = request.getSession();
			session.setAttribute("id", id);
			session.setMaxInactiveInterval(60 * 20);
		}
		System.out.println("id : " + id);
		System.out.println(path);
		return "redirect:" + path.replace(request.getContextPath(), "");
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/";
	}

	@RequestMapping(value = "/registerOK")
	public String register(UserVO vo) {
		System.out.println("<register 컨트롤러>");
		service.registerUser(vo);
		return "redirect:/";
	}

	@RequestMapping(value = "/duplicateCheck")
	public void idDuplicateCheck(UserVO vo, HttpServletResponse response) {
		try (PrintWriter out = response.getWriter()) {
			service.duplicateCheck(vo, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setStatus(200);
		return;
	}

	@RequestMapping("/mypage")
	public String myPage(WishVO wishVO, UserVO userVO,Model model, HttpServletRequest request) {
		String id = (String)request.getSession().getAttribute("id");
		if(id == null) return "redirect:/";
		wishVO.setUserId(id);
		userVO.setId(id);
		String list = request.getParameter("list");
		if(list == null || list.equalsIgnoreCase("wishList")) {
			model.addAttribute("places",service.getWishedPlaces(wishVO));
		}else if(list.equalsIgnoreCase("myPlaces")) {
			model.addAttribute("places",service.getUserPlaces(userVO));
		}else if(list.equalsIgnoreCase("reviews"))
			model.addAttribute("reviews",service.getUserReivews(userVO));
		return "myPage";
	}

	@RequestMapping("/addToWishList")
	public void addToWishList(WishVO vo, HttpServletRequest request, HttpServletResponse response) {
		String id = (String) request.getSession().getAttribute("id");
		int placeNo = Integer.parseInt(request.getParameter("no"));
		vo.setUserId(id);
		vo.setPlaceNo(placeNo);
		service.addToWishList(vo);
		response.setStatus(200);
	}

	@RequestMapping("/deleteFromWishList")
	public void deleteFromWishList(WishVO vo, HttpServletRequest request, HttpServletResponse response) {
		String id = (String) request.getSession().getAttribute("id");
		int placeNo = Integer.parseInt(request.getParameter("no"));
		vo.setUserId(id);
		vo.setPlaceNo(placeNo);
		service.deleteFromWishList(vo);
		response.setStatus(200);
	}
	
	@RequestMapping("/withdraw")
	public String withdraw(UserVO vo, HttpSession session) {
		vo.setId((String) session.getAttribute("id"));
		if(!service.withdraw(vo)) {
			System.out.println("비밀번호가 틀렸습니다.");
			return "redirect:mypage";
		}
		return "redirect:logout";
	}
	
}
