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
                if (line.get("text").asText().toLowerCase().startsWith("nombre")) {
                    names.add(line);
                }
                if (line.get("text").asText().toLowerCase().startsWith("direcci")) {
                    address.add(line);
                }
                if (line.get("text").asText().toLowerCase().startsWith("ciudad") || line.get("text").asText().toLowerCase().startsWith("cludad")) {
                    states.add(line);
                }
                if (line.get("text").asText().toLowerCase().startsWith("tel")) {
                    cellphones.add(line);
                }
            }
        });
        GuideInfoDto guideInfoDto = new GuideInfoDto();
        guideInfoDto.getSender().setAddress(getValue(address, 0));
        guideInfoDto.getSender().setPhoneNumber(getValue(cellphones, 0));
        guideInfoDto.getSender().setName(getValue(names, 0));
        guideInfoDto.getSender().setState(getValue(states, 0));
        guideInfoDto.getReceiver().setAddress(getValue(address, 1));
        guideInfoDto.getReceiver().setPhoneNumber(getValue(cellphones, 1));
        guideInfoDto.getReceiver().setName(getValue(names, 1));
        guideInfoDto.getReceiver().setState(getValue(states, 1));
        guidesRsDto.setGuideInfo(guideInfoDto);
        String phoneNumberSender = guideInfoDto.getSender().getPhoneNumber();
        String phoneNumberReceiver = guideInfoDto.getReceiver().getPhoneNumber();
        guideInfoDto.getSender().setPhoneNumber(phoneNumberSender.isEmpty() ? "1" : phoneNumberSender);
        guideInfoDto.getReceiver().setPhoneNumber(phoneNumberReceiver.isEmpty() ? "1" : phoneNumberReceiver);
        guideInfoDto.setComplete(!(guideInfoDto.getSender().getAddress().isEmpty() || guideInfoDto.getSender().getName().isEmpty()
                || guideInfoDto.getSender().getState().isEmpty() || guideInfoDto.getSender().getPhoneNumber().isEmpty()
                || guideInfoDto.getReceiver().getAddress().isEmpty() || guideInfoDto.getReceiver().getName().isEmpty()
                || guideInfoDto.getReceiver().getState().isEmpty() || guideInfoDto.getReceiver().getPhoneNumber().isEmpty())
        );
        return guidesRsDto;
    }

    private String getValue(List<JsonNode> listLine, int index) {
        try {
            return listLine.get(index).get("confidence").asDouble() >= acceptablePercentage ?
                    listLine.get(index).get("text").asText().concat(":1").split(":")[1] : "";
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String downloadGuide(UUID uuid) throws AbsGuideAnalyzerException {
        LOGGER.info("[DOCUMENTO_ID:{}] inicia proceso de descarga de guia.", uuid);
        byte[] decodeGuide = callDownloadCognitiveService(uuid);
        LOGGER.info("[DOCUMENTO_ID:{}] finaliza proceso de descarga de guia.", uuid);
        return Base64.encodeBase64String(decodeGuide);
    }

}
