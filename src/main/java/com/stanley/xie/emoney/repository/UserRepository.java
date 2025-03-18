package com.stanley.xie.emoney.repository;

import com.stanley.xie.emoney.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserToken(String token);

    boolean isExistsByUsername(String username);
}
