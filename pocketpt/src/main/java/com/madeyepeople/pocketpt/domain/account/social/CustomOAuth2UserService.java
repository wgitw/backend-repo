package com.madeyepeople.pocketpt.domain.account.social;

import com.madeyepeople.pocketpt.domain.account.constants.AuthProvider;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
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

    private static final String ID_ATTRIBUTE = "id";
    private static final String LOGIN_ATTRIBUTE = "login";
    private static final String EMAIL_ATTRIBUTE = "email";
    private static final String IMAGE_ATTRIBUTE = "image";
    private static final String LINK_ATTRIBUTE = "link";
    private static final String CREATE_FLAG = "create_flag";

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>)attributes.get("kakao_account");
//        String apiId = ((Integer)attributes.get(ID_ATTRIBUTE)).toString();
        String nickname = ((LinkedHashMap<String, Object>)map.get("profile")).get("nickname").toString();
        String email = map.get("email").toString();
        String social = userRequest.getClientRegistration().getRegistrationId();

        String imageUrl = "";
        if (attributes.get(IMAGE_ATTRIBUTE) instanceof Map) {
            imageUrl = (String)((Map)(attributes.get(IMAGE_ATTRIBUTE))).get(LINK_ATTRIBUTE) == null ?
                    "" : (String)((Map)(attributes.get(IMAGE_ATTRIBUTE))).get(LINK_ATTRIBUTE);
        }

        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        OAuth2User oAuth2User = signUpOrUpdateUser(attributes, email, social, imageUrl, accountOptional);

        return oAuth2User;
    }


    private OAuth2User signUpOrUpdateUser(Map<String, Object> attributes, String email, String social, String imageUrl,
                                          Optional<Account> accountOptional) {
        OAuth2User oAuth2User;
        Account saved;

        if (accountOptional.isEmpty()) {

            String encodedPassword = passwordEncoder.encode(UUID.randomUUID().toString());
            saved = accountRepository.save(Account.builder()
                    .email(email)
                    .social(AuthProvider.valueOf(social))
                    .password(encodedPassword)
                    .build()
            );

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
            saved = accountOptional.get();
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
        oAuth2User = AccountPrincipal.create(saved, attributes);
        return oAuth2User;
    }
}
