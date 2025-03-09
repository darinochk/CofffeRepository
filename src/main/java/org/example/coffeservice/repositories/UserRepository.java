package org.example.coffeservice.repositories;

import org.example.coffeservice.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);


    List<User> findByFirstNameContainingIgnoreCase(String name);

    List<User> findByLastNameContainingIgnoreCase(String lastname);

    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name, String lastname, String email);
}
