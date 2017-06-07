package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.model.Classroom;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.repository.ClassroomRepository;
import com.example.studentmanagementsystem.repository.StudentRepository;
import com.example.studentmanagementsystem.repository.exception.ClassroomNotFoundException;
import com.example.studentmanagementsystem.repository.exception.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(method = RequestMethod.POST)
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @RequestMapping(value = "/{studentId}/class/{classroomCode}", method = RequestMethod.POST)
    @CacheEvict(value = "studentCache", key = "#studentId")
    public Student assignStudentToClassroom(@PathVariable int studentId,
                                            @PathVariable String classroomCode) throws StudentNotFoundException,
                                                                                       ClassroomNotFoundException {
        Student student = Optional.ofNullable(studentRepository.findOne(studentId))
                                  .orElseThrow(() -> new StudentNotFoundException(studentId));
        Classroom classroom = Optional.ofNullable(classroomRepository.findOne(classroomCode))
                                      .orElseThrow(() -> new ClassroomNotFoundException(classroomCode));
        student.addClassroom(classroom);
        return studentRepository.save(student);
    }

    @RequestMapping(value = "/{studentId}/class/{classroomCode}", method = RequestMethod.DELETE)
    @CacheEvict(value = "studentCache", key = "#studentId")
    public void removeStudentFromClassroom(@PathVariable int studentId,
                                           @PathVariable String classroomCode) throws StudentNotFoundException,
                                                                                      ClassroomNotFoundException {
        Student student = Optional.ofNullable(studentRepository.findOne(studentId))
                                  .orElseThrow(() -> new StudentNotFoundException(studentId));
        Classroom classroom = Optional.ofNullable(classroomRepository.findOne(classroomCode))
                                      .orElseThrow(() -> new ClassroomNotFoundException(classroomCode));
        student.removeClassroom(classroom);
        studentRepository.save(student);
    }

    /**
     * Finds a student by id.
     * @param id the searched student id
     * @return the found student
     * @throws StudentNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    @Cacheable(value = "studentCache", key = "#id")
    public Student getStudentById(@RequestParam int id) throws StudentNotFoundException {
        return Optional.ofNullable(studentRepository.findOne(id)).orElseThrow(() -> new StudentNotFoundException(id));
    }

    /**
     * Finds students for the given search parameters.
     * @param firstName the searched first name (optional)
     * @param lastName  the searched last name (optional)
     * @return the found students for the given search parameters
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @Cacheable(value = "studentCache", key = "#root.args")
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
    @RequestMapping(value = "/classes", method = RequestMethod.GET)
    @Cacheable(value = "studentClassroomsCache", key = "#id")
    public List<Classroom> getStudentClassrooms(@RequestParam int id) throws StudentNotFoundException {
        Student student = Optional.ofNullable(studentRepository.findOne(id)).orElseThrow(() -> new StudentNotFoundException(id));
        return student.getClassrooms();
    }

    /**
     * Updates a student with the given data.
     * @param student the new data for the student
     * @return the updated student
     * @throws StudentNotFoundException
     */
    @RequestMapping(method = RequestMethod.PUT)
    @CacheEvict(value = "studentCache", key = "#student.getId()")
    public Student updateStudent(@RequestBody Student student) throws StudentNotFoundException {
        Optional.ofNullable(studentRepository.findOne(student.getId())).orElseThrow(() -> new StudentNotFoundException(student.getId()));
        return studentRepository.save(student);
    }

    /**
     * Deletes a student from the database.
     * @param id the id of the student to be deleted
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @CacheEvict(value = "studentCache", key = "#id")
    public void deleteStudent(@RequestParam int id) {
        studentRepository.delete(id);
    }
}
