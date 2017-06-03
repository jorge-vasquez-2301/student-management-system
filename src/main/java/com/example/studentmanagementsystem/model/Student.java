package com.example.studentmanagementsystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * The Student class.
 * @author Jorge Vasquez
 * @since 1.8
 */
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;

    /**
     * Empty constructor for Student.
     */
    public Student() {
        // No Op
    }

    /**
     * @return the student's id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the student's id.
     * @param id the new student's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the student's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the student's first name.
     * @param firstName the new student's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the student's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the student's last name.
     * @param lastName the new student's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
