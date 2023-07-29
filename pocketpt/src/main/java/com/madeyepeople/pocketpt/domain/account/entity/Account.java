package com.madeyepeople.pocketpt.domain.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.madeyepeople.pocketpt.domain.account.constants.Role;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;


@Entity
@Getter
@ToString
@NoArgsConstructor
//@EqualsAndHashCode
// TODO: @SQLDelete 로 삭제시 is_deleted = true 로 변경되게 하기
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @OneToMany(mappedBy = "account")
    private List<ChattingParticipant> chattingParticipantList;

    @Column(name = "oauth2_id")
    private Long oauth2Id;

    @Nonnull
    private String provider;

    @Nonnull
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String nickname;

    private String profilePictureUrl; // 카멜로 안되어 있어서 내가 바꿀게

    // oauth2 provider가 제공하는 access token. 회원 탈퇴를 위해 필요.
    private String oauth2AccessToken;
    @JsonIgnore
    private String password;


    @Builder
    public Account(Long oauth2Id, String email, String provider, String nickname, String oauth2AccessToken, String password) {
        this.oauth2Id = oauth2Id;
        this.provider = provider;
        this.email = email;
        this.nickname = nickname;
        this.oauth2AccessToken = oauth2AccessToken;
        this.password = password;
    }

    public Account update(String nickname, String oauth2AccessToken) {
        this.nickname = nickname;
        this.oauth2AccessToken = oauth2AccessToken;
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