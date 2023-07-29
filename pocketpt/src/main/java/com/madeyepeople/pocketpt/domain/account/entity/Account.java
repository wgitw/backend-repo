package com.madeyepeople.pocketpt.domain.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.madeyepeople.pocketpt.domain.account.constants.Role;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Date;



@Entity
@Getter
@ToString
@NoArgsConstructor
// TODO: @SQLDelete 로 삭제시 is_deleted = true 로 변경되게 하기
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @OneToMany(mappedBy = "account")
    private List<ChattingParticipant> chattingParticipantList;

    @Column(name = "oauth2_id")
    private Long oauth2Id;
  
    @Enumerated(EnumType.STRING)
    private Role role;

    @Nonnull
    private String provider;

    @Nonnull
    private String email;

    private String name;

    private String phoneNumber;

    private String nickname;

    private String profilePictureUrl;

    private String gender;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    // oauth2 provider가 제공하는 access token. 회원 탈퇴를 위해 필요.
    private String oauthAccessToken;
  
    @JsonIgnore
    private String password;


    @Builder
    public Account(Long oauth2Id, String email, String provider, String nickname, String oauthAccessToken, String password, String profilePictureUrl) {
        this.oauth2Id = oauth2Id;
        this.provider = provider;
        this.email = email;
        this.nickname = nickname;
        this.oauthAccessToken = oauthAccessToken;
        this.profilePictureUrl = profilePictureUrl;
        this.password = password;
    }

    public Account update(String nickname, String oauthAccessToken) {
        this.nickname = nickname;
        this.oauthAccessToken = oauthAccessToken;
        return this;
    }
}
