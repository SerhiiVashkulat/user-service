package ua.vahskulat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.vahskulat.exception.UserEmailExistException;
import ua.vahskulat.exception.UserNotFoundException;
import ua.vahskulat.exception.UserWrongAgeException;
import ua.vahskulat.exception.UserWrongDateException;
import ua.vahskulat.model.Address;
import ua.vahskulat.model.User;
import ua.vahskulat.repository.UserRepository;
import ua.vahskulat.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User userValid;
    private User userInvalid;
    private User userUpdate;

    @BeforeEach
    public void setUp() {

        String phone = "123-456-789";

        Address address =
                new Address("Ukraine", "Kyiv", "Street", "50", "50", "1234");

        this.userValid = new User(1L, "example@gmail.com", "Serhii",
                LocalDate.of(1991, 1, 13), "Vashkulat", address, phone);

        this.userInvalid = new User(2L, "example1@gmail.com", "Serhii",
                LocalDate.of(2010, 1, 13), "Shapoval", address, phone);

        this.userUpdate = new User(1L, "example@gmail.com", "updateFN",
                LocalDate.of(1991, 1, 13), "updateLN", address, phone);
//        userList= new ArrayList<>();
//        userList.add(userValid);
//        pageable = PageRequest.of(0,10);
//        pageList = new PageImpl<>(userList,pageable,userList.size());
    }

    @Test
    void testCreateUser_ByValidValues_ReturnSavedUser() {

        when(userRepository.save(any(User.class))).thenReturn(userValid);

        User userResult = userService.createUser(userValid);

        verify(userRepository, times(1)).save(userValid);

        assertNotNull(userResult);
        assertEquals(userResult.getFirstName(), userValid.getFirstName());
        assertEquals(userResult.getLastName(), userValid.getLastName());

    }

    @Test
    void testCreateUser_ByEmailExist_ReturnUserEmailExistException() {

        String emailExist = "example1@gmail.com";

        when(userRepository.existsUserByEmail(emailExist)).thenThrow(UserEmailExistException.class);

        assertThrows(UserEmailExistException.class, () -> userService.createUser(userInvalid));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_ByWrongBirthDate_ReturnUserWrongDateException() {

        User wrongDate = User.builder()
                .birthDate(LocalDate.of(3000, 1, 1))
                .build();

        assertThrows(UserWrongDateException.class, () -> userService.createUser(wrongDate));
        verify(userRepository, never()).save(wrongDate);

    }

    @Test
    void testDeleteUser() {

        Long id = 1L;

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(userValid));

        doNothing().when(userRepository).delete(userValid);

        userService.deleteUser(id);

        verify(userRepository, times(1)).delete(userValid);

    }

    @Test
    void testFindUserById_ValidId_ReturnUser() {

        Long id = 1l;

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(userValid));

        User currentUserById = userService.getUserById(id);

        assertEquals(currentUserById.getEmail(), userValid.getEmail());
        assertEquals(currentUserById.getFirstName(), userValid.getFirstName());
        assertEquals(id, currentUserById.getId());

        verify(userRepository, times(1)).findById(id);

    }

    @Test
    void testFindUserById_InvalidId_ReturnUserNotFoundExceptionUser() {

        Long id = 3l;

        when(userRepository.findById(any(Long.class))).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateUserNames_ReturnUpdateUser() {

        Long id = 1l;
        String firstName = "updateFN";
        String lastName = "updateLN";

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(userValid));

        when(userRepository.save(any(User.class))).thenReturn(userUpdate);


        User result = userService.updateUserNames(id, firstName, lastName);

        assertEquals(userUpdate, result);
        assertEquals(firstName, result.getFirstName());

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(userValid);

    }

    @Test
    void testUpdateUserAllFields_ReturnUpdatedUser() {

        Long id = 1L;
        User updatedUserFields =
                new User(id, "koko@ldal.com", "Grisha"
                        , LocalDate.of(1997, 1, 1), "Grisha", null, null);

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(userValid));
        when(userRepository.save(any(User.class))).thenReturn(updatedUserFields);

        User result = userService.updateUser(id, updatedUserFields);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(updatedUserFields, result);
        assertEquals(updatedUserFields.getFirstName(), result.getFirstName());
        assertEquals(updatedUserFields.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(updatedUserFields);

    }

    @Test
    void testGetUsersByBirthDateRange_WrongDate_ReturnUserWrongDateException() {


        LocalDate from = LocalDate.of(3000, 1, 1);
        LocalDate to = LocalDate.of(2000, 1, 1);
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(UserWrongDateException.class, () -> userService.getUsersByBirthDateRange(from, to, pageable));
        assertTrue(from.isAfter(to));

        verify(userRepository, never()).findUsersByBirthDateBetween(from, to, pageable);

    }

    @Test
    void testSearchUsersByBirthDateRange_ValidDate_ReturnListUsers() {

        Pageable pageable = PageRequest.of(0, 10);
        LocalDate from = LocalDate.of(1900, 1, 1);
        LocalDate to = LocalDate.of(2000, 1, 1);
        Page<User> page = new PageImpl<>(Collections.singletonList(userValid));
        when(userRepository.findUsersByBirthDateBetween(from, to, pageable)).thenReturn(page);

        var result = userService.getUsersByBirthDateRange(from, to, pageable);

        assertTrue(result.getTotalElements() > 0);
        assertTrue(result.getContent().get(0).getBirthDate().isBefore(to));
        assertEquals(result.getContent().get(0).getEmail(), page.getContent().get(0).getEmail());
        assertEquals(result.getContent().get(0).getFirstName(), page.getContent().get(0).getFirstName());
        assertEquals(result.getTotalElements(), page.getTotalElements());
        verify(userRepository, times(1)).findUsersByBirthDateBetween(from, to, pageable);

    }
}