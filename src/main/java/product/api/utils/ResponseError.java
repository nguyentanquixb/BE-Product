package product.api.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import product.api.response.Response;

import java.util.List;

public class ResponseError {

    public static ResponseEntity<Response> errorResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error(message));
    }

    public static ResponseEntity<Response> errorResponse(List<String> errors) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error(errors));
    }
}
