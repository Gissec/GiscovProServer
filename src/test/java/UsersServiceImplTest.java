import com.example.GiscovAdvancedServer.DTOs.request.PutUserRequest;
import com.example.GiscovAdvancedServer.DTOs.response.PublicUserResponse;
import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
import com.example.GiscovAdvancedServer.mappers.UserMapper;
import com.example.GiscovAdvancedServer.models.UserEntity;
import com.example.GiscovAdvancedServer.repositories.UserRepository;
import com.example.GiscovAdvancedServer.services.TagsService;
import com.example.GiscovAdvancedServer.services.impl.UsersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TagsService tagsService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsersServiceImpl usersService;

    private List<UserEntity> listUsers;

    private List<PublicUserResponse> listPublicUsers;

    private PutUserRequest putUserRequest;

    @BeforeEach
    void setUp() {
        UserEntity user1 = new UserEntity();
        user1.setId(UUID.randomUUID());
        user1.setEmail(Constants.TEST_EMAIL);
        user1.setAvatar(Constants.TEST_AVATAR);
        user1.setRole(Constants.TEST_ROLE);
        user1.setName(Constants.TEST_USER);

        UserEntity user2 = new UserEntity();
        user2.setId(UUID.randomUUID());
        user2.setEmail(Constants.TEST_EMAIL2);
        user2.setAvatar(Constants.TEST_AVATAR);
        user2.setRole(Constants.TEST_ROLE);
        user2.setName(Constants.TEST_USER2);

        listUsers = List.of(user1, user2);
        PublicUserResponse publicUser1 = new PublicUserResponse();
        publicUser1.setId(user1.getId());
        publicUser1.setEmail(user1.getEmail());
        publicUser1.setAvatar(user1.getAvatar());
        publicUser1.setRole(user1.getRole());
        publicUser1.setName(user1.getName());
        PublicUserResponse publicUser2 = new PublicUserResponse();
        publicUser2.setId(user2.getId());
        publicUser2.setEmail(user2.getEmail());
        publicUser2.setAvatar(user2.getAvatar());
        publicUser2.setRole(user2.getRole());
        publicUser2.setName(user2.getName());
        listPublicUsers = List.of(publicUser1, publicUser2);

        putUserRequest = new PutUserRequest();
        putUserRequest.setAvatar(user2.getAvatar());
        putUserRequest.setName(user2.getName());
        putUserRequest.setEmail(user2.getEmail());
        putUserRequest.setRole(user2.getRole());
    }

    @Test
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(listUsers);
        when(userMapper.usersEntityToUserList(listUsers)).thenReturn(listPublicUsers);

        assertEquals(listPublicUsers, usersService.getAllUsers());
    }

    @Test
    void getUserInfoById_Success() {
        when(userRepository.findById(listUsers.get(0).getId())).thenReturn(Optional.of(listUsers.get(0)));
        when(userMapper.userEntityToUser(listUsers.get(0))).thenReturn(listPublicUsers.get(0));

        assertEquals(listPublicUsers.get(0), usersService.getUserInfoById(listUsers.get(0).getId()));
    }

    @Test
    void getUserInfoById_UserNotFound() {
        when(userRepository.findById(listUsers.get(0).getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                usersService.getUserInfoById(listUsers.get(0).getId()));

        assertEquals(ServerErrorCodes.USER_NOT_FOUND, exception.getError());
    }

    @Test
    void getUserInfo_Success() {
        authorization();
        when(userRepository.findById(listUsers.get(0).getId())).thenReturn(Optional.of(listUsers.get(0)));
        when(userMapper.userEntityToUser(listUsers.get(0))).thenReturn(listPublicUsers.get(0));
        assertEquals(listPublicUsers.get(0), usersService.getUserInfo());
    }

    @Test
    void replaceUser_Success() {
        authorization();
        when(userRepository.findById(listUsers.get(0).getId())).thenReturn(Optional.of(listUsers.get(0)));
        when(userRepository.existsByEmail(putUserRequest.getEmail())).thenReturn(false);
        PublicUserResponse putUserResponse = new PublicUserResponse();
        putUserResponse.setAvatar(putUserRequest.getAvatar());
        putUserResponse.setEmail(putUserRequest.getEmail());
        putUserResponse.setId(listUsers.get(0).getId());
        putUserResponse.setRole(putUserRequest.getRole());
        putUserResponse.setName(putUserRequest.getName());
        when(userMapper.userEntityToUser(listUsers.get(0))).thenReturn(putUserResponse);

        assertEquals(putUserResponse, usersService.replaceUser(putUserRequest));
    }


    @Test
    void replaceUser_UserExists() {
        authorization();
        when(userRepository.findById(listUsers.get(0).getId())).thenReturn(Optional.of(listUsers.get(0)));
        when(userRepository.existsByEmail(putUserRequest.getEmail())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () ->
                usersService.replaceUser(putUserRequest));

        assertEquals(ServerErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST, exception.getError());
    }

    @Test
    void deleteUser_Success() {
        authorization();
        when(userRepository.findById(listUsers.get(0).getId())).thenReturn(Optional.of(listUsers.get(0)));
        doNothing().when(tagsService).deleteTags();
        usersService.deleteUser();
        verify(tagsService, times(1)).deleteTags();
    }

    private void authorization() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()
                .getName()).thenReturn(listUsers.get(0).getId().toString());
    }
}
