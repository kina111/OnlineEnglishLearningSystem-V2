package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.UserRole;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserDTO;
import com.swp391.OnlineEnglishLearningSystem.repository.RoleRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.UserRepository;
import com.swp391.OnlineEnglishLearningSystem.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
Tạo mock data
Định nghĩa hành vi
Gọi method
Kiểm tra kết quả
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void ensureEmailNotExists_whenEmailExists_shouldThrowIllegalArgumentException() {
        // Arrange
        String existingEmail = "existing@gmail.com";
        User existingUser = new User();
        existingUser.setEmail(existingEmail);

        when(userRepository.findByEmail(existingEmail)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.ensureEmailNotExists(existingEmail));
        assertEquals("Email already exists", exception.getMessage());
        //verity
        verify(userRepository, times(1)).findByEmail(existingEmail);
    }

    @Test
    void ensureEmailNotExist_whenEmailDoesNotExist_shouldDoNotThrowException(){
        //arrange
        String newEmail = "new@gmail.com";
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());

        //act & assert
        assertDoesNotThrow(() -> userService.ensureEmailNotExists(newEmail));

        //verity
        verify(userRepository, times(1)).findByEmail(newEmail);
    }


    /*
    @Override
    public Optional<User> findByEmailAndEnabledTrue(String email) {
        return userRepository.findByEmailAndEnabledTrue(email);
    }
     */
    @Test
    void findByEmailAndEnabledTrue_whenEmailExistsAndEnabledTrue_shouldReturnUser(){
        //arrange
        String existingEmail = "existing@gmail.com";
        boolean enabled = true;
        User existingUser = new User();
        existingUser.setEmail(existingEmail);
        existingUser.setEnabled(enabled);

        when(userRepository.findByEmailAndEnabledTrue(existingEmail)).thenReturn(Optional.of(existingUser));

        //act & assert
        Optional<User> user = userService.findByEmailAndEnabledTrue(existingEmail);

        assertTrue(user.isPresent());
        assertEquals(existingUser, user.get());
        assertEquals(existingEmail, user.get().getEmail());

        verify(userRepository, times(1)).findByEmailAndEnabledTrue(existingEmail);
    }

    @Test
    void findByEmailAndEnabledTrue_whenEmailExistsAndEnabledFalse_shouldReturnEmpty(){
        //arrange
        String existingEmail = "existing@gmail.com";

        when(userRepository.findByEmailAndEnabledTrue(existingEmail)).thenReturn(Optional.empty());

        //act & assert
        Optional<User> user = userService.findByEmailAndEnabledTrue(existingEmail);
        assertTrue(user.isEmpty());

        verify(userRepository, times(1)).findByEmailAndEnabledTrue(existingEmail);

    }

    @Test
    void findByEmailAndEnabledTrue_whenEmailNotExistsAndEnabledTrue_shouldReturnEmpty(){
        //arrange
        String notExist = "notExist@gmail.com";

        when(userRepository.findByEmailAndEnabledTrue(notExist)).thenReturn(Optional.empty());

        //act & assert
        Optional<User> user = userService.findByEmailAndEnabledTrue(notExist);
        assertTrue(user.isEmpty());

        verify(userRepository, times(1)).findByEmailAndEnabledTrue(notExist);
    }


    /*
    public User buildNewUser(UserDTO userDTO) {
        UserRole roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Default role not found"));

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFullName(userDTO.getFullName());
        user.setRole(roleUser);
        user.setEnabled(false);
        return user;
    }
     */
    @Test
    void buildNewUser_whenRoleExists_shouldReturnNewUser(){
        //arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@gmail.com");
        userDTO.setPassword("plainPassword");
        userDTO.setFullName("user");

        UserRole roleUser = new UserRole();
        roleUser.setName("ROLE_USER");

        //act & assert
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        User user = userService.buildNewUser(userDTO);
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getFullName(), user.getFullName());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(roleUser, user.getRole());
        assertFalse(user.isEnabled());

        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(passwordEncoder, times(1)).encode("plainPassword");
    }

    @Test
    void buildNewUser_whenRoleNotExists_shouldThrowException(){
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> userService.buildNewUser(new UserDTO()));

        verify(roleRepository, times(1)).findByName("ROLE_USER");
    }

    
}
