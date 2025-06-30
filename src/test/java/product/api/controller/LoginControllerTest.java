package product.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import product.api.dto.AuthRequest;
import product.api.entity.User;
import product.api.filters.JwtFilter;
import product.api.repository.UserRepository;
import product.api.response.JwtTokenResponse;
import product.api.service.CustomUserDetailsService;
import product.api.utils.JwtUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoginControllerTest {


    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private LoginController loginController;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtFilter  jwtFilter;

    @Test
    public void testLoginSuccess(){

        AuthRequest  authRequest = new AuthRequest();
        authRequest.setUsername("test@gmail.com");
        authRequest.setPassword("123456");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("123456");

        UserDetails userDetails =  userDetailsService.loadUserByUsername(authRequest.getUsername());

        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();
        jwtTokenResponse.setToken("token");

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);
        when(userDetailsService.loadUserByUsername(authRequest.getUsername())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails,user)).thenReturn(jwtTokenResponse);

        ResponseEntity<JwtTokenResponse> responseEntity = loginController.login(authRequest);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("token", responseEntity.getBody().getToken());
    }

}
