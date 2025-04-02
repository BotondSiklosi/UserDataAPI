package hu.java.userdataapi.service;

import hu.java.userdataapi.entity.AppUser;
import hu.java.userdataapi.exception.DataIntegrityViolationException;
import hu.java.userdataapi.exception.UserNotFoundException;
import hu.java.userdataapi.model.UserCredentials;
import hu.java.userdataapi.model.enums.Role;
import hu.java.userdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AppUser findUserByID(long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    public AppUser createUser(AppUser appUser) throws DataIntegrityViolationException {
        if (userRepository.existsByEmail(appUser.getEmail())) {
            throw new DataIntegrityViolationException("Email address already in use");
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }

    //update user if the user already exists and the email address not in use
    public void updateUser(AppUser updatedAppUser) throws UserNotFoundException, DataIntegrityViolationException {
        AppUser oldAppUser = findUserByID(updatedAppUser.getId());
        if (!oldAppUser.getEmail().equals(updatedAppUser.getEmail()) & userRepository.existsByEmail(updatedAppUser.getEmail())) {
            throw new DataIntegrityViolationException("Email address already in use");
        }
        if (!updatedAppUser.getPassword().isEmpty()) {
            updatedAppUser.setPassword(passwordEncoder.encode(updatedAppUser.getPassword()));
        }
        userRepository.save(updatedAppUser);
    }

    public void deleteUser(long id) throws UserNotFoundException {
        findUserByID(id);
        userRepository.deleteById(id);
    }

    public String AVGUserAge() {
        Double avg = userRepository.findAverageAge();

        Map<String, String> resp = new HashMap<>();
        resp.put("NumberOfUsers", String.valueOf(userRepository.count()));
        resp.put("AVG Num", avg != null ? avg.toString() : "0.0");
        return resp.toString();
    }

    public List<AppUser> getAllUsersBetweenSpecifiedAge(int minAge, int maxAge) {
        return userRepository.findAll().stream()
                .filter(appUser -> appUser.getAge() >= minAge && appUser.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

    public boolean checkIfAlreadyRegisteredAndRegister(UserCredentials userData) {
        if (userRepository.findByEmail(userData.getEmail()).isEmpty()) {
            userRepository.save(AppUser.builder()
                    .name(userData.getUsername())
                    .email(userData.getEmail())
                    .roles(new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN)))
                    .password(passwordEncoder.encode(userData.getPassword()))
                    .build());
            return false;
        }

        return true;
    }

}
