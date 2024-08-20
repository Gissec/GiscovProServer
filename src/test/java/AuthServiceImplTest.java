import com.example.GiscovAdvancedServer.DTOs.request.AuthRequest;
import com.example.GiscovAdvancedServer.DTOs.request.RegisterUserRequest;
import com.example.GiscovAdvancedServer.DTOs.response.LoginUserResponse;
import com.example.GiscovAdvancedServer.constans.ConstantsTest;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
import com.example.GiscovAdvancedServer.mappers.UserMapper;
import com.example.GiscovAdvancedServer.models.UserEntity;
import com.example.GiscovAdvancedServer.repositories.AuthUserRepository;
import com.example.GiscovAdvancedServer.security.JwtService;
import com.example.GiscovAdvancedServer.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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

    private RegisterUserRequest requestRegister;

    private AuthRequest requestAuth;

    private LoginUserResponse response;

    LoginUserResponse response2;

    @BeforeEach
    void setUp() {
        requestRegister = new RegisterUserRequest();
        requestRegister.setEmail(ConstantsTest.TEST_EMAIL);
        requestRegister.setPassword(ConstantsTest.TEST_PASSWORD);
        requestRegister.setAvatar(ConstantsTest.TEST_AVATAR);
        requestRegister.setName(ConstantsTest.TEST_USER);
        requestRegister.setRole(ConstantsTest.TEST_ROLE);

        requestAuth = new AuthRequest();
        requestAuth.setEmail(ConstantsTest.TEST_EMAIL);
        requestAuth.setPassword(ConstantsTest.TEST_PASSWORD);

        newUser = new UserEntity();
        newUser.setId(UUID.randomUUID());
        newUser.setEmail(requestRegister.getEmail());
        newUser.setAvatar(requestRegister.getAvatar());
        newUser.setRole(requestRegister.getRole());
        newUser.setName(requestRegister.getName());

        response = new LoginUserResponse();
        response.setToken(ConstantsTest.TEST_TOKEN);
        response.setRole(ConstantsTest.TEST_ROLE);
        response.setName(ConstantsTest.TEST_USER);
        response.setId(newUser.getId());
        response.setAvatar(ConstantsTest.TEST_AVATAR);
        response.setEmail(ConstantsTest.TEST_EMAIL);

        response2 = new LoginUserResponse();
        response2.setRole(ConstantsTest.TEST_ROLE);
        response2.setName(ConstantsTest.TEST_USER);
        response2.setId(newUser.getId());
        response2.setAvatar(ConstantsTest.TEST_AVATAR);
        response2.setEmail(ConstantsTest.TEST_EMAIL);
    }

    @Test
    void testRegistrationRequest_Success() {
        when(authUserRepository.existsByEmail(requestRegister.getEmail())).thenReturn(false);
        when(userMapper.userDtoToUserEntity(requestRegister)).thenReturn(newUser);
        when(passwordEncoder.encode(requestRegister.getPassword())).thenReturn(ConstantsTest.TEST_PASSWORD);
        when(jwtService.generateToken(newUser.getId().toString())).thenReturn(ConstantsTest.TEST_TOKEN);
        when(userMapper.userEntityToLogin(newUser)).thenReturn(response2);

        assertEquals(response, authService.registrationRequest(requestRegister));
    }

    @Test
    void testRegistrationRequest_UserAlreadyExists() {
        when(authUserRepository.existsByEmail(requestRegister.getEmail())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () ->
                authService.registrationRequest(requestRegister));

        assertEquals(ServerErrorCodes.USER_ALREADY_EXISTS, exception.getError());
    }

    @Test
    void testLoginRequest_Success() {
        when(authUserRepository.findByEmail(requestAuth.getEmail())).thenReturn(Optional.of(newUser));
        when(passwordEncoder.matches(requestAuth.getPassword(), newUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(newUser.getId().toString())).thenReturn(ConstantsTest.TEST_TOKEN);
        when(userMapper.userEntityToLogin(newUser)).thenReturn(response2);

        assertEquals(response, authService.loginRequest(requestAuth));
    }

    @Test
    void testLoginRequest_InvalidPassword() {
        when(authUserRepository.findByEmail(requestAuth.getEmail())).thenReturn(Optional.of(newUser));
        when(passwordEncoder.matches(requestAuth.getPassword(), newUser.getPassword())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> authService.loginRequest(requestAuth));

        assertEquals(ServerErrorCodes.PASSWORD_NOT_VALID, exception.getError());
    }

    @Test
    void testLoginRequest_UserNotFound() {
        when(authUserRepository.findByEmail(requestAuth.getEmail())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> authService.loginRequest(requestAuth));

        assertEquals(ServerErrorCodes.USER_NOT_FOUND, exception.getError());
    }
}

