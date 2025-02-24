package org.example.coffeservice.repositories;

import org.example.coffeservice.models.UserAccount;
import org.example.coffeservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUser(User user);
}
