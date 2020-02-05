package com.miro.devcase.widget.services.impl;

import com.miro.devcase.widget.model.Widget;
import com.miro.devcase.widget.repository.WidgetRepository;
import com.miro.devcase.widget.services.WidgetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WidgetServiceImpl implements WidgetService {

    private WidgetRepository widgetRepository;

    @Autowired
    public WidgetServiceImpl(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @Override
    public Widget createWidget(Widget widget) {
        if (widget == null) {
            throw new IllegalArgumentException("{\"error\":\"Widget can't be null\"}");
        }
        widget.setWidgetId(null);
        log.debug("Creating a widget {}", widget);
        return widgetRepository.save(widget);
    }

    @Override
    public Widget updateWidget(Widget widget) {
        if (widget == null) {
            throw new IllegalArgumentException("{\"error\":\"Widget can't be null\"}");
        }
        if (widget.getWidgetId() == null) {
            throw new IllegalArgumentException("{\"error\":\"Widget ID can't be null\"}");
        }
        log.debug("Updating a widget {}", widget);
        return widgetRepository.save(widget);
    }

    @Override
    public boolean deleteWidget(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("{\"error\":\"Widget ID can't be null\"}");
        }
        log.debug("Removing a widget {}", id);
        return widgetRepository.deleteById(id);
    }

    @Override
    public Optional<Widget> findWidgetById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("{\"error\":\"Widget ID can't be null\"}");
        }
        log.debug("Find widget by id {}", id);
        return widgetRepository.findById(id);
    }

    @Override
    public List<Widget> findAllWidgets() {
        log.debug("Find all Widgets");
        return widgetRepository.findAll();
    }
}
