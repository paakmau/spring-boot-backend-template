package com.example.demo.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.vo.BookVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookTests {
    @Autowired private MockMvc mvc;

    private Gson gson = new Gson();

    private List<BookVo> bookVos =
            Arrays.asList(
                    new BookVo(null, "Book 1", "Author 1"),
                    new BookVo(null, "Book 2", "Author 2"),
                    new BookVo(null, "Book 3", "Author 3"));

    @BeforeAll
    static void initAll() {}

    @BeforeEach
    void init() throws Exception {
        for (BookVo vo : bookVos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.post("/books")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(gson.toJson(vo)))
                            .andExpect(MockMvcResultMatchers.status().isCreated())
                            .andReturn();
            BookVo resVo = gson.fromJson(res.getResponse().getContentAsString(), BookVo.class);
            vo.setId(resVo.getId());
        }
    }

    @Test
    void testPost() throws Exception {
        BookVo vo = new BookVo(null, "Book 10", "Author 10");
        MvcResult res =
                mvc.perform(
                                MockMvcRequestBuilders.post("/books")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(gson.toJson(vo)))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();
        BookVo resVo = gson.fromJson(res.getResponse().getContentAsString(), BookVo.class);
        assertNotNull(resVo);
    }

    @Test
    void testDelete() throws Exception {
        for (BookVo vo : bookVos)
            mvc.perform(MockMvcRequestBuilders.delete("/books/{id}", vo.getId()))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();
    }

    @Test
    void testPut() throws Exception {
        List<BookVo> modifiedVos =
                bookVos.stream()
                        .map(
                                vo ->
                                        new BookVo(
                                                vo.getId(),
                                                vo.getTitle().toUpperCase(),
                                                vo.getAuthor().toLowerCase()))
                        .collect(Collectors.toList());
        for (BookVo vo : modifiedVos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.put("/books/{id}", vo.getId())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(gson.toJson(vo)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            BookVo resVo = gson.fromJson(res.getResponse().getContentAsString(), BookVo.class);
            assertNotNull(resVo);
        }
    }

    @Test
    void testGet() throws Exception {
        for (BookVo vo : bookVos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.get("/books/{id}", vo.getId())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(gson.toJson(vo)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            BookVo resVo = gson.fromJson(res.getResponse().getContentAsString(), BookVo.class);
            assertEquals(vo, resVo);
        }
    }

    @Test
    void testGetByTitle() throws Exception {
        for (BookVo vo : bookVos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.get("/books")
                                            .param("title", vo.getTitle())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(gson.toJson(vo)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();

            Type bookVoListType = new TypeToken<ArrayList<BookVo>>() {}.getType();
            List<BookVo> resVos =
                    gson.fromJson(res.getResponse().getContentAsString(), bookVoListType);
            assertEquals(1, resVos.size());
            assertEquals(vo, resVos.get(0));
        }
    }

    @AfterEach
    void tearDown() {}

    @AfterAll
    static void tearDownAll() {}
}
