package com.example.flags;

import com.example.flags.model.TypeImg;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @SneakyThrows
    public void real_test() {
        var flags = Arrays.asList("ru", "de", "col");
        var dir = "./tests";

        mockMvc.perform(
                        get("/load")
                                .param("flags", flags.get(0), flags.get(1))
                                .param("dir", dir)
                                .param("type", TypeImg.png.name())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
        ;
    }

}
