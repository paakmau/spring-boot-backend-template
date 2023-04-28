package com.example.demo.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.dto.BookDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
class BookTests {
    @Autowired private ObjectMapper objectMapper;

    @Autowired private MockMvc mvc;

    @BeforeAll
    static void initAll() {}

    @BeforeEach
    void init() {}

    @Test
    void testPost() throws Exception {
        BookDto dto = new BookDto(null, "Book A10", "Author A10");
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
        final List<BookDto> bookDtos =
                Arrays.asList(
                        new BookDto(null, "Book B1", "Author B1"),
                        new BookDto(null, "Book B2", "Author B2"),
                        new BookDto(null, "Book B3", "Author B3"));

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

        for (BookDto dto : bookDtos)
            mvc.perform(MockMvcRequestBuilders.get("/books/{id}", dto.getId()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

        for (BookDto dto : bookDtos)
            mvc.perform(MockMvcRequestBuilders.delete("/books/{id}", dto.getId()))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

        for (BookDto dto : bookDtos)
            mvc.perform(MockMvcRequestBuilders.get("/books/{id}", dto.getId()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
    }

    @Test
    void testPut() throws Exception {
        final List<BookDto> bookDtos =
                Arrays.asList(
                        new BookDto(null, "Book C1", "Author C1"),
                        new BookDto(null, "Book C2", "Author C2"),
                        new BookDto(null, "Book C3", "Author C3"));

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

        for (BookDto dto : bookDtos) {
            MvcResult res =
                    mvc.perform(MockMvcRequestBuilders.get("/books/{id}", dto.getId()))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            BookDto resDto =
                    objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
            assertEquals(dto, resDto);
        }

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
            assertEquals(dto, resDto);
        }

        for (BookDto dto : modifiedDtos) {
            MvcResult res =
                    mvc.perform(MockMvcRequestBuilders.get("/books/{id}", dto.getId()))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            BookDto resDto =
                    objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
            assertEquals(dto, resDto);
        }
    }

    @Test
    void testGet() throws Exception {
        final List<BookDto> bookDtos =
                Arrays.asList(
                        new BookDto(null, "Book D1", "Author D1"),
                        new BookDto(null, "Book D2", "Author D2"),
                        new BookDto(null, "Book D3", "Author D3"));

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
        final List<BookDto> bookDtos =
                Arrays.asList(
                        new BookDto(null, "Book E1", "Author E1"),
                        new BookDto(null, "Book E2", "Author E2"),
                        new BookDto(null, "Book E3", "Author E3"));

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
