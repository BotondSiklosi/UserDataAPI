package hu.java.userdataapi.security;

import hu.java.userdataapi.entity.AppUser;
import hu.java.userdataapi.exception.UserNotFoundException;
import hu.java.userdataapi.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        AppUser secuUser = appUserRepository
                .findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        return new User(
                secuUser.getName(),
                secuUser.getPassword(),
                secuUser.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList())
        );
    }
}
