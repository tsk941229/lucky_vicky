package com.luckyvicky.web.admin.auth.repository;

import com.luckyvicky.web.common.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Member, Long> {
}
