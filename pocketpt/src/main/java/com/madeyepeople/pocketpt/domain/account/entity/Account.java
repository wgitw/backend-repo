package com.madeyepeople.pocketpt.domain.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.entity.ChattingMessageBookmark;
import com.madeyepeople.pocketpt.domain.account.constant.RoleEnumConverter;
import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.TopChattingRoom;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;



@Entity
@Getter
@ToString(exclude = {"chattingParticipantList", "chattingMessageBookmarkList", "ptMatchingListOfTrainer", "ptMatchingListOfTrainee", "monthlyPtPriceList"})
@NoArgsConstructor
@AllArgsConstructor
// TODO: @SQLDelete 로 삭제시 is_deleted = true 로 변경되게 하기
public class Account extends BaseEntity {

    /**
     * 공통 정보
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @OneToMany(mappedBy = "account")
    private List<ChattingParticipant> chattingParticipantList;

    @OneToMany(mappedBy = "account")
    private List<ChattingMessageBookmark> chattingMessageBookmarkList;

    @OneToMany(mappedBy = "account")
    private List<TopChattingRoom> topChattingRoomList;

    @OneToMany(mappedBy = "trainer")
    private List<PtMatching> ptMatchingListOfTrainer;

    @OneToMany(mappedBy = "trainee")
    private List<PtMatching> ptMatchingListOfTrainee;

    @OneToMany(mappedBy = "trainer")
    private List<Career> careerList;

    @OneToMany(mappedBy = "trainer")
    private List<MonthlyPtPrice> monthlyPtPriceList;

    @OneToMany(mappedBy = "account")
    private List<Purpose> purposeList;
  
    @Convert(converter = RoleEnumConverter.class)
    @Column(name = "account_role")
    private Role accountRole;

    @Nonnull
    private String provider;

    @Nonnull
    @Column(unique = true)
    private String email;

    private String name;

    private String phoneNumber;

    private String nickname;

    private String profilePictureUrl;

    private String gender;

    private LocalDateTime birthdate;

    /**
     * 회원 또는 트레이너 한쪽만 필요한 정보
     */
    private Float height;
    private Float weight;
    private String expertise;
    @Column(unique = true)
    private String identificationCode;
    private String recommenderCode;
    private String careerCertificateUrl;
    private String introduce;
    @ColumnDefault("0")
    private Integer totalSales;
    private Float serviceFeeRate;
    private Float discountRate;

    /**
     * 인증 관련 부가적인 정보
     */
    @Column(name = "oauth2_id")
    private Long oauth2Id;

    // oauth2 provider가 제공하는 access token. 회원 탈퇴를 위해 필요.
    private String oauthAccessToken;
  
    @JsonIgnore
    private String password;


    @Builder
    public Account(Long oauth2Id, Role accountRole, String email, String provider, String name, String phoneNumber, String nickname, String oauthAccessToken, String password, String profilePictureUrl) {
        this.oauth2Id = oauth2Id;
        this.accountRole = accountRole;
        this.provider = provider;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.oauthAccessToken = oauthAccessToken;
        this.profilePictureUrl = profilePictureUrl;
        this.password = password;
    }

    public Account updateByOAuthInfo(String nickname, String oauthAccessToken, String imageUrl) {
        this.nickname = nickname;
        this.oauthAccessToken = oauthAccessToken;
        this.profilePictureUrl = imageUrl;
        return this;
    }

    public Account updateByRegistrationRequest(String name, String phoneNumber, Role accountRole, String identificationCode) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.accountRole = accountRole;
        this.identificationCode = identificationCode;
        return this;
    }

    public Account updateTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
        return this;
    }

    public Account deleteAccountRole() {
        this.accountRole = null;
        return this;
    }
}
