package com.example.demo.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                                MockMvcRequestBuilders.post("/book")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();
        BookDto resDto =
                objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
        assertEquals(dto.withId(resDto.id()), resDto);
    }

    @Test
    void testDelete() throws Exception {
        List<BookDto> bookDtos = new ArrayList<>();

        for (BookDto dto :
                Arrays.asList(
                        new BookDto(null, "Book B1", "Author B1"),
                        new BookDto(null, "Book B2", "Author B2"),
                        new BookDto(null, "Book B3", "Author B3"))) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.post("/book")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(dto)))
                            .andExpect(MockMvcResultMatchers.status().isCreated())
                            .andReturn();
            BookDto resDto =
                    objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
            bookDtos.add(resDto);
        }

        for (BookDto dto : bookDtos)
            mvc.perform(MockMvcRequestBuilders.get("/book/{id}", dto.id()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

        for (BookDto dto : bookDtos)
            mvc.perform(MockMvcRequestBuilders.delete("/book/{id}", dto.id()))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

        for (BookDto dto : bookDtos)
            mvc.perform(MockMvcRequestBuilders.get("/book/{id}", dto.id()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
    }

    @Test
    void testPut() throws Exception {
        List<BookDto> bookDtos = new ArrayList<>();

        for (BookDto dto :
                Arrays.asList(
                        new BookDto(null, "Book C1", "Author C1"),
                        new BookDto(null, "Book C2", "Author C2"),
                        new BookDto(null, "Book C3", "Author C3"))) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.post("/book")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(dto)))
                            .andExpect(MockMvcResultMatchers.status().isCreated())
                            .andReturn();
            BookDto resDto =
                    objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
            bookDtos.add(resDto);
        }

        for (BookDto dto : bookDtos) {
            MvcResult res =
                    mvc.perform(MockMvcRequestBuilders.get("/book/{id}", dto.id()))
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
                                                dto.id(),
                                                dto.title().toUpperCase(),
                                                dto.author().toLowerCase()))
                        .collect(Collectors.toList());
        for (BookDto dto : modifiedDtos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.put("/book/{id}", dto.id())
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
                    mvc.perform(MockMvcRequestBuilders.get("/book/{id}", dto.id()))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            BookDto resDto =
                    objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
            assertEquals(dto, resDto);
        }
    }

    @Test
    void testGet() throws Exception {
        List<BookDto> bookDtos = new ArrayList<>();

        for (BookDto dto :
                Arrays.asList(
                        new BookDto(null, "Book D1", "Author D1"),
                        new BookDto(null, "Book D2", "Author D2"),
                        new BookDto(null, "Book D3", "Author D3"))) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.post("/book")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(dto)))
                            .andExpect(MockMvcResultMatchers.status().isCreated())
                            .andReturn();
            BookDto resDto =
                    objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
            bookDtos.add(resDto);
        }

        for (BookDto dto : bookDtos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.get("/book/{id}", dto.id())
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
        List<BookDto> bookDtos = new ArrayList<>();

        for (BookDto dto :
                Arrays.asList(
                        new BookDto(null, "Book E1", "Author E1"),
                        new BookDto(null, "Book E2", "Author E2"),
                        new BookDto(null, "Book E3", "Author E3"))) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.post("/book")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(dto)))
                            .andExpect(MockMvcResultMatchers.status().isCreated())
                            .andReturn();
            BookDto resDto =
                    objectMapper.readValue(res.getResponse().getContentAsString(), BookDto.class);
            bookDtos.add(resDto);
        }

        for (BookDto dto : bookDtos) {
            MvcResult res =
                    mvc.perform(
                                    MockMvcRequestBuilders.get("/book")
                                            .param("title", dto.title())
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
