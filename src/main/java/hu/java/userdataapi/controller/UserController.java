package hu.java.userdataapi.controller;

import hu.java.userdataapi.entity.AppUser;
import hu.java.userdataapi.exception.DataIntegrityViolationException;
import hu.java.userdataapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody AppUser appUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        try {
            AppUser createdAppUser = userService.createUser(appUser);
            return ResponseEntity.ok(createdAppUser);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Email already exists"));
        }
    }

    @GetMapping("findById/{id}")
    public ResponseEntity<AppUser> findUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserByID(id));
    }

    @PostMapping("/updateUser")
    public ResponseEntity<?> updateUser(@Valid @RequestBody AppUser appUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        userService.updateUser(appUser);
        return ResponseEntity.ok("User updated");
    }

    @GetMapping("/deleteUserById/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User has been deleted successfully");
    }

    @GetMapping("/getUsersAVGAge")
    public ResponseEntity<String> getUsersAVGAge() {
        return ResponseEntity.ok(userService.AVGUserAge().toString());
    }

    @GetMapping("/getUsersBetweenSpecifiedAge")
    public ResponseEntity<List<AppUser>> getUsersBetweenSpecifiedAge(@RequestParam(name = "minAge", defaultValue = "18") int minAge, @RequestParam(name = "maxAge", defaultValue = "40") int maxAge) {
        List<AppUser> resp = userService.getAllUsersBetweenSpecifiedAge(minAge, maxAge);
        return ResponseEntity.ok(resp);
    }

    //Collects the error messages from create new or update user and send them back in the resp
    private ResponseEntity<ProblemDetail> buildValidationErrorResponse(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation error");
        problem.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(problem);
    }

}
