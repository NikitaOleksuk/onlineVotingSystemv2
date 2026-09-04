package project.onlinevotingsystem.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class AppError {
    private String message;
    private int status;
    private Date timestamp;

    public AppError( int status,String message) {

        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
