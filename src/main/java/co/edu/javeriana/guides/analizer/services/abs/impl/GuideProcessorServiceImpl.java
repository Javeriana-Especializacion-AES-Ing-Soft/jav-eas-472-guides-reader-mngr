package co.edu.javeriana.guides.analizer.services.abs.impl;

import co.edu.javeriana.guides.analizer.dtos.GuidesRsDto;
import co.edu.javeriana.guides.analizer.exceptions.AbsGuideAnalyzerException;
import co.edu.javeriana.guides.analizer.services.abs.AbsGuideProcessorService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GuideProcessorServiceImpl extends AbsGuideProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuideProcessorServiceImpl.class);

    @Override
    public GuidesRsDto readGuide(String guideExtension, String encodeGuide, UUID processId) throws AbsGuideAnalyzerException {
        LOGGER.info("[ID:{}] inicia proceso de envio a servicio cognitivo y analisis de resultado.", processId);
        byte[] guide = Base64.decodeBase64(encodeGuide);
        JsonNode cognitiveExtractGuide = callReadCognitiveService(guideExtension, guide, processId);
        UUID documentId = UUID.fromString(cognitiveExtractGuide.get("uuid").asText());
        GuidesRsDto guidesRsDto = new GuidesRsDto();
        guidesRsDto.setUuid(documentId);
        LOGGER.info("[ID:{}] finaliza proceso de envio a servicio cognitivo y analisis de resultado. [DOCUMENTO_ID:{}]", processId, documentId);
        return guidesRsDto;
    }

    @Override
    public String downloadGuide(UUID uuid) throws AbsGuideAnalyzerException {
        LOGGER.info("[DOCUMENTO_ID:{}] inicia proceso de descarga de guia.", uuid);
        byte[] decodeGuide = callDownloadCognitiveService(uuid);
        LOGGER.info("[DOCUMENTO_ID:{}] finaliza proceso de descarga de guia.", uuid);
        return Base64.encodeBase64String(decodeGuide);
    }

}
