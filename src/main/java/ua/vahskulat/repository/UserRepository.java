package ua.vahskulat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.vahskulat.model.User;

import java.time.LocalDate;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail (String email);

    Page<User> findUsersByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable);
}
