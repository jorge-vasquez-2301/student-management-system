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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClassroomApiControllerTests {

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
    public void testCreateClassroom() throws Exception {
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.code", is("INF-102")))
               .andExpect(jsonPath("$.title", is("ProgramacionI")))
               .andExpect(jsonPath("$.description", is("Programacion1")));
    }

    @Test
    public void testGetClassroomByCode() throws Exception {
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes/INF-102"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.code", is("INF-102")))
               .andExpect(jsonPath("$.title", is("ProgramacionI")))
               .andExpect(jsonPath("$.description", is("Programacion1")));
    }

    @Test
    public void testGetNullClassroomByCode() throws Exception {
        mockMvc.perform(get("/classes/INF-102"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(CLASSROOM_NOT_FOUND_REASON));
    }

    @Test
    public void testGetClassrooms() throws Exception {
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/classes/INF-103/ProgramacionII/Programacion2"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes"))
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
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/classes/INF-103/ProgramacionII/Programacion2"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes?title=programacioni"))
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
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/classes/INF-103/ProgramacionII/Programacion2"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes?description=programacion1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].code", is("INF-102")))
               .andExpect(jsonPath("$[0].title", is("ProgramacionI")))
               .andExpect(jsonPath("$[0].description", is("Programacion1")));
    }

    @Test
    public void testGetClassroomStudents() throws Exception {
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/Maria/Lopez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/1/assign/INF-102"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/2/assign/INF-102"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes/INF-102/students"))
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
        mockMvc.perform(post("/students/Jorge/Vasquez"))
               .andExpect(status().isOk());
        mockMvc.perform(post("/students/Maria/Lopez"))
               .andExpect(status().isOk());
        mockMvc.perform(get("/classes/INF-102/students"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(CLASSROOM_NOT_FOUND_REASON));
    }

    @Test
    public void testUpdateClassroom() throws Exception {
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(put("/classes/INF-102/NuevoProgramacionI/NuevoProgramacion1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is("INF-102")))
               .andExpect(jsonPath("$.title", is("NuevoProgramacionI")))
               .andExpect(jsonPath("$.description", is("NuevoProgramacion1")));
    }

    @Test
    public void testUpdateNullClassroom() throws Exception {
        mockMvc.perform(put("/classes/INF-102/NuevoProgramacionI/NuevoProgramacion1"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(CLASSROOM_NOT_FOUND_REASON));
    }

    @Test
    public void testDeleteClassroom() throws Exception {
        mockMvc.perform(post("/classes/INF-102/ProgramacionI/Programacion1"))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/classes/INF-102"))
               .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNullClassroom() throws Exception {
        mockMvc.perform(delete("/classes/INF-102"))
               .andExpect(status().isNotFound())
               .andExpect(status().reason(DELETE_ERROR_REASON));
    }
}
