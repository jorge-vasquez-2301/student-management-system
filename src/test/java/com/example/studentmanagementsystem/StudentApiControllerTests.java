package com.example.studentmanagementsystem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentManagementSystemApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StudentApiControllerTests {

    private static final String STUDENT_NOT_FOUND_REASON = "The student was not found in the system";
    private static final String CLASSROOM_NOT_FOUND_REASON = "The classroom was not found in the system";
    private static final String DELETE_ERROR_REASON = "There is no item to delete";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testCreateStudent() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.firstName", is("Jorge")))
               .andExpect(jsonPath("$.lastName", is("Vasquez")))
               .andExpect(jsonPath("$.classrooms", nullValue()));
    }

    @Test
    public void testAssignStudentToClassroom() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/1/class/INF-102"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.firstName", is("Jorge")))
               .andExpect(jsonPath("$.lastName", is("Vasquez")))
               .andExpect(jsonPath("$.classrooms", hasSize(1)))
               .andExpect(jsonPath("$.classrooms[0].code", is("INF-102")))
               .andExpect(jsonPath("$.classrooms[0].title", is("ProgramacionI")))
               .andExpect(jsonPath("$.classrooms[0].description", is("Programacion1")));

    }

    @Test
    public void testAssignNullStudentToClassroom() throws Exception {
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/1/class/INF-102"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(STUDENT_NOT_FOUND_REASON));
    }

    @Test
    public void testAssignStudentToNullClassroom() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/1/class/INF-102"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(CLASSROOM_NOT_FOUND_REASON));
    }

    @Test
    public void testAssignNullStudentToNullClassroom() throws Exception {
        mockMvc.perform(post("/students/1/class/INF-102"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(STUDENT_NOT_FOUND_REASON));
    }

    @Test
    public void testRemoveStudentFromClassroom() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/1/class/INF-102"))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/students/1/class/INF-102"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/students/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.firstName", is("Jorge")))
               .andExpect(jsonPath("$.lastName", is("Vasquez")))
               .andExpect(jsonPath("$.classrooms", empty()));
    }

    @Test
    public void testRemoveUnassignedStudentFromClassroom() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/students/1/class/INF-102"))
               .andExpect(status().isOk());
    }

    @Test
    public void testRemoveNullStudentFromClassroom() throws Exception {
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/students/1/class/INF-102"))
               .andExpect(status().reason(STUDENT_NOT_FOUND_REASON));
    }

    @Test
    public void testRemoveStudentFromNullClassroom() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/students/1/class/INF-102"))
               .andExpect(status().reason(CLASSROOM_NOT_FOUND_REASON));
    }

    @Test
    public void testGetStudentById() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/students/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.firstName", is("Jorge")))
               .andExpect(jsonPath("$.lastName", is("Vasquez")))
               .andExpect(jsonPath("$.classrooms", empty()));
    }

    @Test
    public void testGetNullStudentById() throws Exception {
        mockMvc.perform(get("/students/1"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(STUDENT_NOT_FOUND_REASON));
    }

    @Test
    public void testGetStudents() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/Maria/Lopez"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/students"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].firstName", is("Jorge")))
               .andExpect(jsonPath("$[0].lastName", is("Vasquez")))
               .andExpect(jsonPath("$[0].classrooms", empty()))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].firstName", is("Maria")))
               .andExpect(jsonPath("$[1].lastName", is("Lopez")))
               .andExpect(jsonPath("$[1].classrooms", empty()));
    }

    @Test
    public void testGetStudentsByFirstName() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/Maria/Lopez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/Jorge/Lopez"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/students?firstName=jorge"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].firstName", is("Jorge")))
               .andExpect(jsonPath("$[0].lastName", is("Vasquez")))
               .andExpect(jsonPath("$[0].classrooms", empty()))
               .andExpect(jsonPath("$[1].id", is(3)))
               .andExpect(jsonPath("$[1].firstName", is("Jorge")))
               .andExpect(jsonPath("$[1].lastName", is("Lopez")))
               .andExpect(jsonPath("$[1].classrooms", empty()));
    }

    @Test
    public void testGetStudentsByLastName() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/Maria/Lopez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/Jorge/Lopez"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/students?lastName=lopez"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(2)))
               .andExpect(jsonPath("$[0].firstName", is("Maria")))
               .andExpect(jsonPath("$[0].lastName", is("Lopez")))
               .andExpect(jsonPath("$[0].classrooms", empty()))
               .andExpect(jsonPath("$[1].id", is(3)))
               .andExpect(jsonPath("$[1].firstName", is("Jorge")))
               .andExpect(jsonPath("$[1].lastName", is("Lopez")))
               .andExpect(jsonPath("$[1].classrooms", empty()));
    }

    @Test
    public void testGetStudentsByFirstAndLastName() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/Maria/Lopez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/Jorge/Lopez"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/students?firstName=jorge&lastName=lopez"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].id", is(3)))
               .andExpect(jsonPath("$[0].firstName", is("Jorge")))
               .andExpect(jsonPath("$[0].lastName", is("Lopez")))
               .andExpect(jsonPath("$[0].classrooms", empty()));
    }

    @Test
    public void testGetStudentClassrooms() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/classes/INF-103/ProgramacionII/Programacion2"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/1/class/INF-102"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/1/class/INF-103"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/students/1/classes"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].code", is("INF-102")))
               .andExpect(jsonPath("$[0].title", is("ProgramacionI")))
               .andExpect(jsonPath("$[0].description", is("Programacion1")))
               .andExpect(jsonPath("$[1].code", is("INF-103")))
               .andExpect(jsonPath("$[1].title", is("ProgramacionII")))
               .andExpect(jsonPath("$[1].description", is("Programacion2")));
    }

    @Test
    public void testGetNullStudentClassrooms() throws Exception {
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/classes/INF-103/ProgramacionII/Programacion1I"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/students/1/classes"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(STUDENT_NOT_FOUND_REASON));
    }

    @Test
    public void testUpdateStudent() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(put("/students/1/Jose/Perez"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.firstName", is("Jose")))
               .andExpect(jsonPath("$.lastName", is("Perez")))
               .andExpect(jsonPath("$.classrooms", nullValue()));
    }

    @Test
    public void testUpdateNullStudent() throws Exception {
        mockMvc.perform(put("/students/1/Jose/Perez"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(STUDENT_NOT_FOUND_REASON));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/students/1"))
               .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNullStudent() throws Exception {
        mockMvc.perform(delete("/students/1"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(DELETE_ERROR_REASON));
    }
}
