import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
import com.example.GiscovAdvancedServer.services.impl.FilesServiceImpl;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@ExtendWith(MockitoExtension.class)
public class FilesServiceImplTest {

    @InjectMocks
    private FilesServiceImpl filesService;

    private MultipartFile multipartFile;

    private final String testDir = Constants.TEST_STORAGEDIR;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(filesService, Constants.TEST_REALNAME_DIR, testDir);

        new File(testDir).mkdirs();

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void uploadFile_Success() {
        String originalFileName = Constants.TEST_ORIGINAL_FILE_NAME;
        String content = Constants.TEST_CONTENT;
        multipartFile = new MockMultipartFile(originalFileName, originalFileName,
                Constants.TEST_CONTENT_TYPE, content.getBytes());

        String fileDownloadUri = filesService.uploadFile(multipartFile);

        assertNotNull(fileDownloadUri);
        assertTrue(fileDownloadUri.contains(originalFileName));

        File savedFile = new File(testDir + File.separator + originalFileName);
        assertTrue(savedFile.exists());
    }

    @Test
    void uploadFile_FileIsEmpty() {
        String originalFileName = Constants.TEST_ORIGINAL_FILE_NAME;
        String content = Constants.TEST_CONTENT;
        multipartFile = new MockMultipartFile(originalFileName, originalFileName,
                Constants.TEST_CONTENT_TYPE, (byte[]) null);

        CustomException exception = assertThrows(CustomException.class, () ->
                filesService.uploadFile(multipartFile));

        assertEquals(ServerErrorCodes.UNKNOWN, exception.getError());
    }

    @Test
    void downloadFile_Success() throws IOException {
        String filename = Constants.TEST_ORIGINAL_FILE_NAME;
        String content = Constants.TEST_CONTENT;
        File file = new File(testDir + File.separator + filename);
        Files.write(file.toPath(), content.getBytes());

        UrlResource resource = filesService.downloadFile(filename);

        assertNotNull(resource);
        assertTrue(resource.exists());

        try (InputStream inputStream = resource.getInputStream()) {
            String downloadedContent = new String(inputStream.readAllBytes());
            assertEquals(content, downloadedContent);
        }
    }

    @Test
    void downloadFile_FileNotFound() {
        String nonExistentFilename = Constants.TEST_ORIGINAL_FILE_NAME;

        CustomException thrownException = assertThrows(CustomException.class, () -> {
            filesService.downloadFile(nonExistentFilename);
        });
        assertEquals(ServerErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED, thrownException.getError());
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(new File(testDir));
        RequestContextHolder.resetRequestAttributes();
    }
}
