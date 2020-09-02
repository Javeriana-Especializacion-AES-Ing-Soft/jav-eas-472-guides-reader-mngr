package co.edu.javeriana.guides.analizer.enums;

import org.springframework.http.HttpStatus;

public enum GuideAnalyzerExceptionCode {

    REST_WITHOUT_COMMUNICATION(HttpStatus.INTERNAL_SERVER_ERROR);

    private HttpStatus status;

    GuideAnalyzerExceptionCode(HttpStatus code) {
        this.status = code;
    }

    public HttpStatus getCode() {
        return status;
    }

}
