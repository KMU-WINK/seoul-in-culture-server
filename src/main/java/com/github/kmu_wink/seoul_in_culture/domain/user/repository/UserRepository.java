package com.github.kmu_wink.seoul_in_culture.domain.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByKakao(long kakao);

	boolean existsByNickname(String nickname);
}
