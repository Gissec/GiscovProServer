import com.example.GiscovAdvancedServer.DTOs.request.AuthRequest;
import com.example.GiscovAdvancedServer.DTOs.request.RegisterUserRequest;
import com.example.GiscovAdvancedServer.DTOs.response.LoginUserResponse;
import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
import com.example.GiscovAdvancedServer.mappers.UserMapper;
import com.example.GiscovAdvancedServer.models.UserEntity;
import com.example.GiscovAdvancedServer.repositories.AuthUserRepository;
import com.example.GiscovAdvancedServer.security.JwtService;
import com.example.GiscovAdvancedServer.services.impl.AuthServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserEntity newUser;

    private RegisterUserRequest request1;

    private AuthRequest request2;

    @BeforeEach
    void setUp() {
        request1 = new RegisterUserRequest();
        request1.setEmail(Constants.TEST_EMAIL);
        request1.setPassword(Constants.TEST_PASSWORD);
        request1.setAvatar(Constants.TEST_AVATAR);
        request1.setName(Constants.TEST_USER);
        request1.setRole(Constants.TEST_ROLE);
        request2 = new AuthRequest();
        request2.setEmail(Constants.TEST_EMAIL);
        request2.setPassword(Constants.TEST_PASSWORD);
        newUser = new UserEntity();
        newUser.setId(UUID.randomUUID());
        newUser.setEmail(request1.getEmail());
        newUser.setAvatar(request1.getAvatar());
        newUser.setRole(request1.getRole());
        newUser.setName(request1.getName());
    }

    @Test
    void testRegistrationRequest_Success() {
        LoginUserResponse response = new LoginUserResponse();
        response.setToken(Constants.TEST_TOKEN);
        response.setRole(Constants.TEST_ROLE);
        response.setName(Constants.TEST_USER);
        response.setId(newUser.getId());
        response.setAvatar(Constants.TEST_AVATAR);
        response.setEmail(Constants.TEST_EMAIL);

        LoginUserResponse response2 = new LoginUserResponse();
        response2.setRole(Constants.TEST_ROLE);
        response2.setName(Constants.TEST_USER);
        response2.setId(newUser.getId());
        response2.setAvatar(Constants.TEST_AVATAR);
        response2.setEmail(Constants.TEST_EMAIL);

        when(authUserRepository.existsByEmail(request1.getEmail())).thenReturn(false);
        when(userMapper.userDtoToUserEntity(request1)).thenReturn(newUser);
        when(passwordEncoder.encode(request1.getPassword())).thenReturn(Constants.TEST_PASSWORD);
        when(jwtService.generateToken(newUser.getId().toString())).thenReturn(Constants.TEST_TOKEN);
        when(userMapper.userEntityToLogin(newUser)).thenReturn(response2);

        assertEquals(response, authService.registrationRequest(request1));
    }

    @Test
    void testRegistrationRequest_UserAlreadyExists() {
        when(authUserRepository.existsByEmail(request1.getEmail())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () ->
                authService.registrationRequest(request1));

        assertEquals(ServerErrorCodes.USER_ALREADY_EXISTS, exception.getError());
    }

    @Test
    void testLoginRequest_Success() {
        LoginUserResponse response = new LoginUserResponse();
        response.setToken(Constants.TEST_TOKEN);
        response.setRole(Constants.TEST_ROLE);
        response.setName(Constants.TEST_USER);
        response.setId(newUser.getId());
        response.setAvatar(Constants.TEST_AVATAR);
        response.setEmail(Constants.TEST_EMAIL);

        LoginUserResponse response2 = new LoginUserResponse();
        response2.setRole(Constants.TEST_ROLE);
        response2.setName(Constants.TEST_USER);
        response2.setId(newUser.getId());
        response2.setAvatar(Constants.TEST_AVATAR);
        response2.setEmail(Constants.TEST_EMAIL);

        when(authUserRepository.findByEmail(request2.getEmail())).thenReturn(Optional.of(newUser));
        when(passwordEncoder.matches(request2.getPassword(), newUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(newUser.getId().toString())).thenReturn(Constants.TEST_TOKEN);
        when(userMapper.userEntityToLogin(newUser)).thenReturn(response2);

        assertEquals(response, authService.loginRequest(request2));
    }

    @Test
    void testLoginRequest_InvalidPassword() {
        when(authUserRepository.findByEmail(request2.getEmail())).thenReturn(Optional.of(newUser));
        when(passwordEncoder.matches(request2.getPassword(), newUser.getPassword())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> authService.loginRequest(request2));

        assertEquals(ServerErrorCodes.PASSWORD_NOT_VALID, exception.getError());
    }

    @Test
    void testLoginRequest_UserNotFound() {
        when(authUserRepository.findByEmail(request2.getEmail())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> authService.loginRequest(request2));

        assertEquals(ServerErrorCodes.USER_NOT_FOUND, exception.getError());
    }
}

