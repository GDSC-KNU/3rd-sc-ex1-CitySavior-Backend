package GDSCKNU.CitySavior.controller;


import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import GDSCKNU.CitySavior.dto.ReportRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
@SpringBootTest
class ReportsControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("파일과 함께 신고에 대한 정보를 받아들였을때 신고가 성공적으로 이루어지는지 확인")
    void 신고하기_정상시나리오() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile(
                "imgFiles",
                "testImage.png",
                MediaType.IMAGE_PNG_VALUE,
                new FileInputStream("src/test/resources/testImage/testImage.png"));

        ReportRequestDto testDescription = new ReportRequestDto(37.123456, 127.123456, "testDescription", 1, 1);

        //when
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        //then
        mockMvc.perform(multipart("/reports")
                        .file(file)
                        .content(new ObjectMapper().writeValueAsString(testDescription))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}