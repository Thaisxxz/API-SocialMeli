package com.social.meli.service;

import com.social.meli.dto.user.LoginDTO;
import com.social.meli.dto.user.UserCreateDTO;
import com.social.meli.exception.user.EmailAlreadyExistsException;
import com.social.meli.exception.user.InvalidPasswordException;
import com.social.meli.exception.user.NicknameAlreadyExistsException;
import com.social.meli.exception.user.NicknameNotFoundException;
import com.social.meli.model.User;
import com.social.meli.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks UserService userService;

    @Test
    void createUser_shouldCreateWhenEmailAndNicknameAreFree() {
        var dto = new UserCreateDTO();
        dto.setEmail("a@a.com");
        dto.setName("Ana");
        dto.setNickname("nickname");
        dto.setPhone("11 99999-9999");
        dto.setPassword("123456");
        dto.setConfirmPassword("123456");

        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.empty());
        when(userRepository.findByNickname("nickname")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("ENC(123456)");

        var saved = new User();
        saved.setId(1L);
        saved.setEmail(dto.getEmail());
        saved.setNickname(dto.getNickname());
        saved.setPhone(dto.getPhone());
        saved.setPassword("ENC(123456)");

        when(userRepository.save(any(User.class))).thenReturn(saved);

        var result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("nickname", result.getNickname());
        assertEquals("a@a.com", result.getEmail());
        assertEquals("11 99999-9999", result.getPhone());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("ENC(123456)", captor.getValue().getPassword());

        verify(passwordEncoder).encode("123456");
    }

    @Test
    void createUser_shouldThrowWhenEmailAlreadyExists() {
        var dto = new UserCreateDTO();
        dto.setEmail("a@a.com");
        dto.setNickname("nickname");

        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(dto));

        verify(userRepository, never()).save(any());
        verify(userRepository, never()).findByNickname(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void createUser_shouldThrowWhenNicknameAlreadyExists() {
        var dto = new UserCreateDTO();
        dto.setEmail("a@a.com");
        dto.setNickname("nickname");

        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.empty());
        when(userRepository.findByNickname("nickname")).thenReturn(Optional.of(new User()));

        assertThrows(NicknameAlreadyExistsException.class, () -> userService.createUser(dto));

        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void findById_shouldReturnUserWhenExists() {
        var user = new User();
        user.setId(10L);
        user.setEmail("a@a.com");
        user.setNickname("nickname");
        user.setPhone("11 99999-9999");

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        var result = userService.findById(10L);

        assertEquals(10L, result.getId());
        assertEquals("nickname", result.getNickname());
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(userRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.findById(10L));
    }

    @Test
    void login_shouldReturnUserWhenPasswordMatches() {
        var user = new User();
        user.setId(1L);
        user.setNickname("nickname");
        user.setPassword("ENC(123456)");
        user.setEmail("a@a.com");
        user.setPhone("11 99999-9999");

        var dto = new LoginDTO();
        dto.setNickname("nickname");
        dto.setPassword("123456");

        when(userRepository.findByNickname("nickname")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "ENC(123456)")).thenReturn(true);

        var result = userService.login(dto);

        assertEquals(1L, result.getId());
        assertEquals("nickname", result.getNickname());
        verify(passwordEncoder).matches("123456", "ENC(123456)");
    }

    @Test
    void login_shouldThrowWhenNicknameNotFound() {
        var dto = new LoginDTO();
        dto.setNickname("nickname");
        dto.setPassword("123456");

        when(userRepository.findByNickname("nickname")).thenReturn(Optional.empty());

        assertThrows(NicknameNotFoundException.class, () -> userService.login(dto));
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void login_shouldThrowWhenPasswordIsInvalid() {
        var user = new User();
        user.setNickname("nickname");
        user.setPassword("ENC(123456)");

        var dto = new LoginDTO();
        dto.setNickname("nickname");
        dto.setPassword("wrongpw");

        when(userRepository.findByNickname("nickname")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpw", "ENC(123456)")).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userService.login(dto));
    }

    @Test
    void findAll_shouldMapAllUsers() {
        var u1 = new User();
        u1.setId(1L); u1.setNickname("nick001"); u1.setEmail("1@a.com"); u1.setPhone("11 99999-9999");
        var u2 = new User();
        u2.setId(2L); u2.setNickname("nick002"); u2.setEmail("2@a.com"); u2.setPhone("11 98888-8888");

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        var result = userService.findAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }
}