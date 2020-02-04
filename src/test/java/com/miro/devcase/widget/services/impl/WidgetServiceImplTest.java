package com.miro.devcase.widget.services.impl;

import com.miro.devcase.widget.WidgetMocks;
import com.miro.devcase.widget.model.Widget;
import com.miro.devcase.widget.repository.WidgetRepository;
import com.miro.devcase.widget.services.WidgetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class WidgetServiceImplTest {

    @MockBean
    private WidgetRepository widgetRepository;

    private WidgetService widgetService;

    @BeforeEach
    void setUp() {
        widgetService = new WidgetServiceImpl(widgetRepository);
    }

    @Test
    void testCreateWidget_WidgetNULL_throwException(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            widgetService.createWidget(null);
        });
    }

    @Test
    void testCreateWidget(){
        Widget widget = WidgetMocks.createWidgetMock(6);
        Mockito.doReturn(widget).when(widgetRepository).save(Mockito.any());
        Assertions.assertNotNull(widgetService.createWidget(widget));
    }

    @Test
    void testUpdateWidget_WidgetNULL_throwException(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            widgetService.updateWidget(null);
        });
    }

    @Test
    void testUpdateWidget_WidgetIdNULL_throwException(){
        Widget widget = WidgetMocks.createWidgetMock(6);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            widgetService.updateWidget(widget);
        });
    }

    @Test
    void testUpdateWidget(){
        Widget widget = WidgetMocks.createWidgetMockWithId(6, 1L);
        Mockito.doReturn(widget).when(widgetRepository).save(Mockito.any());
        Assertions.assertNotNull(widgetService.updateWidget(widget));
    }

    @Test
    void testDeleteWidget_WidgetIdNULL_throwException(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            widgetService.deleteWidget(null);
        });
    }

    @Test
    void testFindWidgetByID_WidgetIdNULL_throwException(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            widgetService.findWidgetById(null);
        });
    }

    @Test
    void testFindWidgetByID(){
        Widget widget = WidgetMocks.createWidgetMockWithId(6, 1L);
        Mockito.doReturn(Optional.of(widget)).when(widgetRepository).findById(1L);
        Assertions.assertNotNull(widgetService.findWidgetById(1L));
    }

    @Test
    void testFindAllWidget(){
        Widget widget1 = WidgetMocks.createWidgetMockWithId(6, 1L);
        Widget widget2 = WidgetMocks.createWidgetMockWithId(6, 2L);
        Mockito.doReturn(Arrays.asList(widget1, widget2)).when(widgetRepository).findAll();
        Assertions.assertNotNull(widgetService.findAllWidgets());
        Assertions.assertEquals(2, widgetService.findAllWidgets().size());
    }
}