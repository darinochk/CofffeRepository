package org.example.coffeservice.repositories;

import java.util.List;
import java.util.Optional;
import org.example.coffeservice.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  List<User> findByFirstNameContainingIgnoreCase(String name);

  List<User> findByLastNameContainingIgnoreCase(String lastname);

  List<User>
      findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
          String name, String lastname, String email);
}
