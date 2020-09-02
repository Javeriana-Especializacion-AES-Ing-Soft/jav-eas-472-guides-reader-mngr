package co.edu.javeriana.guides.analizer.services.abs.impl;

import co.edu.javeriana.guides.analizer.dtos.GuidesRsDto;
import co.edu.javeriana.guides.analizer.exceptions.AbsGuideAnalyzerException;
import co.edu.javeriana.guides.analizer.exceptions.impl.CognitiveServiceCallException;
import co.edu.javeriana.guides.analizer.utilities.ResourceProvider;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GuideProcessorServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GuideProcessorServiceImpl guideProcessorService;

    private final static String EXTENSION = "jpg";
    private final static String ENCODE_GUIDE = Base64.encodeBase64String("jpg".getBytes());
    private final static UUID PROCESS_ID = UUID.randomUUID();

    private final ResourceProvider resourceProvider = new ResourceProvider();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        guideProcessorService.setBucketName("bucket");
        guideProcessorService.setCognitiveEndpoint("endpoint");
        guideProcessorService.setCognitivePathDownload("/download");
        guideProcessorService.setCognitivePathRead("/read");
        guideProcessorService.setFormExtractType("TYPE");
        guideProcessorService.setRootDirectory("/guides");
    }

    @Test
    void readGuideTest() throws AbsGuideAnalyzerException {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(JsonNode.class)))
                .thenReturn(resourceProvider.getCognitiveServiceResponseReaderMock());
        GuidesRsDto result = guideProcessorService.readGuide(EXTENSION, ENCODE_GUIDE, PROCESS_ID);
        Assertions.assertNotNull(result.getUuid());
    }

    @Test
    void readGuideFailedByRestClientTest() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(JsonNode.class)))
                .thenThrow(RestClientException.class);
        Assertions.assertThrows(CognitiveServiceCallException.class,
                () -> guideProcessorService.readGuide(EXTENSION, ENCODE_GUIDE, PROCESS_ID));
    }

    @Test
    void callDownloadCognitiveServiceTest() throws AbsGuideAnalyzerException {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(byte[].class)))
                .thenReturn(new ResponseEntity<>("file".getBytes(), HttpStatus.OK));
        String result = guideProcessorService.downloadGuide(PROCESS_ID);
        Assertions.assertNotNull(result);
    }

    @Test
    void callDownloadCognitiveServiceFailedByRestClientTest() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(byte[].class)))
                .thenThrow(RestClientException.class);
        Assertions.assertThrows(CognitiveServiceCallException.class,
                () -> guideProcessorService.downloadGuide(PROCESS_ID));
    }

}
