package hu.java.userdataapi.init;

import hu.java.userdataapi.entity.User;
import hu.java.userdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Initializer {

    private final UserRepository userRepository;


    @Bean
    public CommandLineRunner afterInit() {

        return args -> {
            List<User> users = List.of(
                    User.builder()
                            .name("Kovács János")
                            .email("janos.kovacs@example.com")
                            .phone("+3612345678")
                            .age(15)
                            .address("Budapest, Kossuth tér 1.")
                            .build(),

                    User.builder()
                            .name("Nagy Eszter")
                            .email("eszter.nagy@example.com")
                            .phone("+3623456789")
                            .age(28)
                            .address("Debrecen, Piac utca 5.")
                            .build(),

                    User.builder()
                            .name("Tóth Gábor")
                            .email("gabor.toth@example.com")
                            .phone("+3634567890")
                            .age(45)
                            .address("Szeged, Fő tér 10.")
                            .build(),

                    User.builder()
                            .name("Szabó Anna")
                            .email("anna.szabo@example.com")
                            .phone("+3645678901")
                            .age(22)
                            .address("Pécs, Király utca 15.")
                            .build(),

                    User.builder()
                            .name("Horváth Péter")
                            .email("peter.horvath@example.com")
                            .phone("+3656789012")
                            .age(38)
                            .address("Miskolc, Széchenyi utca 20.")
                            .build()
            );

            userRepository.saveAll(users);
        };

    }

}
