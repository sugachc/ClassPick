package com.classpick.springbootproject.controller;


import com.classpick.springbootproject.requestdto.LoginRequestDto;
import com.classpick.springbootproject.responsedto.LoginResponseDto;
import com.classpick.springbootproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public void login(@Validated @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request){

        //엑세스토큰 추출
        //userService.login(loginRequestDto.getAccessToken());

        /** 아래의 작업을 통해 3가지 일이 수행됨
         *
         * 1) 랜덤한 session key 값을 생성해주고
         * 2) 그 session key와 대응되는 value로 memberId를 설정하여 , 그 한 쌍을 session store에 저장한 후
         * 3) 그 session key 값을 응답 cookie 값으로 저장
         * */

        /** 1. 세션이 있으면 신규 세션 반환 or 없으면 신규 세션 생성 (세션이란 결국 key와 value의 조합인데, 이때 빈 value에 key를 갖는 세션을 생셩) */
        HttpSession session = request.getSession();

        /** 2. 그 생성된 세션의 빈 value에 memberId 값을 넣어주고 , 그 value의 이름을 MEMBER_ID로 설정 - 이때 이름일 뿐 key가 아니라는 점 주의  */
       // session.setAttribute(USER_ID, loginParameterDto.getMemberId());

        //return LoginResponseDto.builder().status(loginParameterDto.getStatus()).build();

    }




}
