package hu.bme.szgbizt.secushop.controller;

import hu.bme.szgbizt.secushop.dto.*;
import hu.bme.szgbizt.secushop.exception.InvalidFileExtensionException;
import hu.bme.szgbizt.secushop.service.SecuShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static hu.bme.szgbizt.secushop.exception.errorcode.ErrorCode.SS_0102;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class SecuShopControllerTest {

    private static final UUID UUID_DEFAULT = UUID.randomUUID();

    private SecuShopController secuShopController;
    private Authentication authentication;

    @Mock
    private SecuShopService secuShopService;

    @BeforeEach
    void setUp() {
        secuShopController = new SecuShopController(secuShopService);
        authentication = new JwtAuthenticationToken(mock(Jwt.class), List.of(mock(SimpleGrantedAuthority.class)), UUID_DEFAULT.toString());
    }

    @Test
    void testGetImage() {

        // Arrange
        when(secuShopService.getImage(anyString())).thenReturn(mock(UrlResource.class));

        // Act
        var urlResource = secuShopController.getImage(authentication, "filename");

        // Assert
        assertNotNull(urlResource);
    }

    @Test
    void testCreateCaffDataWithNonCaffFile() {

        // Arrange
        var httpServletRequest = new MockHttpServletRequest();
        var content = "content".getBytes();
        var file = new MockMultipartFile("name", "filename.nonCaff", "contentType", content);

        // Act
        var invalidFileExtensionException = assertThrows(InvalidFileExtensionException.class,
                () -> secuShopController.createCaffData(authentication, httpServletRequest, file, "filename", "description"));

        // Arrange
        assertEquals(SS_0102, invalidFileExtensionException.getErrorCode());
    }

    @Test
    void testCreateCaffWithCaffFile() {

        // Arrange
        var httpServletRequest = mock(HttpServletRequest.class);
        var content = "content".getBytes();
        var file = new MockMultipartFile("name", "filename.caff", "contentType", content);
        when(secuShopService.createCaffData(any(UUID.class), anyString(), anyString(), any(MultipartFile.class), any(HttpServletRequest.class)))
                .thenReturn(mock(DetailedCaffData.class));

        // Act
        var caffData = secuShopController.createCaffData(authentication, httpServletRequest, file, "filename", "description");

        // Arrange
        assertNotNull(caffData);
    }

    @Test
    void testGetCaffDataAsResource() {

        // Arrange
        when(secuShopService.getCaffDataAsResource(any(UUID.class), any(UUID.class))).thenReturn(mock(Resource.class));

        // Act
        var resource = secuShopController.getCaffDataAsResource(authentication, UUID_DEFAULT);

        // Assert
        assertNotNull(resource);
    }

    @Test
    void testGetCaffData() {

        // Arrange
        when(secuShopService.getCaffData(any(UUID.class))).thenReturn(mock(DetailedCaffData.class));

        // Act
        var detailedCaffData = secuShopService.getCaffData(UUID_DEFAULT);

        // Assert
        assertNotNull(detailedCaffData);
    }

    @Test
    void testGetCaffDataList() {

        // Arrange
        when(secuShopService.getCaffDataList()).thenReturn(List.of(mock(CaffData.class)));

        // Act
        var caffDataList = secuShopService.getCaffDataList();

        // Assert
        assertNotNull(caffDataList);
    }

    @Test
    void testCreateComment() {

        // Arrange
        when(secuShopService.postComment(any(UUID.class), any(UUID.class), anyString())).thenReturn(mock(CaffComment.class));

        // Act
        var caffComment = secuShopService.postComment(UUID_DEFAULT, UUID_DEFAULT, "message");

        // Assert
        assertNotNull(caffComment);
    }

    @Test
    void testModifyPassword() {

        try {

            // Arrange
            var patchPasswordRequest = mock(PatchPasswordRequest.class);
            doNothing().when(secuShopService).modifyPassword(any(UUID.class), any(UUID.class), eq(patchPasswordRequest));

            // Act
            secuShopController.modifyPassword(authentication, UUID_DEFAULT, patchPasswordRequest);

        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void testModifyProfile() {

        try {

            // Arrange
            var patchProfileRequest = mock(PatchProfileRequest.class);
            doNothing().when(secuShopService).modifyProfile(any(UUID.class), any(UUID.class), eq(patchProfileRequest));

            // Act
            secuShopController.modifyProfile(authentication, UUID_DEFAULT, patchProfileRequest);

        } catch (Exception ex) {
            fail();
        }
    }
}
