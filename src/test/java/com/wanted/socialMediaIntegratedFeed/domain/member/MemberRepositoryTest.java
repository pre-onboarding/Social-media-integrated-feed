package com.wanted.socialMediaIntegratedFeed.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Member 저장 성공")
    void memberSignup() {
        // given
        Member member = Member.builder()
                .email("example@gmail.com")
                .username("example")
                .password("example")
                .build();

        // when
        Member saveMember = memberRepository.save(member);

        // then
        assertAll(
                () -> assertThat(member.getId()).isEqualTo(saveMember.getId()),
                () -> assertThat(member.getEmail()).isEqualTo(saveMember.getEmail()),
                () -> assertThat(member.getUsername()).isEqualTo(saveMember.getUsername()),
                () -> assertThat(member.getPassword()).isEqualTo(saveMember.getPassword())
        );
    }
}