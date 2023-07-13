package com.madeyepeople.pocketpt.domain.account.entity;

import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
public class Account extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    private String email;

    private String social;

    private String nickname;

    @Builder
    public Account(String email, String social, String nickname) {
        this.email = email;
        this.social = social;
        this.nickname = nickname;
    }
}
