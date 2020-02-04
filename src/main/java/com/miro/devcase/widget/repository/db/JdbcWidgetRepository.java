package com.miro.devcase.widget.repository.db;

import com.miro.devcase.widget.model.Widget;
import com.miro.devcase.widget.repository.WidgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
@Profile("database")
public class JdbcWidgetRepository implements WidgetRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Widget save(Widget widget) {
        int widgetId = jdbcTemplate.update(
                "insert into widget (xIndex, yIndex, zIndex, width, height, modificationDate) " +
                        "values(?,?,?,?,?,?)",
                widget.getXIndex(),
                widget.getYIndex(),
                widget.getZIndex(),
                widget.getHeight(),
                widget.getWidth(),
                widget.getModificationDate());
        return null;
    }

    @Override
    public Optional<Widget> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Collection<Widget> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void resetMemory() {
    }
}
