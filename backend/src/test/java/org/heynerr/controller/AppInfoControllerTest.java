package org.heynerr.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppInfoController.class)
@TestPropertySource(properties = "app.version=1.4.0-TEST")
class AppInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAppInfo_retourneVersion_avecChampVersionAttendus() throws Exception {
        mockMvc.perform(get("/api/app-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.version").value("1.4.0-TEST"));
    }

    @Test
    void getAppInfo_retourneMap_avecStructureComplete() throws Exception {
        mockMvc.perform(get("/api/app-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.version").isString());
    }

    @Test
    void getAppInfo_endpoint_esAccessibleSansAuthentification() throws Exception {
        mockMvc.perform(get("/api/app-info"))
                .andExpect(status().isOk());
    }
}

