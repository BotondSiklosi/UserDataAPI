package hu.java.userdataapi.init;

import hu.java.userdataapi.entity.AppUser;
import hu.java.userdataapi.model.enums.Role;
import hu.java.userdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Initializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner afterInit() {

        return args -> {
            List<AppUser> appUsers = List.of(
                    AppUser.builder()
                            .name("Kovács János")
                            .email("janos.kovacs@example.com")
                            .password(passwordEncoder.encode("alma1"))
                            .phone("+3612345678")
                            .age(15)
                            .address("Budapest, Kossuth tér 1.")
                            .roles( new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN)))
                            .build(),

                    AppUser.builder()
                            .name("Nagy Eszter")
                            .email("eszter.nagy@example.com")
                            .password(passwordEncoder.encode("alma2"))
                            .phone("+3623456789")
                            .age(28)
                            .address("Debrecen, Piac utca 5.")
                            .roles( new HashSet<>(Arrays.asList(Role.USER)))
                            .build(),

                    AppUser.builder()
                            .name("Tóth Gábor")
                            .email("gabor.toth@example.com")
                            .password(passwordEncoder.encode("alma3"))
                            .phone("+3634567890")
                            .age(45)
                            .address("Szeged, Fő tér 10.")
                            .roles( new HashSet<>(Arrays.asList(Role.USER)))
                            .build(),

                    AppUser.builder()
                            .name("Szabó Anna")
                            .email("anna.szabo@example.com")
                            .password(passwordEncoder.encode("alma4"))
                            .phone("+3645678901")
                            .age(22)
                            .address("Pécs, Király utca 15.")
                            .roles( new HashSet<>(Arrays.asList(Role.USER)))
                            .build(),

                    AppUser.builder()
                            .name("Horváth Péter")
                            .email("peter.horvath@example.com")
                            .password(passwordEncoder.encode("alma5"))
                            .phone("+3656789012")
                            .age(38)
                            .address("Miskolc, Széchenyi utca 20.")
                            .roles( new HashSet<>(Arrays.asList(Role.ADMIN)))
                            .build()
            );

            userRepository.saveAll(appUsers);
        };

    }

}
