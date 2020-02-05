package com.miro.devcase.widget.services;

import com.miro.devcase.widget.model.Widget;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface WidgetService {
    Widget createWidget(Widget widget);

    Widget updateWidget(Widget widget);

    boolean deleteWidget(Long id);

    Optional<Widget> findWidgetById(Long id);

    List<Widget> findAllWidgets();

}
