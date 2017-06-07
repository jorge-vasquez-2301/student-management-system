package com.example.studentmanagementsystem.repository.exception;

/**
 * The ClassroomNotFoundException class.
 * @author Jorge Vasquez
 * @since 1.8
 */
public class ClassroomNotFoundException extends Exception {

    /**
     * Creates a new instance of ClassroomNotFoundException.
     * @param classroomCode the classroom code
     */
    public ClassroomNotFoundException(String classroomCode) {
        super("Record for classroom with code " + classroomCode + " not found");
    }
}
