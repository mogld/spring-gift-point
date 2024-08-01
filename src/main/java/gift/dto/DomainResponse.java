package gift.dto;

import gift.model.HttpResult;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class DomainResponse {

    private HttpResult httpResult;
    private List<Map<String, Object>> data;
    private HttpStatus httpStatus;

    public DomainResponse() {}

    public DomainResponse(HttpResult httpResult, List<Map<String, Object>> data, HttpStatus httpStatus) {
        this.httpResult = httpResult;
        this.data = data;
        this.httpStatus = httpStatus;
    }

    public HttpResult getHttpResult() {
        return httpResult;
    }

    public void setHttpResult(HttpResult httpResult) {
        this.httpResult = httpResult;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
