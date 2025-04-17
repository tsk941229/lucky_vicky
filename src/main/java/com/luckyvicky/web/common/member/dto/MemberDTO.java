package com.luckyvicky.web.common.member.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberDTO {

    private String memberId;

    private String password;

}
