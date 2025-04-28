package com.stroll.www.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stroll.www.vo.PlaceVO;
import com.stroll.www.vo.WishVO;

@Controller
public class PlaceController {
	@Autowired
	private PlaceService placeService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private UserService userService;
	
	private String extractGuAddress(String fullAddress) {
		if (fullAddress == null || fullAddress.isEmpty()) {
	        return "";
	    }
	    Pattern pattern = Pattern.compile("^(.+?(구|군))");
	    Matcher matcher = pattern.matcher(fullAddress);
	    if (matcher.find()) {
	        return matcher.group(1).trim(); // 전체 매칭된 부분 리턴
	    }
	    return ""; // 구나 군이 없을 경우 빈 문자열
	}

	@RequestMapping("/aroundme")
	public String showAroundme(@RequestParam(value = "address", required = false) String address, PlaceVO vo, Model model, HttpServletRequest request) {
		vo.setGuAddress(address);
		String keywords = request.getParameter("keywords");
		String order = request.getParameter("order");
		String pageStr = request.getParameter("page");
		String maxDistanceStr = request.getParameter("maxDistance");
		String minStarStr = request.getParameter("minStar");
		int maxDistance = maxDistanceStr == null ? -1 : Integer.parseInt(maxDistanceStr);
		int minStar = minStarStr == null ? -1 : Integer.parseInt(minStarStr);
		if (keywords == null)
			keywords = "";
		if (order == null)
			order = "distance";
		int page = pageStr == null ? 1 : Integer.parseInt(pageStr);
		model.addAttribute("places", placeService.getPlaceList(vo, keywords, order, page, request , maxDistance, minStar));
		int numOfPages = (Integer) request.getAttribute("numOfPages");
		int firstPage = page - 4 >= 1 ? page - 4 : 1;
		int lastPage = firstPage + 8 <= numOfPages ? firstPage + 8 : numOfPages;
		model.addAttribute("firstPage", firstPage);
		model.addAttribute("lastPage", lastPage);
		return "aroundme";
	}

	@RequestMapping("/detail")
	public String showDetail(PlaceVO vo, WishVO wishVO, Model model, HttpSession session) {
		model.addAttribute("place", placeService.getPlace(vo));
		model.addAttribute("imgs", placeService.getImgs(vo));
		model.addAttribute("replies", replyService.selectReplies(vo));
		String id = (String) session.getAttribute("id");
		if (id != null) {
			wishVO.setUserId(id);
			wishVO.setPlaceNo(vo.getNo());
			model.addAttribute("isWishedPlace", userService.isWishedPlace(wishVO));
		}
		return "detail";
	}
	
	@RequestMapping(value = "/insertPlace", method = RequestMethod.POST)
	public String insertPlace(@RequestParam("imgs") MultipartFile[] imgs, @RequestParam("address") String address, PlaceVO vo, HttpSession session, RedirectAttributes redirect) {
		String id = (String)session.getAttribute("id");
		if(id == null) return "redirect:/";
		vo.setUserId(id);
		vo.setGuAddress(extractGuAddress(address));
		vo.setAfterGuAddress(address.replace(vo.getGuAddress(),"").trim());
		redirect.addAttribute("no", placeService.insertPlace(vo, imgs));
		return "redirect:detail";
	}
	@RequestMapping(value = "/deletePlace")
	public String deletePlace(PlaceVO vo, HttpSession session) {
		String id = (String) session.getAttribute("id");
		if(id==null || !placeService.deletePlace(vo, id))
			return "redirect:detail?no="+vo.getNo();
		return "redirect:aroundme"; 
	}
}
