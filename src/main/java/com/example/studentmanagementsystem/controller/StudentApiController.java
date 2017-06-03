package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.model.Classroom;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.repository.ClassroomRepository;
import com.example.studentmanagementsystem.repository.StudentRepository;
import com.example.studentmanagementsystem.repository.exception.ClassroomNotFoundException;
import com.example.studentmanagementsystem.repository.exception.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Optional;

/**
 * The StudentApiController class.
 * @author Jorge Vasquez
 * @since 1.8
 */
@RestController
@RequestMapping(value = "/students")
public class StudentApiController {

    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;

    /**
     * Creates a new instance of StudentApiController.
     * @param studentRepository reference to the StudentRepository
     */
    @Autowired
    public StudentApiController(StudentRepository studentRepository, ClassroomRepository classroomRepository) {
        this.studentRepository = studentRepository;
        this.classroomRepository = classroomRepository;
    }

    /**
     * Creates a new Student.
     * @param student the student to be created
     * @return the student saved in the database
     */
    @RequestMapping(value = "/{firstName}/{lastName}", method = RequestMethod.POST)
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @RequestMapping(value = "/{studentId}/assign/{classroomCode}", method = RequestMethod.POST)
    public Student assignStudentToClassroom(@PathVariable int studentId, @PathVariable String classroomCode) throws StudentNotFoundException, ClassroomNotFoundException {
        Student student = Optional.ofNullable(studentRepository.findOne(studentId)).orElseThrow(StudentNotFoundException::new);
        Classroom classroom = Optional.ofNullable(classroomRepository.findOne(classroomCode)).orElseThrow(ClassroomNotFoundException::new);
        student.getClassrooms().add(classroom);
        return studentRepository.save(student);
    }

    /**
     * Finds a student by id.
     * @param id the searched student id
     * @return the found student
     * @throws StudentNotFoundException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Student getStudentById(@PathVariable int id) throws StudentNotFoundException {
        return Optional.ofNullable(studentRepository.findOne(id)).orElseThrow(StudentNotFoundException::new);
    }

    /**
     * Finds students for the given search parameters.
     * @param firstName the searched first name (optional)
     * @param lastName  the searched last name (optional)
     * @return the found students for the given search parameters
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Student> getStudents(@RequestParam(value = "firstName", required = false) String firstName,
                                     @RequestParam(value = "lastName", required = false) String lastName) {
        List<Student> students;
        if (firstName != null && lastName != null) {
            students = studentRepository.findStudentsByFirstAndLastName(firstName, lastName);
        } else if (firstName != null) {
            students = studentRepository.findStudentsByFirstNameContainingIgnoreCase(firstName);
        } else if (lastName != null) {
            students = studentRepository.findStudentsByLastNameContainingIgnoreCase(lastName);
        } else {
            students = studentRepository.findAll();
        }
        return students;
    }

    /**
     * Finds the classrooms for a given student id
     * @param id the searched student id
     * @return the found classrooms
     */
    @RequestMapping(value = "/{id}/classes", method = RequestMethod.GET)
    public List<Classroom> getStudentClassrooms(@PathVariable int id) throws StudentNotFoundException {
        Student student = Optional.ofNullable(studentRepository.findOne(id)).orElseThrow(StudentNotFoundException::new);
        return student.getClassrooms();
    }

    /**
     * Updates a student with the given data.
     * @param student the new data for the student
     * @return the updated student
     * @throws StudentNotFoundException
     */
    @RequestMapping(value = "/{id}/{firstName}/{lastName}", method = RequestMethod.PUT)
    public Student updateStudent(Student student) throws StudentNotFoundException {
        Optional.ofNullable(studentRepository.findOne(student.getId())).orElseThrow(StudentNotFoundException::new);
        return studentRepository.save(student);
    }

    /**
     * Deletes a student from the database.
     * @param id the id of the student to be deleted
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteStudent(@PathVariable int id) {
        studentRepository.delete(id);
    }

    /**
     * Handles StudentNotFoundException and EmptyResultDataAccessException.
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The student was not found in the system")
    @ExceptionHandler({StudentNotFoundException.class, EmptyResultDataAccessException.class})
    public void exceptionHandler() {
        // No Op
    }

    /**
     * Handles MethodArgumentTypeMismatchException
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The request is invalid")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void methodArgumentTypeMismatchExceptionHandler() {
        // No Op
    }
}
