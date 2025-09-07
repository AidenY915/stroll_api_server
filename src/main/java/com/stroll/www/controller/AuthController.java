package com.stroll.www.controller;

import com.stroll.www.request.LoginRequest;
import com.stroll.www.response.TokenResponse;
import com.stroll.www.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        UserVO userVo = new UserVO(loginRequest.getUserId(), loginRequest.getPassword());
        String id = userService.login(userVo);
        if (id == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401
                    .body(Map.of(
                            "message", "아이디 또는 비밀번호가 올바르지 않습니다."
                    ));
        }
        String token = null;
        //JwtService로 토큰 발급
        try {
            token = jwtService.generateToken(id);
        }catch (DecoderException e){
            e.printStackTrace(); //토큰 발급 실패
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401
                    .body(Map.of(
                            "message", "아이디 또는 비밀번호가 올바르지 않습니다."
                    ));
        }
        TokenResponse tokenResponse = new TokenResponse(token);
        return ResponseEntity.ok(tokenResponse);
    }
/*
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/";
	}
*/
}