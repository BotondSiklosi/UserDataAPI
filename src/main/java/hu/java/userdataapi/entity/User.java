package hu.java.userdataapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(name = "email",unique = true)
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$", message = "Invalid phone number format")
    @Column(name = "phone")
    private String phone;

    @NotNull(message = "Age is mandatory")
    @Min(value = 14, message = "Age must be at least 14")
    @Max(value = 120, message = "Age must be less than 120")
    @Column(name = "age")
    private int age;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    @Column(name = "address")
    private String address;
}
