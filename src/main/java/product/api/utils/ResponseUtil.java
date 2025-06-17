package product.api.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import product.api.response.Response;

public class ResponseUtil {
    public static ResponseEntity<Response> buildResponse(HttpStatus status, Object data) {
        return ResponseEntity.status(status).body(Response.ok(data));
    }
}
