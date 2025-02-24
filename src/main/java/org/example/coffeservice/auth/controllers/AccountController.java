package org.example.coffeservice.auth.controllers;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.validation.Valid;
import org.example.coffeservice.auth.models.LoginDto;
import org.example.coffeservice.auth.models.RegisterDto;
import org.example.coffeservice.models.User;
import org.example.coffeservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    @Autowired
    private UserRepository appUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/profile")
    public ResponseEntity<Object> profile(Authentication auth) {
        var response = new HashMap<String, Object>();
        response.put("Username", auth.getName());
        response.put("Authorities", auth.getAuthorities());

        var appUser = appUserRepository.findByEmail(auth.getName());
        response.put("User", appUser);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterDto registerDto, BindingResult result) {
        if (result.hasErrors()) {
            var errorList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();

            for (int i = 0; i < errorList.size(); i++) {
                var error = (FieldError) errorList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errorsMap);
        }

        var bCryptEncoder = new BCryptPasswordEncoder();
        User appUser = new User();
        appUser.setFirstName(registerDto.getFirstName());
        appUser.setPhone(registerDto.getPhone());
        appUser.setLastName(registerDto.getLastName());
        appUser.setEmail(registerDto.getEmail());
        appUser.setRole("VISITOR");
        appUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));


        try {
            var otherUser = appUserRepository.findByEmail(registerDto.getEmail());
            if (otherUser != null) {
                return ResponseEntity.badRequest().body("Email address already used");
            }

            appUserRepository.save(appUser);

            String jwtToken = createJwtToken(appUser);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("user", appUser);

            return ResponseEntity.ok(response);


        } catch (Exception ex) {
            System.out.println("There is an Exception :");
            ex.printStackTrace();
        }

        return ResponseEntity.badRequest().body("Error");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDto loginDto, BindingResult result) {
        if (result.hasErrors()) {
            var errorList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();

            for (int i = 0; i < errorList.size(); i++) {
                var error = (FieldError) errorList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errorsMap);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

            User appUser = appUserRepository.findByEmail(loginDto.getEmail());
            String jwtToken = createJwtToken(appUser);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("user", appUser);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            System.out.println("There is an Exception :");
            ex.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Bad username or password");
    }



    private String createJwtToken(User appUser) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(24 * 3600))
                .subject(appUser.getEmail())
                .claim("role", appUser.getRole())
                .build();

        var encoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(jwtSecretKey.getBytes())
        );
        var params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims
        );

        return encoder.encode(params).getTokenValue();
    }
}

