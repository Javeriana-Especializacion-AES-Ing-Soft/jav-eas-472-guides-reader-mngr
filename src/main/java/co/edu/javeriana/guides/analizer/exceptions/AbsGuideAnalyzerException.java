package co.edu.javeriana.guides.analizer.exceptions;

import co.edu.javeriana.guides.analizer.enums.GuideAnalyzerExceptionCode;

public class AbsGuideAnalyzerException extends Exception {

    private final GuideAnalyzerExceptionCode exceptionCode;

    public AbsGuideAnalyzerException(GuideAnalyzerExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public AbsGuideAnalyzerException(GuideAnalyzerExceptionCode exceptionCode, String causeMessage) {
        super(causeMessage);
        this.exceptionCode = exceptionCode;
    }

    public AbsGuideAnalyzerException(GuideAnalyzerExceptionCode exceptionCode, String causeMessage, Exception e) {
        super(causeMessage, e);
        this.exceptionCode = exceptionCode;
    }

    public GuideAnalyzerExceptionCode getExceptionCode() {
        return exceptionCode;
    }

}
