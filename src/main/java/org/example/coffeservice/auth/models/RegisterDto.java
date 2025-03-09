package org.example.coffeservice.auth.models;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.coffeservice.utils.validation.ValidPhone;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "Firstname can't be blank")
    private String firstName;

    @NotBlank(message = "Lastname can't be blank")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email can't be null")
    private String email;

    @ValidPhone
    private String phone;

    @NotEmpty
    @Size(min = 6, message = "Minimum password length is 6 characters")
    private String password;
}
