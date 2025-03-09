package org.example.coffeservice.auth.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @Email(message = "Email should be valid")
    @NotNull(message = "Email can't be null")
    private String email;

    @Size(min = 6, message = "Minimum password length is 6 characters")
    private String password;
}

