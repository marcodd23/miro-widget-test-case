package com.miro.devcase.widget.services;

import com.miro.devcase.widget.model.Widget;

import java.util.Collection;
import java.util.Optional;

public interface WidgetService {
    Widget createWidget(Widget widget);

    Widget updateWidget(Widget widget);

    void deleteWidget(Long id);

    Optional<Widget> findWidgetById(Long id);

    Collection<Widget> findAllWidgets();

}
