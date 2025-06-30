package product.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import product.api.dto.AuthRequest;
import product.api.entity.User;
import product.api.repository.UserRepository;
import product.api.response.JwtTokenResponse;
import product.api.service.CustomUserDetailsService;
import product.api.utils.JwtUtil;


@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService userDetailsService;

    private final UserRepository userRepository;

    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService userDetailsService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword()));

        User user = userRepository.findByEmail(request.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("username not found");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        JwtTokenResponse jwtTokenResponse = jwtUtil.generateToken(userDetails,user);

        return ResponseEntity.ok(jwtTokenResponse);
    }
}
