package ua.vahskulat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.vahskulat.dto.request.UserCreateRequestDTO;
import ua.vahskulat.dto.request.UserUpdateRequestDto;
import ua.vahskulat.dto.response.UserResponseDto;
import ua.vahskulat.exception.UserEmailExistException;
import ua.vahskulat.exception.UserNotFoundException;
import ua.vahskulat.exception.UserWrongAgeException;
import ua.vahskulat.exception.UserWrongDateException;
import ua.vahskulat.model.User;
import ua.vahskulat.service.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    private User testUser;
    private UserResponseDto userResponse;

    @BeforeEach
    void setUp() {

        this.testUser = new User(1L, "example@gmail.com", "Serhii",
                LocalDate.of(1991, 1, 13), "Shapoval", null, null);

        this.userResponse = new UserResponseDto("example@gmail.com", "Serhii", "Shapoval",
                LocalDate.of(1991, 1, 13), null, null);

        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2023, 9, 30);


    }

    @Test
    void createUser_ReturnUser200Ok() throws Exception {


        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(userResponse))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.firstName").value("Serhii"))
                .andExpect(jsonPath("$.data.email").value("example@gmail.com"))
                .andExpect(header().string("Location", "/api/v1/users/1"));

        verify(userService, times(1)).createUser(any(User.class));

    }

    @Test
    void createUser_ByExistEmail_ReturnErrorResponseBadRequest409() throws Exception {

        UserCreateRequestDTO existEmail = new UserCreateRequestDTO("example@gmail.com", "Grisha", "Privet",
                LocalDate.of(1996, 5, 16), null, null);

        when(userService.createUser(any(User.class)))
                .thenThrow(new UserEmailExistException(" This email example@gmail.com is busy "));

        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(existEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" User email exist "))
                .andExpect(jsonPath("$.detail").value(" This email " + existEmail.email() + " is busy "))
                .andExpect(jsonPath("$.path").value("/api/v1/users"));

        verify(userService, times(1)).createUser(argThat(user ->

                "example@gmail.com".equals(existEmail.email())));

    }

    @Test
    void createUser_ByWrongDate_ReturnErrorResponseBedRequest400() throws Exception {

        UserCreateRequestDTO wrongDateUser = new UserCreateRequestDTO("example@gmail.com", "Grisha", "Privet",
                LocalDate.of(2026, 5, 16), null, null);

        when(userService.createUser(any(User.class)))
                .thenThrow(new UserWrongDateException(" Birth date must be earlier than current date "));


        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(wrongDateUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Wrong date "))
                .andExpect(jsonPath("$.detail").value(" Birth date must be earlier than current date "))
                .andExpect(jsonPath("$.path").value("/api/v1/users"));

        verify(userService, times(1)).createUser(argThat(user ->
                LocalDate.of(2026, 5, 16).equals(user.getBirthDate())));
    }

    @Test
    void createUser_ByWrongDate_ReturnErrorResponseUnprocessableEntity422() throws Exception {

        UserCreateRequestDTO wrongAgeUser = new UserCreateRequestDTO("example@gmail.com", "Grisha", "Privet",
                LocalDate.of(2019, 5, 16), null, null);

        when(userService.createUser(any(User.class)))
                .thenThrow(new UserWrongAgeException(" User must be at least 18 years old "));


        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(wrongAgeUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Wrong age "))
                .andExpect(jsonPath("$.detail").value(" User must be at least 18 years old "))
                .andExpect(jsonPath("$.path").value("/api/v1/users"));
        verify(userService, times(1)).createUser(argThat(user ->
                LocalDate.of(2019, 5, 16).equals(wrongAgeUser.birthDate())));
    }

    @Test
    void createUser_ByEmptyEmail_ReturnValidationErrorResponseBadRequest400() throws Exception {
        UserCreateRequestDTO wrongEmail = new UserCreateRequestDTO("", "Grisha", "Privet",
                LocalDate.of(2019, 5, 16), null, null);


        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(wrongEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Validation error "))
                .andExpect(jsonPath("$.path").value("/api/v1/users"));
        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    void updateUserNames_Return200Ok() throws Exception {
        Long id = 1L;

        User user = new User(1L, "example@gmail.com", "updateFN",
                LocalDate.of(1991, 1, 13), "updateLN", null, null);
        UserUpdateRequestDto updatedUser = new UserUpdateRequestDto("updateFN", "updateLN");

        when(userService.updateUserNames(id, updatedUser.firstName(), updatedUser.lastName()))
                .thenReturn(user);

        mockMvc.perform(patch("/api/v1/users/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.firstName").value(updatedUser.firstName()))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id));
        verify(userService, times(1))
                .updateUserNames(id,updatedUser.firstName(),updatedUser.lastName());


    }
    @Test
    void updateUserNames_ByIllegalId_ReturnErrorResponseBadRequest400() throws Exception {

        Long id = -1L;
        UserUpdateRequestDto updatedUser = new UserUpdateRequestDto("updateFN", "updateLN");
        when(userService.updateUserNames(id,updatedUser.firstName(),updatedUser.lastName()))
                .thenThrow(new IllegalArgumentException("User ID must be greater than 0"));

        mockMvc.perform(patch("/api/v1/users/{id}",id)
                        .content(objectMapper.writeValueAsString(updatedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Invalid query parameter"))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id ));

        verify(userService, never())
                .updateUserNames(id,updatedUser.firstName(),updatedUser.lastName());

    }
    @Test
    void updateUserNames_ByInvalidID_ReturnErrorResponseNotFound404() throws Exception {

        Long id = 100L;
        UserUpdateRequestDto updatedUser = new UserUpdateRequestDto("updateFN", "updateLN");

        when(userService.updateUserNames(id,updatedUser.firstName(),updatedUser.lastName()))
                .thenThrow(new UserNotFoundException(" User with this " + id + " not found "));

        mockMvc.perform(patch("/api/v1/users/{id}",id)
                        .content(objectMapper.writeValueAsString(updatedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" User not found "))
                .andExpect(jsonPath("$.detail").value(" User with this " + id + " not found "))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id ));

        verify(userService, times(1))
                .updateUserNames(id,updatedUser.firstName(),updatedUser.lastName());

    }
    @Test
    void updateUserNames_ByInvalidNames_ReturnErrorResponseBadRequest400() throws Exception {

        Long id = 1L;
        UserUpdateRequestDto updatedUser = new UserUpdateRequestDto(" ", "updateLN");

        mockMvc.perform(patch("/api/v1/users/{id}",id)
                        .content(objectMapper.writeValueAsString(updatedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Validation error "))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id ));

        verify(userService, never())
                .updateUserNames(id,updatedUser.firstName(),updatedUser.lastName());

    }
    @Test
    void updateAllFieldsUser_ReturnUserResponseOk200() throws Exception {
        Long id = 1L;
        User updatedUser = new User(1L,"exampl1e@gmail.com", "Serega",
                LocalDate.of(1991, 11, 15), "Vashkulat", null, "123-123-123");

        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO("exampl1e@gmail.com", "Serega",
                 "Vashkulat", LocalDate.of(1991, 11, 15),null, "123-123-123");

        when(userService.updateUser(eq(id),any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/users/{id}", id)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.firstName").value("Serega"))
                .andExpect(jsonPath("$.data.lastName").value("Vashkulat"))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id));
        verify(userService,times(1)).updateUser(eq(id),any(User.class));

    }
    @Test
    void updateAllFieldsUser_ByExistEmail_ReturnUserErrorResponseConflict409() throws Exception {
        Long id = 1L;
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO("exampl1e@gmail.com", "Serega",
                "Vashkulat", LocalDate.of(1991, 11, 15),null, "123-123-123");

        when(userService.updateUser(eq(id),any(User.class)))
                .thenThrow(new UserEmailExistException(" This email " + requestDTO.email() + " is busy "));

        mockMvc.perform(put("/api/v1/users/{id}", id)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" User email exist "))
                .andExpect(jsonPath("$.detail").value(" This email " + requestDTO.email() + " is busy "))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id));
        verify(userService,times(1)).updateUser(eq(id),argThat(user ->
                "exampl1e@gmail.com".equals(requestDTO.email())));
    }
    @Test
    void updateAllFieldsUser_ByInvalidEmail_ReturnUserErrorResponseBadRequest400() throws Exception {
        Long id = 1L;
        UserCreateRequestDTO requestDTO = new UserCreateRequestDTO("exampl1e", "Serega",
                "Vashkulat", LocalDate.of(1991, 11, 15),null, "123-123-123");

        mockMvc.perform(put("/api/v1/users/{id}", id)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Validation error "))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id));
        verify(userService,never()).updateUser(any(Long.class),any(User.class));
    }
    @Test
    void deleteUser_ReturnNoContent204() throws Exception {

        Long id = 1L;

        mockMvc.perform(delete("/api/v1/users/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }
    @Test
    void deleteUser_ByInvalidId_ReturnUserErrorResponseNotFound404() throws Exception {

        Long id = 5L;
        doThrow(new UserNotFoundException(" User with this " + id + " not found ")).when(userService).deleteUser(id);
        mockMvc.perform(delete("/api/v1/users/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(" User not found "))
                .andExpect(jsonPath("$.detail").value(" User with this " + id + " not found "))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id ));
        verify(userService,times(1)).deleteUser(id);

    }
    @Test
    void searchUsersByDateRange_ReturnUserResponseAPIOk200() throws Exception {
        Pageable pageable = PageRequest.of(0,10);
        LocalDate from = LocalDate.of(1990,1,1);
        LocalDate to = LocalDate.of(2023,9,30);
        List<UserResponseDto> list = Collections.singletonList(userResponse);
        Page<User> pageUser = new PageImpl<>(Collections.singletonList(testUser));
        when(userService.getUsersByBirthDateRange(from,to, pageable))
                .thenReturn(pageUser);


        mockMvc.perform(get("/api/v1/users")
                        .param("fromDate", from.toString())
                        .param("toDate", to.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].email").value("example@gmail.com"))
                .andExpect(jsonPath("$.data.[0].firstName").value("Serhii"));
        verify(userService,times(1))
                .getUsersByBirthDateRange(from,to,pageable);

    }
    @Test
    void searchUsersByDateRange_ByWrongDate_ReturnUserErrorResponseBadRequest400() throws Exception {
        Pageable pageable = PageRequest.of(0,10);
        LocalDate wrongFrom = LocalDate.of(3000,1,1);
        LocalDate to = LocalDate.of(2023,9,30);

        when(userService.getUsersByBirthDateRange(wrongFrom,to,pageable))
                .thenThrow((new UserWrongDateException(" From date "  + wrongFrom.toString() + " must be before "
                        + to.toString())));

        mockMvc.perform(get("/api/v1/users")
                        .param("fromDate", wrongFrom.toString())
                        .param("toDate", to.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(" Wrong date "))
                .andExpect(jsonPath("$.path").value("/api/v1/users"));
        verify(userService,times(1))
                .getUsersByBirthDateRange(wrongFrom,to,pageable);
    }
    @Test
    void searchUsersByDateRange_ByInvalidDate_ReturnUserErrorResponseBadRequest400() throws Exception {

        LocalDate to = LocalDate.of(2023,9,30);

        mockMvc.perform(get("/api/v1/users")
                        .param("fromDate", " ")
                        .param("toDate", to.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid query parameter"))
                .andExpect(jsonPath("$.path").value("/api/v1/users"));
        verify(userService,never())
                .getUsersByBirthDateRange(any(LocalDate.class),any(LocalDate.class),any(Pageable.class) );

    }

}