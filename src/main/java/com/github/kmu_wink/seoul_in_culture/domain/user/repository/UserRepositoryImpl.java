package com.github.kmu_wink.seoul_in_culture.domain.user.repository;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final MongoOperations operations;

    @Override
    public Optional<User> findById(String id) {

        return Optional.ofNullable(operations.findById(id, User.class));
    }

    @Override
    public Optional<User> findByKakao(long kakao) {

        return Optional.ofNullable(operations.findOne(new Query().addCriteria(where("kakao").is(kakao)), User.class));
    }

    @Override
    public boolean existsByNickname(String nickname) {

        return operations.exists(new Query().addCriteria(where("nickname").is(nickname)), User.class);
    }

    @Override
    public User save(User user) {

        return operations.save(user);
    }
}
