package ute.udn.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiPassDTO {
    private String data;
    private Boolean success;
    private String timestamp;
    private String cause;
    private String path;

    public ApiPassDTO(Boolean success, String data, String cause, String path) {
        this.timestamp = Instant.now().toString();
        this.data = data;
        this.success = success;
        this.cause = cause;
        this.path = path;
    }

    public ApiPassDTO(Boolean success, String data) {
        this.timestamp = Instant.now().toString();
        this.data = data;
        this.success = success;
        this.cause = null;
        this.path = null;
    }
}
