package com.github.kmu_wink.yeogichadae2.domain.user.repository;

import com.github.kmu_wink.yeogichadae2.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
