package hu.java.userdataapi.service;

import hu.java.userdataapi.entity.AppUser;
import hu.java.userdataapi.exception.DataIntegrityViolationException;
import hu.java.userdataapi.exception.UserNotFoundException;
import hu.java.userdataapi.model.enums.Role;
import hu.java.userdataapi.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<AppUser> appUsers = List.of(
            AppUser.builder()
                    .id(1L)
                    .name("Kovács János")
                    .email("janos.kovacs@example.com")
                    .password("alma1")
                    .phone("+3612345678")
                    .age(15)
                    .address("Budapest, Kossuth tér 1.")
                    .roles(new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN)))
                    .build(),

            AppUser.builder()
                    .id(2L)
                    .name("Nagy Eszter")
                    .email("eszter.nagy@example.com")
                    .password("alma2")
                    .phone("+3623456789")
                    .age(28)
                    .address("Debrecen, Piac utca 5.")
                    .roles(new HashSet<>(Arrays.asList(Role.USER)))
                    .build(),

            AppUser.builder()
                    .id(3L)
                    .name("Tóth Gábor")
                    .email("gabor.toth@example.com")
                    .password("alma3")
                    .phone("+3634567890")
                    .age(45)
                    .address("Szeged, Fő tér 10.")
                    .roles(new HashSet<>(Arrays.asList(Role.USER)))
                    .build(),

            AppUser.builder()
                    .id(4L)
                    .name("Szabó Anna")
                    .email("anna.szabo@example.com")
                    .password("alma4")
                    .phone("+3645678901")
                    .age(22)
                    .address("Pécs, Király utca 15.")
                    .roles(new HashSet<>(Arrays.asList(Role.USER)))
                    .build(),

            AppUser.builder()
                    .id(5L)
                    .name("Horváth Péter")
                    .email("peter.horvath@example.com")
                    .password("alma5")
                    .phone("+3656789012")
                    .age(38)
                    .address("Miskolc, Széchenyi utca 20.")
                    .roles(new HashSet<>(Arrays.asList(Role.ADMIN)))
                    .build()
    );

    private final Long searchId = 1L;
    private final String name = "Siklósi Botond";
    private final String email = "test@email.com";
    private final String password = "test";
    private final int age = 22;

    @Test
    void findUserByID() {
        when(appUserRepository.findById(searchId)).thenReturn(Optional.of(appUsers.get(0)));

        AppUser appUser = userService.findUserByID(searchId);

        assertEquals("Kovács János", appUser.getName());
        verify(appUserRepository).findById(searchId);
    }

    @Test
    void userNotFoundExceptionOnFindUserByID() {
        Long invalidUserId = 999L;
        when(appUserRepository.findById(searchId)).thenReturn(Optional.of(appUsers.get(2)));

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findUserByID(invalidUserId)
        );

        assertEquals("User not found: 999", exception.getMessage());
    }

    @Test
    void createUser() {
        when(appUserRepository.existsByEmail(email)).thenReturn(false);

        AppUser createUser = AppUser.builder()
                .id(6L)
                .name(name)
                .email(email)
                .password(password)
                .age(age)
                .roles(new HashSet<>(Arrays.asList(Role.ADMIN)))
                .build();

        userService.createUser(createUser);
        verify(appUserRepository).save(createUser);
    }

    @Test
    void updateUser() {
        AppUser createUser = AppUser.builder()
                .id(1L)
                .name(name)
                .email(email)
                .password(password)
                .age(age)
                .roles(new HashSet<>(Arrays.asList(Role.ADMIN)))
                .build();

        when(appUserRepository.findById(searchId)).thenReturn(Optional.of(appUsers.get(0)));

        userService.updateUser(createUser);
        verify(appUserRepository).save(createUser);

    }

    @Test
    void deleteUser() {
        when(appUserRepository.findById(searchId)).thenReturn(Optional.of(appUsers.get(0)));
        userService.deleteUser(searchId);
        verify(appUserRepository).deleteById(searchId);
    }

    @Test
    void AVGUserAge() {
        when(appUserRepository.findAverageAge()).thenReturn(28.0);
        assertEquals("28.0", userService.AVGUserAge().get("AVG Num"));
        verify(appUserRepository).findAverageAge();

    }

    @Test
    void getAllUsersBetweenSpecifiedAge() {
        when(appUserRepository.findAll()).thenReturn(appUsers);

        int listSize = userService.getAllUsersBetweenSpecifiedAge(18, 40).size();

        assertEquals(3, listSize);
        verify(appUserRepository).findAll();


    }
}