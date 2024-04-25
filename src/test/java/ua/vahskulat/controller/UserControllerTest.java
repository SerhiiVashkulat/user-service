package ua.vahskulat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.vahskulat.model.User;
import ua.vahskulat.service.UserService;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    void setUp(){

       this.testUser = new User(1L,"example@gmail.com","Serhii",
                LocalDate.of(1991,1,13),"Shapoval",null,null);

        testUserDto =  new UserDto("example@gmail.com","Serhii","Shapoval",
                LocalDate.of(1991,1,13),null,null);

        LocalDate from = LocalDate.of(1990,1,1);
        LocalDate to = LocalDate.of(2023,9,30);

        dateRangeDto = new DateRangeDto(from,to);
        pageable = PageRequest.of(0,10);
        list = new ArrayList<>();
        listDto = new ArrayList<>();
        list.add(testUser);
        listDto.add(testUserDto);
        listPageDto = new PageImpl<>(listDto);

        emailDto = new EmailDto("example123@gmail.com");
        pageUser = new PageImpl<>(list,pageable, list.size());

    }
    @Test
    void createUser_ReturnUser200Ok() throws Exception {


        when(userService.createUser(userMapper.toUser(testUserDto))).thenReturn(testUser);

        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(testUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.firstName").value("Serhii"))
                .andExpect(jsonPath("$.data.email").value("example@gmail.com"))
                .andExpect(header().string("Location","/api/v1/users/1"));

        verify(userService, times(1)).createUser(any(User.class));

    }
}