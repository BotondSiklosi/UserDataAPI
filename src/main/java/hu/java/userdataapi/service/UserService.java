package hu.java.userdataapi.service;

import hu.java.userdataapi.entity.AppUser;
import hu.java.userdataapi.exception.DataIntegrityViolationException;
import hu.java.userdataapi.exception.UserNotFoundException;
import hu.java.userdataapi.model.UserCredentials;
import hu.java.userdataapi.model.enums.Role;
import hu.java.userdataapi.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;

    public AppUser findUserByID(Long id) throws UserNotFoundException {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    public AppUser createUser(AppUser appUser) throws DataIntegrityViolationException {
        if (appUserRepository.existsByEmail(appUser.getEmail())) {
            throw new DataIntegrityViolationException("Email address already in use");
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    //update user if the user already exists and the email address not in use
    public void updateUser(AppUser updatedAppUser) throws UserNotFoundException, DataIntegrityViolationException {
        AppUser oldAppUser = findUserByID(updatedAppUser.getId());
        if (!oldAppUser.getEmail().equals(updatedAppUser.getEmail()) & appUserRepository.existsByEmail(updatedAppUser.getEmail())) {
            throw new DataIntegrityViolationException("Email address already in use");
        }
        if (!updatedAppUser.getPassword().isEmpty()) {
            updatedAppUser.setPassword(passwordEncoder.encode(updatedAppUser.getPassword()));
        }
        appUserRepository.save(updatedAppUser);
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        findUserByID(id);
        appUserRepository.deleteById(id);
    }

    public Map<String, String> AVGUserAge() {
        Double avg = appUserRepository.findAverageAge();

        Map<String, String> resp = new HashMap<>();
        resp.put("NumberOfUsers", String.valueOf(appUserRepository.count()));
        resp.put("AVG Num", avg != null ? avg.toString() : "0.0");
        return resp;
    }

    public List<AppUser> getAllUsersBetweenSpecifiedAge(int minAge, int maxAge) {
        return appUserRepository.findAll().stream()
                .filter(appUser -> appUser.getAge() >= minAge && appUser.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

    public boolean checkIfAlreadyRegisteredAndRegister(UserCredentials userData) {
        if (appUserRepository.findByEmail(userData.getEmail()).isEmpty()) {
            appUserRepository.save(AppUser.builder()
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
