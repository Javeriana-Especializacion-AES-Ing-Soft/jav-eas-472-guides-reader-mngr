package co.edu.javeriana.guides.analizer.services.abs;

import co.edu.javeriana.guides.analizer.enums.GuideAnalyzerExceptionCode;
import co.edu.javeriana.guides.analizer.exceptions.impl.CognitiveServiceCallException;
import co.edu.javeriana.guides.analizer.services.IGuideProcessorService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public abstract class AbsGuideProcessorService implements IGuideProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbsGuideProcessorService.class);

    protected double acceptablePercentage;
    private String cognitiveEndpoint;
    private String cognitivePathRead;
    private String cognitivePathDownload;

    private String bucketName;
    private String formExtractType;
    private String rootDirectory;

    private RestTemplate restTemplate;

    protected JsonNode callReadCognitiveService(String guideExtension, byte[] guide, UUID processId) throws CognitiveServiceCallException {
        LOGGER.info("[ID:{}] inicia llamado de servicio cognitivo para reconocimiento de caracteres.", processId);
        HttpHeaders headers = new HttpHeaders();
        getCommonHeaders(headers);
        getReadHeaders(headers, guideExtension);
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(guide, headers);
        String endpoint = cognitiveEndpoint.concat(cognitivePathRead);
        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, JsonNode.class);
            LOGGER.info("[ID:{}] finaliza llamado de servicio cognitivo para reconocimiento de caracteres.", processId);
            return response.getBody();
        } catch (RestClientException e) {
            throw new CognitiveServiceCallException(GuideAnalyzerExceptionCode.REST_WITHOUT_COMMUNICATION,
                    String.format("[ID:%s] ERROR CON SERVICIO COGNITIVO", processId.toString()), e);
        }
    }

    protected byte[] callDownloadCognitiveService(UUID documentId) throws CognitiveServiceCallException {
        LOGGER.info("[DOCUMENTO_ID:{}] inicia llamado de servicio cognitivo para descarga de guia.", documentId);
        HttpHeaders headers = new HttpHeaders();
        getCommonHeaders(headers);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        String endpoint = cognitiveEndpoint.concat(cognitivePathDownload).replace("{@uuid}", documentId.toString());
        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, byte[].class);
            LOGGER.info("[DOCUMENTO_ID:{}] finaliza llamado de servicio cognitivo para descarga de guia.", documentId);
            return response.getBody();
        } catch (RestClientException e) {
            throw new CognitiveServiceCallException(GuideAnalyzerExceptionCode.REST_WITHOUT_COMMUNICATION, "ERROR CON SERVICIO COGNITIVO", e);
        }
    }

    private void getReadHeaders(HttpHeaders headers, String guideExtension) {
        headers.add("X-Extract-Type", formExtractType);
        headers.add("X-File-Extension", guideExtension);
        headers.add("X-Root-Directory", rootDirectory);
    }

    private void getCommonHeaders(HttpHeaders headers) {
        headers.add("X-Bucket-Name", bucketName);
    }

    @Value("${guides.analyzer.472.acceptable.percentage}")
    public void setAcceptablePercentage(double acceptablePercentage) {
        this.acceptablePercentage = acceptablePercentage;
    }

    @Value("${endpoint.cognitive.service.472}")
    public void setCognitiveEndpoint(String cognitiveEndpoint) {
        this.cognitiveEndpoint = cognitiveEndpoint;
    }

    @Value("${path.cognitive.service.read}")
    public void setCognitivePathRead(String cognitivePathRead) {
        this.cognitivePathRead = cognitivePathRead;
    }

    @Value("${path.cognitive.service.download}")
    public void setCognitivePathDownload(String cognitivePathDownload) {
        this.cognitivePathDownload = cognitivePathDownload;
    }

    @Value("${guides.analyzer.472.main.bucket}")
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Value("${guides.analyzer.472.form.extract.type}")
    public void setFormExtractType(String formExtractType) {
        this.formExtractType = formExtractType;
    }

    @Value("${guides.analyzer.472.root.directory}")
    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
