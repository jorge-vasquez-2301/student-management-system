package com.example.studentmanagementsystem;

import com.example.studentmanagementsystem.repository.exception.ClassroomNotFoundException;
import com.example.studentmanagementsystem.repository.exception.StudentNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * The ExceptionHandler class.
 * @author Jorge Vasquez
 * @since 1.8
 */
@ControllerAdvice
public class ExceptionHandler {

    /**
     * Handles StudentNotFoundException
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The student was not found in the system")
    @org.springframework.web.bind.annotation.ExceptionHandler(StudentNotFoundException.class)
    public void studentNotFoundExceptionHandler() {
        // No Op
    }

    /**
     * Handles ClassroomNotFoundException
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The classroom was not found in the system")
    @org.springframework.web.bind.annotation.ExceptionHandler(ClassroomNotFoundException.class)
    public void classroomNotFoundExceptionHandler() {
        // No Op
    }

    /**
     * Handles EmptyResultDataAccessException.
     */
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "There is no item to delete")
    @org.springframework.web.bind.annotation.ExceptionHandler(EmptyResultDataAccessException.class)
    public void emptyResultDataAccessExceptionHandler() {
        // No Op
    }

    /**
     * Handles MethodArgumentTypeMismatchException
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The request is invalid")
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void methodArgumentTypeMismatchExceptionHandler() {
        // No Op
    }
}
