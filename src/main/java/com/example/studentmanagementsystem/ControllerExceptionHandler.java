package com.example.studentmanagementsystem;

import com.example.studentmanagementsystem.repository.exception.ClassroomNotFoundException;
import com.example.studentmanagementsystem.repository.exception.StudentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * The ControllerExceptionHandler class.
 * @author Jorge Vasquez
 * @since 1.8
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Handles StudentNotFoundException.
     * @param exception the exception to be handled
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The student was not found in the system")
    @ExceptionHandler(StudentNotFoundException.class)
    public void studentNotFoundExceptionHandler(Exception exception) {
        logger.warn(exception.getMessage());
    }

    /**
     * Handles ClassroomNotFoundException.
     * @param exception the exception to be handled
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The classroom was not found in the system")
    @ExceptionHandler(ClassroomNotFoundException.class)
    public void classroomNotFoundExceptionHandler(Exception exception) {
        logger.warn(exception.getMessage());
    }

    /**
     * Handles EmptyResultDataAccessException.
     * @param exception the exception to be handled
     */
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "There is no item to delete")
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public void emptyResultDataAccessExceptionHandler(Exception exception) {
        logger.warn(exception.getMessage());
    }

    /**
     * Handles MethodArgumentTypeMismatchException.
     * @param exception the exception to be handled
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The request is invalid")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void methodArgumentTypeMismatchExceptionHandler(Exception exception) {
        logger.warn(exception.getMessage());
    }
}
