package com.madeyepeople.pocketpt.domain.account.social;

import com.madeyepeople.pocketpt.domain.account.dto.Oauth2ProviderInfo;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.mapper.ToAccountEntity;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final String OAUTH2_ID_ATTRIBUTE = "id";
    private static final String EMAIL_ATTRIBUTE = "email";
    private static final String NICKNAME_ATTRIBUTE = "nickname";
    private static final String IMAGE_ATTRIBUTE = "profile_image_url";

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ToAccountEntity toAccountEntity;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.error(userRequest.toString());
        log.error(userRequest.getAccessToken().getTokenValue());
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>)attributes.get("kakao_account");

        Long oauth2Id = 0L;
        String email = "";
        String nickname = "";
        String provider = "";

        try {
            oauth2Id = Long.valueOf((attributes.get(OAUTH2_ID_ATTRIBUTE)).toString());
            provider = userRequest.getClientRegistration().getRegistrationId();
            email = map.get(EMAIL_ATTRIBUTE).toString();
            nickname = ((LinkedHashMap<String, Object>) map.get("profile")).get(NICKNAME_ATTRIBUTE).toString();
        } catch (Exception e) {
            log.error(CustomExceptionMessage.OAUTH2_NECESSARY_INFO_NOT_FOUND.getMessage());
            e.printStackTrace();
        }

        String imageUrl = "";
        // kakao에서 110x110, 640x640 둘 다 제공. 일단 640만 저장. 추후 부담되면 110도 저장 logic 추가
        try {
            imageUrl = ((LinkedHashMap<String, Object>)map.get("profile")).get(IMAGE_ATTRIBUTE) == null ?
            "" : ((LinkedHashMap<String, Object>)map.get("profile")).get(IMAGE_ATTRIBUTE).toString();
        } catch (Exception e) {
            log.error(CustomExceptionMessage.PROFILE_IMAGE_PARSING_ERROR.getMessage());
            e.printStackTrace();
        }

//        String fileUrl = s3FileService.uploadFile("chatting/" + chattingFileCreateRequest.getChattingRoomId() + "/", chattingFileCreateRequest.getFile());

        Optional<Account> accountOptional = accountRepository.findByEmailAndIsDeletedFalse(email);
        Oauth2ProviderInfo oauth2ProviderInfo = Oauth2ProviderInfo.builder()
                .attributes(attributes)
                .oauth2Id(oauth2Id)
                .provider(provider)
                .email(email)
                .nickname(nickname)
                .imageUrl(imageUrl)
                .accountOptional(accountOptional)
                .build();

        return signUpOrUpdateUser(oauth2ProviderInfo);
    }

    private OAuth2User signUpOrUpdateUser(Oauth2ProviderInfo oauth2ProviderInfo) {
        OAuth2User oAuth2User;
        Account saved;

        if (oauth2ProviderInfo.getAccountOptional().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(UUID.randomUUID().toString());
            // TODO: imageUrl을 S3 url로 바꿔야함.
            saved = accountRepository.save(toAccountEntity.toAccountCreateEntity(oauth2ProviderInfo, encodedPassword));


//            Role role = roleRepository.findByValue(RoleEnum.ROLE_USER).orElseThrow(() ->
//                    new EntityNotFoundException(RoleEnum.ROLE_USER + "에 해당하는 Role이 없습니다."));
//            user = User.of(username, encodedPassword, email, login, imageUrl, member);
//            UserRole userRole = UserRole.of(role, user);
//
//            userRepository.save(user);
//            userRoleRepository.save(userRole);
//            necessaryAttributes.put(CREATE_FLAG, true);
            //생성해야할 객체 추가로 더 있을 수 있음.
        } else {
            log.info("\n\n이미 가입된 회원입니다. nickname을 업데이트합니다.\n\n");
            saved = oauth2ProviderInfo.getAccountOptional().get();
            saved = accountRepository.save(saved
                    .update(
                            saved.getNickname()
                    )
            );
//            //회원정보 수정
//            user = userOptional.get();
//            // 새로 로그인 시 oauth2 기반 데이터로 변경하지않음.
////            user.updateUserBHOAuthIfo(imageUrl);
//            necessaryAttributes.put(CREATE_FLAG, false);
        }
        log.info(saved.toString());
        oAuth2User = AccountPrincipal.create(saved, oauth2ProviderInfo.getAttributes());
        return oAuth2User;
    }
}
