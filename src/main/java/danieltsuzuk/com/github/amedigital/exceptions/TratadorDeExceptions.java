package danieltsuzuk.com.github.amedigital.exceptions;

import danieltsuzuk.com.github.amedigital.dto.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class TratadorDeExceptions {

    @ExceptionHandler(BancoDeDadosException.class)
    public ResponseEntity<Object> bancoDeDadosException(HttpServletRequest request, BancoDeDadosException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponse erro = new ErroResponse(new Date(), status.value(), Arrays.asList(e.getMessage()), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> listaErros = new ArrayList<>();
        ErroResponse erro = new ErroResponse(new Date(), status.value(), listaErros, request.getRequestURI());

        for (FieldError error : e.getFieldErrors()) {
            listaErros.add(error.getField() + ": " +error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

}
