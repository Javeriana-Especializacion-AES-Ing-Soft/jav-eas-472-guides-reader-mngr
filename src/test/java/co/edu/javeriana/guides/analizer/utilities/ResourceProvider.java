package co.edu.javeriana.guides.analizer.utilities;

import co.edu.javeriana.guides.analizer.dtos.GuidesRsDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ResourceProvider {

    public ResponseEntity<JsonNode> getCognitiveServiceResponseReaderMock() {
        try (InputStream inputStream = getClass().getResourceAsStream("/cognitive-service-response-reader.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode js = objectMapper.readValue(inputStream, JsonNode.class);
            return new ResponseEntity<>(js, HttpStatus.OK);
        } catch (IOException e) {
            return null;
        }
    }

    public GuidesRsDto getGuidesRsDtoMock() {
        GuidesRsDto guidesRsDto = new GuidesRsDto();
        guidesRsDto.setUuid(UUID.randomUUID());
        return guidesRsDto;
    }

}
