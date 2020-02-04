package com.miro.devcase.widget.repository;

import com.miro.devcase.widget.model.Widget;

import java.util.List;
import java.util.Optional;

public interface WidgetRepository {

    Widget save(Widget widget);

    Optional<Widget> findById(Long id);

    List<Widget> findAll();

    void deleteById(Long id);

    void resetMemory();

}
