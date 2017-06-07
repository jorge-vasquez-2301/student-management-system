package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.model.Classroom;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.repository.ClassroomRepository;
import com.example.studentmanagementsystem.repository.exception.ClassroomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The ClassroomApiController class.
 * @author Jorge Vasquez
 * @since 1.8
 */
@RestController
@RequestMapping(value = "/classes")
public class ClassroomApiController {

    private final ClassroomRepository classroomRepository;

    /**
     * Creates a new instance of ClassroomApiController.
     * @param classroomRepository reference to the ClassroomRepository
     */
    @Autowired
    public ClassroomApiController(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    /**
     * Creates a new classroom.
     * @param classroom the classroom to be created
     * @return the classroom saved in the database
     */
    @RequestMapping(method = RequestMethod.POST)
    public Classroom createClassroom(@RequestBody Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    /**
     * Finds a classroom by code.
     * @param code the searched classroom code
     * @return the found classroom
     * @throws ClassroomNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    @Cacheable(value = "classroomCache", key = "#code")
    public Classroom getClassroomByCode(@RequestParam String code) throws ClassroomNotFoundException {
        return Optional.ofNullable(classroomRepository.findOne(code)).orElseThrow(() -> new ClassroomNotFoundException(code));
    }

    /**
     * Finds classrooms for the given search parameters.
     * @param title       the searched title (optional)
     * @param description the searched description (optional)
     * @return the found classrooms for the given search parameters
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @Cacheable(value = "classroomCache", key = "#root.args")
    public List<Classroom> getClassrooms(@RequestParam(value = "title", required = false) String title,
                                         @RequestParam(value = "description", required = false) String description) {
        List<Classroom> classrooms;
        if (title != null && description != null) {
            classrooms = classroomRepository.findClassroomsByTitleAndAndDescription(title, description);
        } else if (title != null) {
            classrooms = classroomRepository.findClassRoomsByTitleContainsIgnoreCase(title);
        } else if (description != null) {
            classrooms = classroomRepository.findClassroomsByDescriptionContainsIgnoreCase(description);
        } else {
            classrooms = classroomRepository.findAll();
        }
        return classrooms;
    }

    /**
     * Finds the classrooms for a given student id
     * @param code the searched classroom code
     * @return the found students
     */
    @RequestMapping(value = "/students", method = RequestMethod.GET)
    @Cacheable(value = "classroomStudentsCache", key = "#code")
    public List<Student> getClassroomStudents(@RequestParam String code) throws ClassroomNotFoundException {
        Classroom classroom = Optional.ofNullable(classroomRepository.findOne(code))
                                      .orElseThrow(() -> new ClassroomNotFoundException(code));
        return classroom.getStudents();
    }

    /**
     * Updates a classroom with the given data.
     * @param classroom the new data for the classroom
     * @return the updated classroom
     * @throws ClassroomNotFoundException
     */
    @RequestMapping(method = RequestMethod.PUT)
    @CacheEvict(value = "classroomCache", key = "#classroom.getCode()")
    public Classroom updateClassroom(@RequestBody Classroom classroom) throws ClassroomNotFoundException {
        Optional.ofNullable(classroomRepository.findOne(classroom.getCode()))
                .orElseThrow(() -> new ClassroomNotFoundException(classroom.getCode()));
        return classroomRepository.save(classroom);
    }

    /**
     * Deletes a classroom from the database.
     * @param code the code of the classroom to be deleted
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @CacheEvict(value = "classroomCache", key = "#code")
    public void deleteClassroom(@RequestParam String code) {
        classroomRepository.delete(code);
    }
}
