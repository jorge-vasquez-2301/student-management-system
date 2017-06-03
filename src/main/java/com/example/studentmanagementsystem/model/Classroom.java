package com.example.studentmanagementsystem.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The Classroom class.
 * @author Jorge Vasquez
 * @since 1.8
 */
@Entity
public class Classroom {

    @Id
    private String code;
    private String title;
    private String description;

    /**
     * Empty constructor for Classroom.
     */
    public Classroom() {
        // No Op
    }

    /**
     * @return the code of the class
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code of the class.
     * @param code the new code of the class
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the title of the class
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the class.
     * @param title the new title of the class
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description of the class
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the class.
     * @param description the new description of the class
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
