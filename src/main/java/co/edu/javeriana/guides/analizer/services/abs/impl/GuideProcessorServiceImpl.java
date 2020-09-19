package co.edu.javeriana.guides.analizer.services.abs.impl;

import co.edu.javeriana.guides.analizer.dtos.GuideInfoDto;
import co.edu.javeriana.guides.analizer.dtos.GuidesRsDto;
import co.edu.javeriana.guides.analizer.exceptions.AbsGuideAnalyzerException;
import co.edu.javeriana.guides.analizer.services.abs.AbsGuideProcessorService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GuideProcessorServiceImpl extends AbsGuideProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuideProcessorServiceImpl.class);

    @Override
    public GuidesRsDto readGuide(String guideExtension, String encodeGuide, UUID processId) throws AbsGuideAnalyzerException {
        LOGGER.info("[ID:{}] inicia proceso de envio a servicio cognitivo y analisis de resultado.", processId);
        byte[] guide = Base64.decodeBase64(encodeGuide);
        JsonNode cognitiveExtractGuide = callReadCognitiveService(guideExtension, guide, processId);
        GuidesRsDto guidesInfo = buildGuideInfo(cognitiveExtractGuide);
        LOGGER.info("[ID:{}] finaliza proceso de envio a servicio cognitivo y analisis de resultado. [DOCUMENTO_ID:{}]", processId, guidesInfo.getUuid());
        return guidesInfo;
    }

    private GuidesRsDto buildGuideInfo(JsonNode cognitiveExtractGuide) {
        UUID documentId = UUID.fromString(cognitiveExtractGuide.get("uuid").asText());
        GuidesRsDto guidesRsDto = new GuidesRsDto();
        guidesRsDto.setUuid(documentId);
        List<JsonNode> names = new ArrayList<>();
        List<JsonNode> cellphones = new ArrayList<>();
        List<JsonNode> states = new ArrayList<>();
        List<JsonNode> address = new ArrayList<>();
        cognitiveExtractGuide.get("cognitiveServiceResponse").get("blocks").forEach(line -> {
            if (line.get("blockType").asText().equals("LINE")) {
                if (line.get("text").asText().startsWith("Nombre")) {
                    names.add(line);
                }
                if (line.get("text").asText().startsWith("Direcci")) {
                    address.add(line);
                }
                if (line.get("text").asText().startsWith("Ciudad")) {
                    states.add(line);
                }
                if (line.get("text").asText().startsWith("Tel")) {
                    cellphones.add(line);
                }
            }
        });
        GuideInfoDto guideInfoDto = new GuideInfoDto();
        guideInfoDto.getSender().setAddress(address.get(0).get("confidence").asDouble() >= acceptablePercentage ?
                address.get(0).get("text").asText().concat(":1").split(":")[1] : "");
        guideInfoDto.getSender().setPhoneNumber(cellphones.get(0).get("confidence").asDouble() >= acceptablePercentage ?
                cellphones.get(0).get("text").asText().concat(":1").split(":")[1] : "1");
        guideInfoDto.getSender().setName(names.get(0).get("confidence").asDouble() >= acceptablePercentage ?
                names.get(0).get("text").asText().concat(":1").split(":")[1] : "");
        guideInfoDto.getSender().setState(states.get(0).get("confidence").asDouble() >= acceptablePercentage ?
                states.get(0).get("text").asText().concat(":1").split(":")[1] : "");
        guideInfoDto.getReceiver().setAddress(address.get(1).get("confidence").asDouble() >= acceptablePercentage ?
                address.get(1).get("text").asText().concat(":1").split(":")[1] : "");
        guideInfoDto.getReceiver().setPhoneNumber(cellphones.get(1).get("confidence").asDouble() >= acceptablePercentage ?
                cellphones.get(1).get("text").asText().concat(":1").split(":")[1] : "1");
        guideInfoDto.getReceiver().setName(names.get(1).get("confidence").asDouble() >= acceptablePercentage ?
                names.get(1).get("text").asText().concat(":1").split(":")[1] : "");
        guideInfoDto.getReceiver().setState(states.get(1).get("confidence").asDouble() >= acceptablePercentage ?
                states.get(1).get("text").asText().concat(":1").split(":")[1] : "");
        guidesRsDto.setGuideInfo(guideInfoDto);
        String phoneNumberSender = guideInfoDto.getSender().getPhoneNumber();
        String phoneNumberReceiver = guideInfoDto.getReceiver().getPhoneNumber();
        guideInfoDto.getSender().setPhoneNumber(phoneNumberSender.isEmpty() ? "1" : phoneNumberSender);
        guideInfoDto.getReceiver().setPhoneNumber(phoneNumberReceiver.isEmpty() ? "1" : phoneNumberReceiver);
        guideInfoDto.setComplete(!(guideInfoDto.getSender().getAddress().isEmpty() && guideInfoDto.getSender().getName().isEmpty()
                && guideInfoDto.getSender().getState().isEmpty() && guideInfoDto.getSender().getPhoneNumber().isEmpty()
                && guideInfoDto.getReceiver().getAddress().isEmpty() && guideInfoDto.getReceiver().getName().isEmpty()
                && guideInfoDto.getReceiver().getState().isEmpty() && guideInfoDto.getReceiver().getPhoneNumber().isEmpty())
        );
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
