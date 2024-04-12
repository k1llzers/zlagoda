package org.naukma.zlagoda.security;

import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.employee.EmployeeEntity;
import org.naukma.zlagoda.exception.BadCredentials;
import org.naukma.zlagoda.security.dto.AuthRequest;
import org.naukma.zlagoda.security.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            EmployeeEntity principal = (EmployeeEntity) authentication.getPrincipal();
            jwtService.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(
                    new TokenResponse(jwtService.generateToken(authRequest.getUsername()), principal.getRole()));
        } else {
            throw new BadCredentials("Uncorrect username or password");
        }
    }


    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String manager() {
        return "Welcome for manager";
    }

    @GetMapping("/cashier")
    @PreAuthorize("hasAuthority('ROLE_CASHIER')")
    public String adminProfile() {
        return "Welcome for cashier";
    }
}
