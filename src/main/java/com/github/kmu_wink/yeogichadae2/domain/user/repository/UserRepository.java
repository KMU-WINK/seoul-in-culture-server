package com.github.kmu_wink.yeogichadae2.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.kmu_wink.yeogichadae2.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByKakao(long kakao);

	boolean existsByNickname(String nickname);
}
