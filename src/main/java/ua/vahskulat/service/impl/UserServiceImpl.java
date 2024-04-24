package ua.vahskulat.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.vahskulat.exception.UserEmailExistException;
import ua.vahskulat.exception.UserNotFoundException;
import ua.vahskulat.exception.UserWrongAgeException;
import ua.vahskulat.exception.UserWrongDateException;
import ua.vahskulat.model.User;
import ua.vahskulat.repository.UserRepository;
import ua.vahskulat.service.UserService;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Value("${user.minAge}")
    private int minAgeUser;

    @Override
    @Transactional
    public User createUser(User user) {
        log.info("Received request to create new user: {}", user);
        validateUser(user);
        log.info(" Saving to database new user: {} ", user);
        return userRepository.save(user);

    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info(" Delete user with id: {}", id);
        userRepository.delete(findUserById(id));
    }

    @Override
    @Cacheable(cacheNames = "user-cache", key = "#id")
    public User getUserById(Long id) {
        log.info("Getting user by ID: {}", id);
        return findUserById(id);
    }

    @Override
    @Transactional
    public User updateUserNames(Long id, String firstName, String lastName) {
        log.info("Updating email for user with ID {}: ", id);

        User user = findUserById(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        log.info("Updated names for user new firstName={}, new lastName={}", firstName, lastName);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        validationUserEmail(user.getEmail());

        User userUpdate = findUserById(id);
        userUpdate.setEmail(user.getEmail());
        userUpdate.setFirstName(user.getFirstName());
        userUpdate.setLastName(user.getLastName());
        userUpdate.setBirthDate(user.getBirthDate());
        userUpdate.setAddress(user.getAddress());
        userUpdate.setPhoneNumber(user.getPhoneNumber());

        log.info(" Update all user fields ");
        return userRepository.save(userUpdate);
    }

    @Override
    @Cacheable(cacheNames ="user-cache", key = "{#from, #to, #pageable}")
    public Page<User> getUsersByBirthDateRange(LocalDate from, LocalDate to, Pageable pageable) {
        validateDateRange(from, to);
        log.info(" Finding users by birth date from {} to {}", from, to);
        return userRepository.findUsersByBirthDateBetween(from, to, pageable);
    }


    private void validationUserAge(User user) {
        if (minAgeUser >= Period.between(user.getBirthDate(), LocalDate.now()).getYears()) {
            log.error(" The user {} {} is under 18 years old . ", user.getFirstName(), user.getLastName());
            throw new UserWrongAgeException(" User must be at least 18 years old ");
        }
    }

    private void validationUserEmail(String email) {

        if (userRepository.existsUserByEmail(email)) {
            log.error(" User email validation failed: {} is already used", email);
            throw new UserEmailExistException(" This email " + email + " already used ");
        }
    }

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            log.error(" Date range validation failed ");
            throw new UserWrongDateException(" From date " + from + " must be before To date " + to);
        }

    }

    private void validationDate(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) {
            log.error(" The date of birth is incorrect : {} ", birthDate);
            throw new UserWrongDateException(" Birth date must be earlier than current date ");
        }
    }

    private User findUserById(Long id) {
        log.info("Finding user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(" User whit id: {} not found ", id);
                    return new UserNotFoundException(" User with this " + id + " not found ");
                });
    }
    private void validateUser(User user) {
        validationDate(user.getBirthDate());

        validationUserAge(user);

        validationUserEmail(user.getEmail());
    }
}

