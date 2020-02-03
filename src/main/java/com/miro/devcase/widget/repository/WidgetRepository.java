package com.miro.devcase.widget.repository;

import com.miro.devcase.widget.model.Widget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;

public interface WidgetRepository {

    Widget save(Widget widget);

    Optional<Widget> findById(Long id);

    Collection<Widget> findAll();

    void deleteById(Long id);

    void resetMemory();

}
