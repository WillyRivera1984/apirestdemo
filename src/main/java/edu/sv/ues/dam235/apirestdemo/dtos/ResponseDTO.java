package edu.sv.ues.dam235.apirestdemo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private boolean success;
    private String message;
    private Object data;
    
    public ResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
}
