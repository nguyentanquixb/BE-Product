package product.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import product.api.entity.User;
import product.api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void loadUserByUsername_SuccessTest() {

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("123456");

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@gmail.com");

        assertEquals("test@gmail.com", userDetails.getUsername());
        assertEquals("123456", userDetails.getPassword());
    }

    @Test
    public void loadUserByUsername_NotFoundTest() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("test@gmail.com"));

    }
}