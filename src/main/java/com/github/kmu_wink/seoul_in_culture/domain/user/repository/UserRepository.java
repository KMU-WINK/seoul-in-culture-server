package com.github.kmu_wink.seoul_in_culture.domain.user.repository;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByKakao(long kakao);

    boolean existsByNickname(String nickname);
}
