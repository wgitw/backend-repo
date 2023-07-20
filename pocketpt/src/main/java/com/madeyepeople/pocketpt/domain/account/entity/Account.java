package com.madeyepeople.pocketpt.domain.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.madeyepeople.pocketpt.domain.account.constants.AuthProvider;
import com.madeyepeople.pocketpt.domain.account.constants.Role;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;


@Entity
@Getter
@ToString
@NoArgsConstructor
// TODO: @SQLDelete 로 삭제시 is_deleted = true 로 변경되게 하기
public class Account extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long accountId;

    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider social;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    private String nickname;

    @JsonIgnore
    private String password;

    // 소셜 제공자에서 제공하는 유저 고유 id. email 못받으면 이걸로 중복 가입 방지 로직 구현 예정
//    private String providerId;

    @Builder
    public Account(String email, AuthProvider social, String nickname, String password) {
        this.email = email;
        this.social = social;
//        this.role = role; 이건 OAuth 이후 추가정보 받을때 알 수 있으니 최초 생성시기인 OAuth때는 모름
        this.nickname = nickname;
        this.password = password;
    }

    public Account update(String nickname) {
        this.nickname = nickname;
        return this;
    }
}

//public abstract  class OAuth2UserInfo {
//
//    protected Map<String, Object> attributes;
//
//    public OAuth2UserInfo(Map<String, Object> attributes) {
//        this.attributes = attributes;
//    }
//
//    public Map<String, Object> getAttributes() {
//        return attributes;
//    }
//
//    public abstract String getId();
//
//    public abstract String getName();
//
//    public abstract String getEmail();
//
//    public abstract String getImageUrl();
//}