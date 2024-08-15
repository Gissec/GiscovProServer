import com.example.GiscovAdvancedServer.DTOs.request.NewsRequest;
import com.example.GiscovAdvancedServer.DTOs.response.GetNewsOutResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PageableResponse;
import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
import com.example.GiscovAdvancedServer.mappers.NewsMapper;
import com.example.GiscovAdvancedServer.models.NewsEntity;
import com.example.GiscovAdvancedServer.models.TagsEntity;
import com.example.GiscovAdvancedServer.models.UserEntity;
import com.example.GiscovAdvancedServer.repositories.NewsRepository;
import com.example.GiscovAdvancedServer.repositories.UserRepository;
import com.example.GiscovAdvancedServer.services.TagsService;
import com.example.GiscovAdvancedServer.services.UsersService;
import com.example.GiscovAdvancedServer.services.impl.NewsServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class NewsServiceImplTest {
    @Mock
    private UsersService userService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagsService tagsService;

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    private NewsRequest newsRequest;

    private NewsEntity newsEntity;

    private UserEntity currentUser;

    private Set<TagsEntity> tagsEntities;

    private List<NewsEntity> newsEntities;

    private List<GetNewsOutResponse> getNewsOutResponses;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных
        newsRequest = new NewsRequest();
        newsRequest.setDescription(Constants.TEST_DESCRIPTION);
        newsRequest.setImage(Constants.TEST_AVATAR);
        newsRequest.setTitle(Constants.TEST_TITLE);
        newsRequest.setTags(List.of(Constants.TEST_TAG1, Constants.TEST_TAG2));

        currentUser = new UserEntity();
        currentUser.setId(UUID.fromString(Constants.TEST_UUID1));
        currentUser.setEmail(Constants.TEST_EMAIL);
        currentUser.setName(Constants.TEST_USER);
        currentUser.setPassword(Constants.TEST_PASSWORD);

        TagsEntity tag1 = new TagsEntity();
        tag1.setTitle(Constants.TEST_TAG1);
        TagsEntity tag2 = new TagsEntity();
        tag2.setTitle(Constants.TEST_TAG2);

        tagsEntities = new HashSet<>(Set.of(tag1, tag2));

        newsEntity = new NewsEntity();
        newsEntity.setId(1L);
        newsEntity.setDescription(newsRequest.getDescription());
        newsEntity.setImage(newsRequest.getImage());
        newsEntity.setTitle(newsRequest.getTitle());
        newsEntity.setTags(tagsEntities);
        newsEntity.setUser(currentUser);

        newsEntities = List.of(newsEntity, newsEntity);
        tag1.setNews(Set.of(newsEntity));
        tag2.setNews(Set.of(newsEntity));

        GetNewsOutResponse getNewsOutResponse1 = new GetNewsOutResponse();
        getNewsOutResponse1.setId(newsEntity.getId());
        getNewsOutResponse1.setDescription(newsEntity.getDescription());
        getNewsOutResponse1.setImage(newsEntity.getImage());
        getNewsOutResponse1.setTitle(newsEntity.getTitle());
        getNewsOutResponse1.setUserId(newsEntity.getUser().getId());
        getNewsOutResponse1.setUsername(newsEntity.getUser().getName());

        getNewsOutResponses = List.of(getNewsOutResponse1, getNewsOutResponse1);
    }

    @Test
    void createNews_success() {
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(tagsService.findOrCreateTags(new ArrayList<>(newsRequest.getTags()))).thenReturn(tagsEntities);
        when(newsMapper.newsRequestToEntity(newsRequest)).thenReturn(newsEntity);
        when(newsRepository.save(newsEntity)).thenReturn(newsEntity);

        Long newsId = newsService.createNews(newsRequest);

        assertEquals(1L, newsId);
        verify(newsMapper).newsRequestToEntity(newsRequest);
        verify(newsRepository).save(newsEntity);
        verify(userService).getCurrentUser();
        verify(tagsService).findOrCreateTags(new ArrayList<>(newsRequest.getTags()));
    }

    @Test
    void getNews_Success() {
        int page = 1;
        int perPage = 2;
        Pageable pageable = PageRequest.of(page - 1, perPage);

        Page<NewsEntity> newsPage = new PageImpl<>(newsEntities, pageable, newsEntities.size());

        when(newsRepository.findAll(pageable)).thenReturn(newsPage);
        when(newsMapper.newsEntityToGetNewsOutResponse(newsEntities.get(0))).thenReturn(getNewsOutResponses.get(0));
        when(newsMapper.newsEntityToGetNewsOutResponse(newsEntities.get(1))).thenReturn(getNewsOutResponses.get(1));

        PageableResponse<List<GetNewsOutResponse>> response = newsService.getNews(page, perPage);

        assertEquals(getNewsOutResponses, response.getContent());
        assertEquals(newsEntities.size(), response.getNumberOfElements());
        verify(newsRepository).findAll(pageable);
        verify(newsMapper, times(2)).newsEntityToGetNewsOutResponse(any(NewsEntity.class));
    }

    @Test
    void getUserNews_Success() {
        when(userService.getUserById(currentUser.getId())).thenReturn(currentUser);
        int page = 1;
        int perPage = 2;
        Pageable pageable = PageRequest.of(page - 1, perPage);

        Page<NewsEntity> newsPage = new PageImpl<>(newsEntities, pageable, newsEntities.size());

        when(newsRepository.findByUser(currentUser, pageable)).thenReturn(newsPage);
        when(newsMapper.newsEntityToGetNewsOutResponse(newsEntities.get(0))).thenReturn(getNewsOutResponses.get(0));
        when(newsMapper.newsEntityToGetNewsOutResponse(newsEntities.get(1))).thenReturn(getNewsOutResponses.get(1));

        PageableResponse<List<GetNewsOutResponse>> response = newsService
                .getUserNews(page, perPage, currentUser.getId());

        assertEquals(getNewsOutResponses, response.getContent());
        assertEquals(newsEntities.size(), response.getNumberOfElements());
        verify(newsRepository).findByUser(currentUser, pageable);
        verify(newsMapper, times(2)).newsEntityToGetNewsOutResponse(any(NewsEntity.class));
    }

    @Test
    void findNews_Success() {
        String author = Constants.TEST_USER;
        String keywords = Constants.TEST_DESCRIPTION;
        List<String> tags = List.of(Constants.TEST_TAG1, Constants.TEST_TAG2);
        int page = 1;
        int perPage = 2;

        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsPage = new PageImpl<>(newsEntities, pageable, newsEntities.size());

        when(newsRepository.findAllByAuthorAndKeywordsAndTags(author, keywords, tags, pageable)).thenReturn(newsPage);
        when(newsMapper.newsEntityToGetNewsOutResponse(any(NewsEntity.class))).thenReturn(getNewsOutResponses.get(0));

        PageableResponse<List<GetNewsOutResponse>> response = newsService.findNews(author,
                keywords, page, perPage, tags);

        assertEquals(getNewsOutResponses, response.getContent());
        assertEquals(newsEntities.size(), response.getNumberOfElements());
        verify(newsRepository).findAllByAuthorAndKeywordsAndTags(author, keywords, tags, pageable);
        verify(newsMapper, times(newsEntities.size())).newsEntityToGetNewsOutResponse(any(NewsEntity.class));
    }

    @Test
    void putNews_Success() {
        authorization();
        when(userService.getCurrentUser()).thenReturn(currentUser);

        NewsEntity oldNews = new NewsEntity();
        oldNews.setId(newsEntity.getId());
        oldNews.setDescription(Constants.TEST_OLDDESCRIPTION);
        oldNews.setImage(Constants.TEST_OLDAVATAR);
        oldNews.setTitle(Constants.TEST_OLDTITLE);
        oldNews.setUser(currentUser);
        oldNews.setTags(tagsEntities);

        when(newsRepository.getNewsById(oldNews.getId())).thenReturn(Optional.of(oldNews));
        when(newsMapper.newsRequestToEntity(newsRequest)).thenAnswer(invocation -> {
            NewsEntity updatedNews2 = new NewsEntity();
            updatedNews2.setId(oldNews.getId());
            updatedNews2.setDescription(newsRequest.getDescription());
            updatedNews2.setImage(newsRequest.getImage());
            updatedNews2.setTitle(newsRequest.getTitle());
            updatedNews2.setUser(currentUser);
            return updatedNews2;
        });
        when(tagsService.findOrCreateTags(newsRequest.getTags())).thenReturn(newsEntity.getTags());

        newsService.putNews(oldNews.getId(), newsRequest);

        verify(newsRepository, times(1)).save(any(NewsEntity.class));
    }

    @Test
    void putNews_NewsNotFound1() {
        authorization();
        when(userService.getCurrentUser()).thenReturn(currentUser);

        NewsEntity oldNews = new NewsEntity();
        oldNews.setId(3L);
        oldNews.setDescription(Constants.TEST_OLDDESCRIPTION);
        oldNews.setImage(Constants.TEST_OLDAVATAR);
        oldNews.setTitle(Constants.TEST_OLDTITLE);
        oldNews.setUser(new UserEntity());

        when(newsRepository.getNewsById(oldNews.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                newsService.putNews(oldNews.getId(), newsRequest));

        assertEquals(ServerErrorCodes.NEWS_NOT_FOUND, exception.getError());
    }

    @Test
    void putNews_NewsNotFound2() {
        authorization();
        when(userService.getCurrentUser()).thenReturn(currentUser);

        UserEntity anotherUser = new UserEntity();
        anotherUser.setId(UUID.fromString(Constants.TEST_UUID2));
        NewsEntity oldNews = new NewsEntity();
        oldNews.setId(3L);
        oldNews.setUser(anotherUser);

        when(newsRepository.getNewsById(oldNews.getId())).thenReturn(Optional.of(oldNews));

        CustomException exception = assertThrows(CustomException.class, () ->
                newsService.putNews(oldNews.getId(), newsRequest));

        assertEquals(ServerErrorCodes.NEWS_NOT_FOUND, exception.getError());
    }

    @Test
    void deleteNews_Success() {
        authorization();
        when(userService.getCurrentUser()).thenReturn(currentUser);

        when(newsRepository.getNewsById(newsEntity.getId())).thenReturn(Optional.of(newsEntity));

        doNothing().when(newsRepository).delete(newsEntity);
        doNothing().when(tagsService).deleteTags();

        newsService.deleteNews(newsEntity.getId());

        verify(newsRepository, times(1)).delete(any(NewsEntity.class));
        verify(tagsService, times(1)).deleteTags();
    }

    @Test
    void deleteNews_NewsNotFound1() {
        authorization();
        when(userService.getCurrentUser()).thenReturn(currentUser);

        when(newsRepository.getNewsById(newsEntity.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                newsService.deleteNews(newsEntity.getId()));

        assertEquals(ServerErrorCodes.NEWS_NOT_FOUND, exception.getError());
    }

    @Test
    void deleteNews_NewsNotFound2() {
        authorization();
        when(userService.getCurrentUser()).thenReturn(currentUser);

        UserEntity anotherUser = new UserEntity();
        anotherUser.setId(UUID.fromString(Constants.TEST_UUID2));
        NewsEntity oldNews = new NewsEntity();
        oldNews.setId(3L);
        oldNews.setUser(anotherUser);

        when(newsRepository.getNewsById(oldNews.getId())).thenReturn(Optional.of(oldNews));

        CustomException exception = assertThrows(CustomException.class, () ->
                newsService.deleteNews(oldNews.getId()));

        assertEquals(ServerErrorCodes.NEWS_NOT_FOUND, exception.getError());
    }

    private void authorization() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        lenient().when(SecurityContextHolder.getContext().getAuthentication()
                .getName()).thenReturn(currentUser.getId().toString());
    }
}
