package com.example.Mangxahoi.utils;

import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.exceptions.ApiSubError;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EOResponse<T> {

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern ="yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime timestamp = LocalDateTime.now();
    private int code;
    private String message;

    private T data;



    public EOResponse (){
    }

    public static <T> EOResponse<T> build(int code, String messageUrl, Object...args){
        EOResponse<T> response = new EOResponse<>();
        response.code = code;
        response.message = messageUrl;
        return  response;
    }

    public static <T> EOResponse<T> build (T data){
        EOResponse<T> response = new EOResponse<>();
        response.data = data;
        response.message= MessageCodes.PROCESSED_SUCCESSFULLY;
        response.code = HttpStatus.OK.value();

        return response;
    }

    public static EOResponse<List<ApiSubError>> buildMsg(int code, String message, ApiSubError subError) {
        List<ApiSubError> errors = new ArrayList<ApiSubError>();
        errors.add(subError);
        return buildMsg(code, message, errors);
    }

    public static EOResponse<List<ApiSubError>> buildMsg(int code, String message, List<ApiSubError> subErrors) {
        EOResponse<List<ApiSubError>> response = buildMsg(code, message);
        response.data = subErrors;
        return response;
    }

    public static <T> EOResponse<T> buildMsg(int code, String message) {
        EOResponse<T> response = new EOResponse<>();
        response.code = code;
        response.message = message;
        return response;
    }
}
