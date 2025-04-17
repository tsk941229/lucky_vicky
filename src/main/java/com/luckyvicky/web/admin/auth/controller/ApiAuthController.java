package com.luckyvicky.web.admin.auth.controller;

import com.luckyvicky.web.common.member.dto.MemberDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class ApiAuthController {

    @PostMapping("/api/admin/auth/login")
    public HashMap<String, Object> login(MemberDTO memberDTO) {

//        System.out.println("memberId : " + memberDTO.getMemberId());
//        System.out.println("password : " + memberDTO.getPassword());

        HashMap hashMap = new HashMap();
        hashMap.put("test", "ㅇㅇ");

        return hashMap;
    }

}
