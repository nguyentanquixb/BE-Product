package product.api.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Response {
    private Object data;
    private int status;
    private Object error;

    public Response(Object data, int status, Object error) {
        this.data = data;
        this.status = status;
        this.error = error;
    }

    public static Response ok(Object data) {
        return new Response(data, HttpStatus.OK.value(), null);
    }

    public static Response error(Object error) {
        return new Response(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), error);
    }

    public static Response errorNotFound(Object error) {
        return new Response(null, HttpStatus.NOT_FOUND.value(), error);
    }
}

