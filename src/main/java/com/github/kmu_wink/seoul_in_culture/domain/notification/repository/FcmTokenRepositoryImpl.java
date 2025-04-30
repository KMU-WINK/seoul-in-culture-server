package com.github.kmu_wink.seoul_in_culture.domain.notification.repository;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.FcmToken;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class FcmTokenRepositoryImpl implements FcmTokenRepository {

    private final MongoOperations operations;

    @Override
    public List<String> findAllByUser(User user) {

        return operations.findDistinct(
                new Query().addCriteria(where("user").is(user)),
                "token",
                FcmToken.class,
                String.class
        );
    }

    @Override
    public Optional<FcmToken> findByToken(String token) {

        List<FcmToken> results = operations.aggregate(
                newAggregation(
                        match(where("token").is(token)),

                        lookup("user", "user.$id", "_id", "user"),
                        unwind("user", true)
                ),
                FcmToken.class,
                FcmToken.class
        ).getMappedResults();

        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.getFirst());
        }
    }

    @Override
    public boolean existsByToken(String token) {

        return operations.exists(new Query().addCriteria(where("token").is(token)), FcmToken.class);
    }

    @Override
    public FcmToken save(FcmToken fcmToken) {

        return operations.save(fcmToken);
    }

    @Override
    public void deleteByToken(String token) {

        operations.remove(new Query().addCriteria(where("token").is(token)), FcmToken.class);
    }
}
