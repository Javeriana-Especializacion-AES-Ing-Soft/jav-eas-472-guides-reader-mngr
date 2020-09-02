package co.edu.javeriana.guides.analizer.controllers;

import co.edu.javeriana.guides.analizer.dtos.GuidesRsDto;
import co.edu.javeriana.guides.analizer.enums.GuideAnalyzerExceptionCode;
import co.edu.javeriana.guides.analizer.exceptions.AbsGuideAnalyzerException;
import co.edu.javeriana.guides.analizer.exceptions.impl.CognitiveServiceCallException;
import co.edu.javeriana.guides.analizer.services.IGuideProcessorService;
import co.edu.javeriana.guides.analizer.utilities.ResourceProvider;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GuidesControllerTest {

    @Mock
    private IGuideProcessorService guideProcessorService;

    @InjectMocks
    private GuidesController guidesController;

    private final static String ENCODE_GUIDE = Base64.encodeBase64String("jpg".getBytes());
    private final static String EXTENSION = "jpg";
    private final static UUID PROCESS_ID = UUID.randomUUID();

    private final ResourceProvider resourceProvider = new ResourceProvider();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void processGuideTest() throws AbsGuideAnalyzerException {
        when(guideProcessorService.readGuide(anyString(), anyString(), any())).thenReturn(resourceProvider.getGuidesRsDtoMock());
        ResponseEntity<GuidesRsDto> response = guidesController.processGuide(EXTENSION, ENCODE_GUIDE);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getUuid());
    }

    @Test
    void processGuideFailedByCognitiveErrorTest() throws AbsGuideAnalyzerException {
        when(guideProcessorService.readGuide(anyString(), anyString(), any())).
                thenThrow(new CognitiveServiceCallException(GuideAnalyzerExceptionCode.REST_WITHOUT_COMMUNICATION, "SERVICIO COGNITIVO SIN CONEXION"));
        ResponseEntity<GuidesRsDto> response = guidesController.processGuide(EXTENSION, ENCODE_GUIDE);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void downloadGuideTest() throws AbsGuideAnalyzerException {
        when(guideProcessorService.downloadGuide(any())).thenReturn("ENCODE");
        ResponseEntity<String> response = guidesController.downloadGuide(PROCESS_ID);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void downloadGuideFailedByCognitiveErrorTest() throws AbsGuideAnalyzerException {
        when(guideProcessorService.downloadGuide(any())).
                thenThrow(new CognitiveServiceCallException(GuideAnalyzerExceptionCode.REST_WITHOUT_COMMUNICATION));
        ResponseEntity<String> response = guidesController.downloadGuide(PROCESS_ID);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
