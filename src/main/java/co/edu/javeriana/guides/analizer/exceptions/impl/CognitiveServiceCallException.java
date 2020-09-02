package co.edu.javeriana.guides.analizer.exceptions.impl;

import co.edu.javeriana.guides.analizer.enums.GuideAnalyzerExceptionCode;
import co.edu.javeriana.guides.analizer.exceptions.AbsGuideAnalyzerException;

public class CognitiveServiceCallException extends AbsGuideAnalyzerException {

    public CognitiveServiceCallException(GuideAnalyzerExceptionCode exceptionCode, String causeMessage) {
        super(exceptionCode, causeMessage);
    }

    public CognitiveServiceCallException(GuideAnalyzerExceptionCode exceptionCode, String causeMessage, Exception e) {
        super(exceptionCode, causeMessage, e);
    }

}
