package com.wanted.socialMediaIntegratedFeed.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Modifying
    @Query("UPDATE Member m SET m.refreshToken = ?2 WHERE m.id = ?1")
    void updateRefreshToken(Long id, String refreshToken);

}
