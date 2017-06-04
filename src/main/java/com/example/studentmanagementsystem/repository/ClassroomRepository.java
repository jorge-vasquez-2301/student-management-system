package com.example.studentmanagementsystem.repository;

import com.example.studentmanagementsystem.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The ClassroomRepository interface.
 * @author Jorge Vasquez
 * @since 1.8
 */
public interface ClassroomRepository extends JpaRepository<Classroom, String> {

    /**
     * Finds classrooms searching by their title.
     * @param title the searched title
     * @return the list of found classrooms
     */
    List<Classroom> findClassRoomsByTitleContainsIgnoreCase(String title);

    /**
     * Finds classrooms searching by their description.
     * @param description the searched description
     * @return the list of found classrooms
     */
    List<Classroom> findClassroomsByDescriptionContainsIgnoreCase(String description);

    /**
     * Finds classrooms searching by their title and description.
     * @param title       the searched title
     * @param description the searched description
     * @return the list of found classrooms
     */
    @Query("SELECT classroom FROM Classroom classroom WHERE LOWER(classroom.title) LIKE LOWER(:title) AND LOWER(classroom.description) LIKE LOWER(:description)")
    List<Classroom> findClassroomsByTitleAndAndDescription(@Param("title") String title, @Param("description") String description);
}
