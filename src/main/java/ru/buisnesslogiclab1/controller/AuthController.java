package ru.buisnesslogiclab1.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.buisnesslogiclab1.dto.AuthenticationRequest;
import ru.buisnesslogiclab1.dto.StatusCode;
import ru.buisnesslogiclab1.security.JwtUtil;
import ru.buisnesslogiclab1.util.ResponseHelper;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder encoder;

    private final UserDetailsService userDetailsService;
    private final ResponseHelper responseHelper;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        log.info("got authenticate request for username {} password {}", authenticationRequest.getUsername(), authenticationRequest.getPassword());
        log.info(encoder.encode(authenticationRequest.getPassword()));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return responseHelper.asResponseEntity(StatusCode.createRequestFailedCode("wrong login or password"));
        }


        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}


@Data
class AuthenticationResponse {
    private String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

}

