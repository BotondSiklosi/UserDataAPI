package hu.java.userdataapi.service;

import hu.java.userdataapi.entity.User;
import hu.java.userdataapi.exception.DataIntegrityViolationException;
import hu.java.userdataapi.exception.UserNotFoundException;
import hu.java.userdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserByID(long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    public User createUser(User user) throws DataIntegrityViolationException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DataIntegrityViolationException("Email address already in use");
        }
        return userRepository.save(user);
    }

    //update user if the user already exists and the email address not in use
    public void updateUser(User updatedUser) throws UserNotFoundException, DataIntegrityViolationException {
         User oldUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + updatedUser.getId()));
        if (!oldUser.getEmail().equals(updatedUser.getEmail()) & userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new DataIntegrityViolationException("Email address already in use");
        }
        userRepository.save(updatedUser);
    }

    public void deleteUser(long id) throws UserNotFoundException {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        userRepository.deleteById(id);
    }

    public String AVGUserAge() {
        Double avg = userRepository.findAverageAge();

        Map<String, String> resp = new HashMap<>();
        resp.put("NumberOfUsers", String.valueOf(userRepository.count()));
        resp.put("AVG Num", avg != null ? avg.toString() : "0.0");
        return resp.toString();
    }

    public List<User> getAllUsersBetweenSpecifiedAge(int minAge, int maxAge) {
        return userRepository.findAll().stream()
                .filter(user -> user.getAge() >= minAge && user.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

}
