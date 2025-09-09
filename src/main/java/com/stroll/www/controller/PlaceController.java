package com.stroll.www.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stroll.www.response.PlaceDetailResponse;
import com.stroll.www.response.PlaceListResponse;
import com.stroll.www.response.PlaceSummaryResponse;
import com.stroll.www.response.ReviewResponse;
import com.stroll.www.vo.ReplyVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stroll.www.property.AwsProps;
import com.stroll.www.vo.PlaceVO;
import com.stroll.www.vo.WishVO;
import com.stroll.www.controller.PlaceService;
import com.stroll.www.controller.ReplyService;
import com.stroll.www.controller.UserService;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequestMapping("/api")
public class PlaceController {
	@Autowired
	private PlaceService placeService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private UserService userService;
	private final S3Client s3Client = S3Client.builder() // S3 연결(인증)
			.region(Region.AP_NORTHEAST_2).credentialsProvider(StaticCredentialsProvider
					.create(AwsBasicCredentials.create(AwsProps.s3AccessKeyId, AwsProps.s3SecretAccessKey)))
			.build();
	
//	private String extractGuAddress(String fullAddress) {
//		if (fullAddress == null || fullAddress.isEmpty()) {
//	        return "";
//	    }
//	    Pattern pattern = Pattern.compile("^(.+?(구|군))");
//	    Matcher matcher = pattern.matcher(fullAddress);
//	    if (matcher.find()) {
//	        return matcher.group(1).trim(); // 전체 매칭된 부분 리턴
//	    }
//	    return ""; // 구나 군이 없을 경우 빈 문자열
//	}

    @GetMapping(value = "/places", produces = "application/json;charset=UTF-8")
    public ResponseEntity<PlaceListResponse> showAroundme(
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "keywords", defaultValue = "") String keywords,
            @RequestParam(value = "order", defaultValue = "distance") String order,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "maxDistance", defaultValue = "-1") int maxDistance,
            @RequestParam(value = "minStar", defaultValue = "-1") int minStar,
            HttpServletRequest request
    ) {
        System.out.println(address);
        System.out.println(keywords);
        System.out.println(order);
        System.out.println(page);
        System.out.println(maxDistance);
        System.out.println(minStar);
        // 기존 vo 사용
        PlaceVO vo = new PlaceVO();
        vo.setGuAddress(address);

        // 서비스 호출
        List<PlaceVO> searchedPlaces =
                placeService.getPlaceList(vo, keywords, order, page, request, maxDistance, minStar);

        // 총 페이지 수 (기존에 request attribute로 넣던 값 활용)
        int numOfPages = (Integer) request.getAttribute("numOfPages");

        // VO -> DTO 변환
        List<PlaceSummaryResponse> places = searchedPlaces.stream()
                .map(PlaceSummaryResponse::from)
                .toList();

        // 응답 래퍼 구성
        PlaceListResponse body = new PlaceListResponse(places, numOfPages);

        return ResponseEntity.ok(body);
    }

	@RequestMapping(value = "/place/{placeNo}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<PlaceDetailResponse> showDetail(@PathVariable(value = "placeNo") int placeNo, HttpSession session) {
        PlaceVO place = new PlaceVO();
        System.out.println(placeNo);
        place.setNo(placeNo);
        place = placeService.getPlace(place);
        PlaceDetailResponse placeDetailResponse = PlaceDetailResponse.from(place);

//		List<String> imgs =  placeService.getImgs(place);

        //찜한 곳인지 표시
		String id = (String) session.getAttribute("id");
		if (id != null) {
            WishVO wishVO = new WishVO();
			wishVO.setUserId(id);
			wishVO.setPlaceNo(place.getNo());
            placeDetailResponse.setWished(userService.isWishedPlace(wishVO));
		}
        return ResponseEntity.ok(placeDetailResponse);
	}

    @RequestMapping(value = "/place/{placeNo}/reviews", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<ReviewResponse>> getReviewsOfPlace(@PathVariable(value = "placeNo") int placeNo) {
        PlaceVO place = new PlaceVO();
        place.setNo(placeNo);
        List<ReplyVO> replies =  replyService.selectReplies(place);
        List<ReviewResponse> reviewListResponse  = new LinkedList<>(replies.stream().map(ReviewResponse::from).toList());
        return ResponseEntity.ok(reviewListResponse);
    }
/*
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
*/
	@RequestMapping(value = "/image/{image_title:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImageFromS3(@PathVariable String image_title) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(AwsProps.s3Bucket)
                .key("image/" + image_title)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(objectBytes.asByteArray());
    }

}
