package com.wanted.socialMediaIntegratedFeed.web.member;

import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.domain.member.MemberRepository;
import com.wanted.socialMediaIntegratedFeed.global.exception.ErrorCode;
import com.wanted.socialMediaIntegratedFeed.global.exception.ErrorException;
import com.wanted.socialMediaIntegratedFeed.global.jwt.JwtProvider;
import com.wanted.socialMediaIntegratedFeed.web.member.dto.ApprovalRequest;
import com.wanted.socialMediaIntegratedFeed.web.member.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.wanted.socialMediaIntegratedFeed.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    private final PasswordEncoder encoder;

    private final RedisTemplate redisTemplate;

    private ValueOperations<String, String> valueOperations;


    /**
     * 멤버 회원가입
     * @param request 에서 받은 이메일, 유저네임이 중복되어 있는지 확인 후 패스워드를 암호화해서 멤버를 저장합니다.
     *                멤버 인증코드를 생성하여 redis에 저장
     */
    @Transactional
    public void memberSignup(SignupRequest request) {
        emailDuplicateCheck(request.getEmail());
        usernameDuplicateCheck(request.getUsername());

        Member member = Member.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword())).build();

        memberRepository.save(member);

        String authCode = createAuthCode();

        valueOperations = redisTemplate.opsForValue();
        valueOperations.set("AuthCode "+ member.getUsername(), authCode, 5, TimeUnit.MINUTES);
        /** Todo: 이메일로 인증코드 전송 기능 추가시 삭제 요망 */
        log.info("AuthCode = {}", valueOperations.get("AuthCode " + member.getUsername()));
    }

    /**
     * 멤버 인증
     * @param request 에서 받은 유저네임으로 멤버를 찾고 비밀번호가 맞는지 확인 후
     *                redis에서 AuthCode를 조회해서 올바른 코드면 멤버를 승인
     */
    @Transactional
    public void memberApproval(ApprovalRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ErrorException(NON_EXISTENT_MEMBER));

        if (!encoder.matches(request.getPassword(), member.getPassword())) {
            throw new ErrorException(WRONG_PASSWORD);
        }

        valueOperations = redisTemplate.opsForValue();
        String redisAuthCode = valueOperations.get("AuthCode " + member.getUsername());

        if (!redisAuthCode.equals(request.getAuthCode())) {
            throw new ErrorException(WRONG_AUTH_CODE);
        }
        member.updateAuth();
    }

    /**
     * 랜덤 6자리 인증 코드 생성
     */
    private String createAuthCode() {
        Random rand  = new Random();
        String numStr = "";
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }
        return numStr;
    }

    /**
     * 중복된 이메일 이라면 예외를 던져주고, 그렇지 않다면 false를 리턴합니다.
     */
    private boolean emailDuplicateCheck(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new ErrorException(DUPLICATE_EMAIL);
        }
        return false;
    }

    /**
     * 중복된 유저네임 이라면 예외를 던져주고, 그렇지 않다면 false를 리턴합니다.
     */
    private boolean usernameDuplicateCheck(String name) {
        if (memberRepository.existsByUsername(name)) {
            throw new ErrorException(DUPLICATE_USERNAME);
        }
        return false;
    }

    @Transactional
    public String signin(Member member) {
        memberRepository.updateRefreshToken(member.getId(), jwtProvider.generateRefreshToken(member));
        return jwtProvider.generateAccessToken(member);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException("이메일 혹은 비밀번호가 일치하지 않습니다.", ErrorCode.NOT_FOUND_MEMBER));
    }

}
