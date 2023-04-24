package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmController filmController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(filmController).isNotNull();
    }


    @Test
    public void shouldReturnStatusOKonAllFilmsOrEmptyList() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andDo(print())
                .andExpect(status().isOk());
    }


}