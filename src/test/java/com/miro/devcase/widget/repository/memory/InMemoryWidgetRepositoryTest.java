package com.miro.devcase.widget.repository.memory;

import com.miro.devcase.widget.WidgetMocks;
import com.miro.devcase.widget.model.Widget;
import com.miro.devcase.widget.repository.WidgetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class InMemoryWidgetRepositoryTest {

    private WidgetRepository widgetRepository = new InMemoryWidgetRepository();

    @BeforeEach
    void setUp() {
        widgetRepository.resetMemory();
    }

    @Test
    void testSaveNewWidget(){
        Widget widgetMock = WidgetMocks.createWidgetMock(6);
        Widget savedWidget = widgetRepository.save(widgetMock);
        Assertions.assertEquals(1L, savedWidget.getWidgetId());
    }

    @Test
    void testUpdateWidget(){
        Widget widgetMock = WidgetMocks.createWidgetMock(6);
        Widget savedWidget = widgetRepository.save(widgetMock);
        savedWidget.setZIndex(9);
        savedWidget.setXIndex(100);
        savedWidget.setYIndex(100);
        savedWidget.setWidth(100);
        savedWidget.setHeight(100);
        savedWidget = widgetRepository.save(widgetMock);
        Assertions.assertEquals(1L, savedWidget.getWidgetId());
        Assertions.assertEquals(9, savedWidget.getZIndex());
        Assertions.assertEquals(100, savedWidget.getXIndex());
        Assertions.assertEquals(100, savedWidget.getYIndex());
        Assertions.assertEquals(100, savedWidget.getHeight());
        Assertions.assertEquals(100, savedWidget.getWidth());
    }

    @Test
    void testAddWidgetWithoutZetaIndex(){
        Widget widgetMock = WidgetMocks.createWidgetMock(100);
        Widget savedWidget = widgetRepository.save(widgetMock);
        Widget widgetMock2 = WidgetMocks.createWidgetMock(null);
        Widget savedWidget2 = widgetRepository.save(widgetMock2);
        Assertions.assertEquals(1L, savedWidget.getWidgetId());
        Assertions.assertEquals(2L, savedWidget2.getWidgetId());
        Assertions.assertEquals(100, savedWidget.getZIndex());
        Assertions.assertEquals(101, savedWidget2.getZIndex());
    }

    @Test
    void testAddWidgetAndShift(){
        Widget widgetMock1 = WidgetMocks.createWidgetMock(100);
        Widget widgetMock2 = WidgetMocks.createWidgetMock(101);
        Widget widgetMock3 = WidgetMocks.createWidgetMock(102);
        Widget widgetSaved1 = widgetRepository.save(widgetMock1);
        Widget widgetSaved2 = widgetRepository.save(widgetMock2);
        Widget widgetSaved3 = widgetRepository.save(widgetMock3);
        Assertions.assertEquals(1L, widgetSaved1.getWidgetId());
        Assertions.assertEquals(2L, widgetSaved2.getWidgetId());
        Assertions.assertEquals(3L, widgetSaved3.getWidgetId());

        Widget collidingWidgetMock = WidgetMocks.createWidgetMock(100);
        Widget savedCollidingMock = widgetRepository.save(collidingWidgetMock);
        Assertions.assertEquals(4L, savedCollidingMock.getWidgetId());
        Assertions.assertEquals(100, savedCollidingMock.getZIndex());
        Assertions.assertEquals(101, widgetSaved1.getZIndex());
        Assertions.assertEquals(102, widgetSaved2.getZIndex());
        Assertions.assertEquals(103, widgetSaved3.getZIndex());


    }

}