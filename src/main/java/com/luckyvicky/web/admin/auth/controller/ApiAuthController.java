package com.luckyvicky.web.admin.auth.controller;

import com.luckyvicky.web.common.member.dto.MemberDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class ApiAuthController {

    @PostMapping("/api/admin/auth/login")
    public HashMap<String, Object> login(@RequestBody MemberDTO memberDTO) {


        System.out.println(memberDTO.getMemberId());
        System.out.println(memberDTO.getPassword());

        HashMap hashMap = new HashMap();
        hashMap.put("test", "ㅇㅇ");

        return hashMap;
    }

}
