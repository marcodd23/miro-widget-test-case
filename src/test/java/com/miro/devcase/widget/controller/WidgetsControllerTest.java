package com.miro.devcase.widget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.devcase.widget.WidgetApplication;
import com.miro.devcase.widget.WidgetMocks;
import com.miro.devcase.widget.config.WidgetAppConfiguration;
import com.miro.devcase.widget.model.Widget;
import com.miro.devcase.widget.repository.WidgetRepository;
import com.miro.devcase.widget.repository.memory.InMemoryWidgetRepository;
import com.miro.devcase.widget.services.WidgetService;
import com.miro.devcase.widget.services.impl.WidgetServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = WidgetsController.class)
class WidgetsControllerTest {

    private static final String BASE_URL = "/api/widgets";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private WidgetService widgetService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createWidgetTest() throws Exception {
        Widget widgetMock = WidgetMocks.createWidgetMock(6);
        Widget widgetMockAfter = WidgetMocks.createWidgetMockWithId(6, 1L);
        Mockito.when(widgetService.createWidget(Mockito.any())).thenReturn(widgetMockAfter);
        String contentAsString = mockMvc.perform(post(BASE_URL)
                .content(objectMapper.writeValueAsBytes(widgetMock))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
        Widget widget = objectMapper.readValue(contentAsString, Widget.class);
        Assertions.assertEquals(6, widget.getZIndex());
    }

    @Test
    void createWidgetTest_emptybody_throw_exception() throws Exception {
        Mockito.when(widgetService.createWidget(Mockito.any())).thenThrow(new IllegalArgumentException("{\"error\":\"Widget can't be null\"}"));
        mockMvc.perform(post(BASE_URL))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    void updateWidgetTest() throws Exception {
        Widget widgetMock = WidgetMocks.createWidgetMockWithId(6, 1L);
        Widget widgetMockAfter = WidgetMocks.createWidgetMockWithId(7, 1L);
        Mockito.when(widgetService.updateWidget(Mockito.any())).thenReturn(widgetMockAfter);
        String contentAsString = mockMvc.perform(put(BASE_URL)
                .content(objectMapper.writeValueAsBytes(widgetMock))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
        Widget widget = objectMapper.readValue(contentAsString, Widget.class);
        Assertions.assertEquals(7, widget.getZIndex());
    }
}