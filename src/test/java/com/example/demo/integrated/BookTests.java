package com.example.demo.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.dto.BookDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Autowired private ObjectMapper objectMapper;

    @Autowired private MockMvc mvc;

    private final List<BookDto> bookDtos =
            Arrays.asList(
                    new BookDto(null, "Book 1", "Author 1"),
                    new BookDto(null, "Book 2", "Author 2"),
                    new BookDto(null, "Book 3", "Author 3"));

    @BeforeAll
    static void initAll() {}

    @BeforeEach
    void init() throws Exception {
        for (BookDto dto : bookDtos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.post("/books")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(dto)))
                            .andExpect(MockMvcResultMatchers.status().isCreated())
                            .andReturn();
            BookDto resDto =
                    objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
            dto.setId(resDto.getId());
        }
    }

    @Test
    void testPost() throws Exception {
        BookDto dto = new BookDto(null, "Book 10", "Author 10");
        MvcResult res =
                mvc.perform(
                                MockMvcRequestBuilders.post("/books")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();
        BookDto resDto =
                objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(resDto);
    }

    @Test
    void testDelete() throws Exception {
        for (BookDto dto : bookDtos)
            mvc.perform(MockMvcRequestBuilders.delete("/books/{id}", dto.getId()))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();
    }

    @Test
    void testPut() throws Exception {
        List<BookDto> modifiedDtos =
                bookDtos.stream()
                        .map(
                                dto ->
                                        new BookDto(
                                                dto.getId(),
                                                dto.getTitle().toUpperCase(),
                                                dto.getAuthor().toLowerCase()))
                        .collect(Collectors.toList());
        for (BookDto dto : modifiedDtos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.put("/books/{id}", dto.getId())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(dto)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            BookDto resDto =
                    objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
            assertNotNull(resDto);
        }
    }

    @Test
    void testGet() throws Exception {
        for (BookDto dto : bookDtos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.get("/books/{id}", dto.getId())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(dto)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            BookDto resDto =
                    objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
            assertEquals(dto, resDto);
        }
    }

    @Test
    void testGetByTitle() throws Exception {
        for (BookDto dto : bookDtos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.get("/books")
                                            .param("title", dto.getTitle())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(dto)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();

            List<BookDto> resDtos =
                    objectMapper.readValue(
                            res.getResponse().getContentAsString(),
                            new TypeReference<ArrayList<BookDto>>() {});
            assertEquals(1, resDtos.size());
            assertEquals(dto, resDtos.get(0));
        }
    }

    @AfterEach
    void tearDown() {}

    @AfterAll
    static void tearDownAll() {}
}
