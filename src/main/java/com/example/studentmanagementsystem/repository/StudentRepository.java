package com.example.studentmanagementsystem.repository;

import com.example.studentmanagementsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The StudentRepository interface.
 * @author Jorge Vasquez
 * @since 1.8
 */
public interface StudentRepository extends JpaRepository<Student, Integer> {

    /**
     * Finds students searching by their first name.
     * @param firstName the searched first name
     * @return the list of found students
     */
    List<Student> findStudentsByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Finds students searching by their last name.
     * @param lastName the searched last name
     * @return the list of found students
     */
    List<Student> findStudentsByLastNameContainingIgnoreCase(String lastName);

    /**
     * Finds students searching by their first and last name.
     * @param firstName the searched first name
     * @param lastName  the searched last name
     * @return the list of found students
     */
    @Query("SELECT student FROM Student student WHERE LOWER(student.firstName) LIKE LOWER(:firstName) AND LOWER(student.lastName) LIKE LOWER(:lastName)")
    List<Student> findStudentsByFirstAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
