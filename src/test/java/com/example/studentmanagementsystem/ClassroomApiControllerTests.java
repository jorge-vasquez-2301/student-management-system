package com.example.studentmanagementsystem;

import com.example.studentmanagementsystem.model.Classroom;
import com.example.studentmanagementsystem.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentManagementSystemApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClassroomApiControllerTests {

    private static final String CLASSROOM_NOT_FOUND_REASON = "The classroom was not found in the system";
    private static final String DELETE_ERROR_REASON = "There is no item to delete";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;
    private JacksonTester<Student> studentJacksonTester;
    private JacksonTester<Classroom> classroomJacksonTester;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void testCreateClassroom() throws Exception {
        Classroom classroom = new Classroom();
        classroom.setCode("INF-102");
        classroom.setTitle("ProgramacionI");
        classroom.setDescription("Programacion1");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom).getJson()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.code", is("INF-102")))
               .andExpect(jsonPath("$.title", is("ProgramacionI")))
               .andExpect(jsonPath("$.description", is("Programacion1")));
    }

    @Test
    public void testGetClassroomByCode() throws Exception {
        Classroom classroom = new Classroom();
        classroom.setCode("INF-102");
        classroom.setTitle("ProgramacionI");
        classroom.setDescription("Programacion1");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom).getJson()))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes?code=INF-102"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.code", is("INF-102")))
               .andExpect(jsonPath("$.title", is("ProgramacionI")))
               .andExpect(jsonPath("$.description", is("Programacion1")));
    }

    @Test
    public void testGetNullClassroomByCode() throws Exception {
        mockMvc.perform(get("/classes?code=INF-102"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(CLASSROOM_NOT_FOUND_REASON));
    }

    @Test
    public void testGetClassrooms() throws Exception {
        Classroom classroom1 = new Classroom();
        classroom1.setCode("INF-102");
        classroom1.setTitle("ProgramacionI");
        classroom1.setDescription("Programacion1");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom1).getJson()))
               .andExpect(status().isOk());
        Classroom classroom2 = new Classroom();
        classroom2.setCode("INF-103");
        classroom2.setTitle("ProgramacionII");
        classroom2.setDescription("Programacion2");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom2).getJson()))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes/search"))
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
    public void testGetClassroomsByTitle() throws Exception {
        Classroom classroom1 = new Classroom();
        classroom1.setCode("INF-102");
        classroom1.setTitle("ProgramacionI");
        classroom1.setDescription("Programacion1");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom1).getJson()))
               .andExpect(status().isOk());
        Classroom classroom2 = new Classroom();
        classroom2.setCode("INF-103");
        classroom2.setTitle("ProgramacionII");
        classroom2.setDescription("Programacion2");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom2).getJson()))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes/search?title=programacioni"))
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
    public void testGetClassroomsByDescription() throws Exception {
        Classroom classroom1 = new Classroom();
        classroom1.setCode("INF-102");
        classroom1.setTitle("ProgramacionI");
        classroom1.setDescription("Programacion1");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom1).getJson()))
               .andExpect(status().isOk());
        Classroom classroom2 = new Classroom();
        classroom2.setCode("INF-103");
        classroom2.setTitle("ProgramacionII");
        classroom2.setDescription("Programacion2");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom2).getJson()))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes/search?description=programacion1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].code", is("INF-102")))
               .andExpect(jsonPath("$[0].title", is("ProgramacionI")))
               .andExpect(jsonPath("$[0].description", is("Programacion1")));
    }

    @Test
    public void testGetClassroomsByTitleAndDescription() throws Exception {
        Classroom classroom1 = new Classroom();
        classroom1.setCode("INF-102");
        classroom1.setTitle("ProgramacionI");
        classroom1.setDescription("Programacion1");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom1).getJson()))
               .andExpect(status().isOk());
        Classroom classroom2 = new Classroom();
        classroom2.setCode("INF-103");
        classroom2.setTitle("ProgramacionII");
        classroom2.setDescription("Programacion2");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom2).getJson()))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes/search?title=programacioni&description=programacion1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].code", is("INF-102")))
               .andExpect(jsonPath("$[0].title", is("ProgramacionI")))
               .andExpect(jsonPath("$[0].description", is("Programacion1")));
    }

    @Test
    public void testGetClassroomStudents() throws Exception {
        Student student1 = new Student();
        student1.setFirstName("Jorge");
        student1.setLastName("Vasquez");
        mockMvc.perform(post("/students").contentType(APPLICATION_JSON_UTF8)
                                         .content(studentJacksonTester.write(student1).getJson()))
               .andExpect(status().isOk());
        Student student2 = new Student();
        student2.setFirstName("Maria");
        student2.setLastName("Lopez");
        mockMvc.perform(post("/students").contentType(APPLICATION_JSON_UTF8)
                                         .content(studentJacksonTester.write(student2).getJson()))
               .andExpect(status().isOk());
        Classroom classroom = new Classroom();
        classroom.setCode("INF-102");
        classroom.setTitle("ProgramacionI");
        classroom.setDescription("Programacion1");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom).getJson()))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/1/class/INF-102"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/2/class/INF-102"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes/students?code=INF-102"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].firstName", is("Jorge")))
               .andExpect(jsonPath("$[0].lastName", is("Vasquez")))
               .andExpect(jsonPath("$[0].classrooms", hasSize(1)))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].firstName", is("Maria")))
               .andExpect(jsonPath("$[1].lastName", is("Lopez")))
               .andExpect(jsonPath("$[1].classrooms", hasSize(1)));
    }

    @Test
    public void testGetNullClassroomStudents() throws Exception {
        Student student1 = new Student();
        student1.setFirstName("Jorge");
        student1.setLastName("Vasquez");
        mockMvc.perform(post("/students").contentType(APPLICATION_JSON_UTF8)
                                         .content(studentJacksonTester.write(student1).getJson()))
               .andExpect(status().isOk());
        Student student2 = new Student();
        student2.setFirstName("Maria");
        student2.setLastName("Lopez");
        mockMvc.perform(post("/students").contentType(APPLICATION_JSON_UTF8)
                                         .content(studentJacksonTester.write(student2).getJson()))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes/students?code=INF-102"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(CLASSROOM_NOT_FOUND_REASON));
    }

    @Test
    public void testUpdateClassroom() throws Exception {
        Classroom classroom = new Classroom();
        classroom.setCode("INF-102");
        classroom.setTitle("ProgramacionI");
        classroom.setDescription("Programacion1");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom).getJson()))
               .andExpect(status().isOk());
        classroom.setTitle("NuevoProgramacionI");
        classroom.setDescription("NuevoProgramacion1");
        mockMvc.perform(put("/classes").contentType(APPLICATION_JSON_UTF8)
                                       .content(classroomJacksonTester.write(classroom).getJson()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is("INF-102")))
               .andExpect(jsonPath("$.title", is("NuevoProgramacionI")))
               .andExpect(jsonPath("$.description", is("NuevoProgramacion1")));
    }

    @Test
    public void testUpdateNullClassroom() throws Exception {
        Classroom classroom = new Classroom();
        classroom.setCode("INF-102");
        classroom.setTitle("ProgramacionI");
        classroom.setDescription("Programacion1");
        mockMvc.perform(put("/classes").contentType(APPLICATION_JSON_UTF8)
                                       .content(classroomJacksonTester.write(classroom).getJson()))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(CLASSROOM_NOT_FOUND_REASON));
    }

    @Test
    public void testDeleteClassroom() throws Exception {
        Classroom classroom = new Classroom();
        classroom.setCode("INF-102");
        classroom.setTitle("ProgramacionI");
        classroom.setDescription("Programacion1");
        mockMvc.perform(post("/classes").contentType(APPLICATION_JSON_UTF8)
                                        .content(classroomJacksonTester.write(classroom).getJson()))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/classes?code=INF-102"))
               .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNullClassroom() throws Exception {
        mockMvc.perform(delete("/classes?code=INF-102"))
               .andExpect(status().isNoContent())
               .andExpect(status().reason(DELETE_ERROR_REASON));
    }
}
