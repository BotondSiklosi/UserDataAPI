package hu.java.userdataapi.entity;

import hu.java.userdataapi.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "app_user")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$", message = "Invalid phone number format")
    @Column(name = "phone")
    private String phone;

    @NotNull(message = "Age is mandatory")
    @Min(value = 14, message = "Age must be at least 14")
    @Max(value = 120, message = "Age must be less than 120")
    @Column(name = "age", nullable = false)
    private int age;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    @Column(name = "address")
    private String address;

    @NotNull(message = "password is mandatory")
    @Column(name = "password", nullable = false)
    private String password;

    @ElementCollection
    @Singular
    @NotEmpty
    @Column(name = "roles", nullable = false)
    private Set<Role> roles;
}
