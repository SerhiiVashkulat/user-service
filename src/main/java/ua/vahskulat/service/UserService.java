package ua.vahskulat.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.vahskulat.model.User;

import java.time.LocalDate;

public interface UserService {
    User createUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);

    User updateUserNames(Long id, String firstName, String lastName);

    User updateUser(Long id, User user);

    Page<User> getUsersByBirthDateRange(LocalDate from, LocalDate to, Pageable pageable);
}
