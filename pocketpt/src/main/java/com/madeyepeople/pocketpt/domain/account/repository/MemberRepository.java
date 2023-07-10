package com.madeyepeople.pocketpt.domain.account.repository;

import com.madeyepeople.pocketpt.domain.account.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<ChatUser, Long> {

}
