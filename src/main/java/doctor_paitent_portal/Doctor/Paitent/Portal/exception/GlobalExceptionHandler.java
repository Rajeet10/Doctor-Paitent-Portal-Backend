package doctor_paitent_portal.Doctor.Paitent.Portal.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle AccessDeniedException
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "success", false,
                        "message", "Access Denied: You do not have permission to access this resource",
                        "data", null
                ));
    }

    // Optional: Handle other exceptions globally
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "success", false,
                        "message", ex.getMessage(),
                        "data", null
                ));
    }
}
