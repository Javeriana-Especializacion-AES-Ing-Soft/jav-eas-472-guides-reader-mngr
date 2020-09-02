package co.edu.javeriana.guides.analizer.services;

import co.edu.javeriana.guides.analizer.dtos.GuidesRsDto;
import co.edu.javeriana.guides.analizer.exceptions.AbsGuideAnalyzerException;

import java.util.UUID;

public interface IGuideProcessorService {

    GuidesRsDto readGuide(String guideExtension, String encodeGuide, UUID processId) throws AbsGuideAnalyzerException;

    String downloadGuide(UUID uuid) throws AbsGuideAnalyzerException;

}
