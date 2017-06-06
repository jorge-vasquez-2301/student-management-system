package com.example.studentmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.List;

/**
 * The Classroom class.
 * @author Jorge Vasquez
 * @since 1.8
 */
@Entity
@JsonIgnoreProperties("students")
public class Classroom implements Serializable{

    @Id
    private String code;
    private String title;
    private String description;

    @ManyToMany(mappedBy = "classrooms")
    private List<Student> students;

    /**
     * Empty constructor for Classroom.
     */
    public Classroom() {
        // No Op
    }

    /**
     * @return the code of the classroom
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code of the classroom.
     * @param code the new code of the classroom
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the title of the classroom
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the classroom.
     * @param title the new title of the classroom
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description of the classroom
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the classroom.
     * @param description the new description of the classroom
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the students of the classroom
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Sets the students of the classroom.
     * @param students the new students of the classroom
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
