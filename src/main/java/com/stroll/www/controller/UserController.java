package com.stroll.www.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.stroll.www.response.PlaceDetailResponse;
import com.stroll.www.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.stroll.www.vo.UserVO;
import com.stroll.www.vo.WishVO;
import com.stroll.www.controller.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	private UserService service;

/*

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
*/
}
