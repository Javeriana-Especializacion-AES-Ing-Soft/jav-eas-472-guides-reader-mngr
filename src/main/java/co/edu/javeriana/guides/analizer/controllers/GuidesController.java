package co.edu.javeriana.guides.analizer.controllers;

import co.edu.javeriana.guides.analizer.dtos.GuidesRsDto;
import co.edu.javeriana.guides.analizer.exceptions.AbsGuideAnalyzerException;
import co.edu.javeriana.guides.analizer.services.IGuideProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin("*") // NO SONAR
@RequestMapping("guides")
@RestController
public class GuidesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuidesController.class);

    private IGuideProcessorService guideProcessorService;

    @PostMapping("analyzer")
    public ResponseEntity<GuidesRsDto> processGuide(@RequestHeader("X-File-Extension") String guideExtension,
                                                    @RequestBody String encodedGuide) {
        final UUID processId = UUID.randomUUID();
        LOGGER.info("[ID:{}] INICIA PROCESO DE ANALISIS DE GUIA Y MAPEO DE CAMPOS REQUERIDOS", processId);
        try {
            GuidesRsDto response = guideProcessorService.readGuide(guideExtension, encodedGuide, processId);
            LOGGER.info("[ID:{}] FINALIZA PROCESO DE ANALISIS DE GUIA Y MAPEO DE CAMPOS REQUERIDOS", processId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AbsGuideAnalyzerException e) {
            return new ResponseEntity<>(e.getExceptionCode().getCode());
        }
    }

    @GetMapping("{uuid}")
    public ResponseEntity<String> processGuide(@PathVariable UUID uuid) {
        try {
            LOGGER.info("[DOCUMENTO_ID:{}] INICIA PROCESO DE DESCARGA DE IMAGEN GUIA", uuid);
            String response = guideProcessorService.downloadGuide(uuid);
            LOGGER.info("[DOCUMENTO_ID:{}] FINALIZA PROCESO DE DESCARGA DE IMAGEN GUIA", uuid);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AbsGuideAnalyzerException e) {
            return new ResponseEntity<>(e.getExceptionCode().getCode());
        }
    }

    @Autowired
    public void setGuideProcessorService(IGuideProcessorService guideProcessorService) {
        this.guideProcessorService = guideProcessorService;
    }
}
