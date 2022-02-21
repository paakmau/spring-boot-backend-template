package com.example.demo.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.dto.BookDto;
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

    private List<BookDto> bookVos =
            Arrays.asList(
                    new BookDto(null, "Book 1", "Author 1"),
                    new BookDto(null, "Book 2", "Author 2"),
                    new BookDto(null, "Book 3", "Author 3"));

    @BeforeAll
    static void initAll() {}

    @BeforeEach
    void init() throws Exception {
        for (BookDto dto : bookVos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.post("/books")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(gson.toJson(dto)))
                            .andExpect(MockMvcResultMatchers.status().isCreated())
                            .andReturn();
            BookDto resVo = gson.fromJson(res.getResponse().getContentAsString(), BookDto.class);
            dto.setId(resVo.getId());
        }
    }

    @Test
    void testPost() throws Exception {
        BookDto dto = new BookDto(null, "Book 10", "Author 10");
        MvcResult res =
                mvc.perform(
                                MockMvcRequestBuilders.post("/books")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(gson.toJson(dto)))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();
        BookDto resVo = gson.fromJson(res.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(resVo);
    }

    @Test
    void testDelete() throws Exception {
        for (BookDto dto : bookVos)
            mvc.perform(MockMvcRequestBuilders.delete("/books/{id}", dto.getId()))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();
    }

    @Test
    void testPut() throws Exception {
        List<BookDto> modifiedVos =
                bookVos.stream()
                        .map(
                                dto ->
                                        new BookDto(
                                                dto.getId(),
                                                dto.getTitle().toUpperCase(),
                                                dto.getAuthor().toLowerCase()))
                        .collect(Collectors.toList());
        for (BookDto dto : modifiedVos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.put("/books/{id}", dto.getId())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(gson.toJson(dto)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            BookDto resVo = gson.fromJson(res.getResponse().getContentAsString(), BookDto.class);
            assertNotNull(resVo);
        }
    }

    @Test
    void testGet() throws Exception {
        for (BookDto dto : bookVos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.get("/books/{id}", dto.getId())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(gson.toJson(dto)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            BookDto resVo = gson.fromJson(res.getResponse().getContentAsString(), BookDto.class);
            assertEquals(dto, resVo);
        }
    }

    @Test
    void testGetByTitle() throws Exception {
        for (BookDto dto : bookVos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.get("/books")
                                            .param("title", dto.getTitle())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(gson.toJson(dto)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();

            Type bookVoListType = new TypeToken<ArrayList<BookDto>>() {}.getType();
            List<BookDto> resVos =
                    gson.fromJson(res.getResponse().getContentAsString(), bookVoListType);
            assertEquals(1, resVos.size());
            assertEquals(dto, resVos.get(0));
        }
    }

    @AfterEach
    void tearDown() {}

    @AfterAll
    static void tearDownAll() {}
}
