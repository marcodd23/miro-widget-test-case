package com.miro.devcase.widget.repository.db;

import com.miro.devcase.widget.model.Widget;
import com.miro.devcase.widget.repository.WidgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Repository
@Profile("database")
public class JdbcWidgetRepository implements WidgetRepository {

    private final int Z_STEP = 1;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public Widget save(Widget widget) {
        if (widget.getZIndex() != null) {
            Optional<Widget> collidinWidget = findByZIndex(widget.getZIndex());
            collidinWidget.ifPresent(w -> switchAllWidgetsOnZindex(w.getZIndex()));
        }
        if (widget.getWidgetId() == null) {
            long widgetId = saveWidgetInfo(widget);
            widget = findById(widgetId).get();
        } else {
            widget = updateWidgetInfo(widget);
        }
        return widget;
    }

    private Widget updateWidgetInfo(Widget widget) {
        widget.setModificationDate(LocalDateTime.now());
        jdbcTemplate.update("update widget set xIndex=?, yIndex=?, zIndex=?, width=?, height=?, modificationDate=? " +
                "where widgetId=?",
                widget.getXIndex(),
                widget.getYIndex(),
                widget.getZIndex(),
                widget.getWidth(),
                widget.getHeight(),
                widget.getModificationDate(),
                widget.getWidgetId());

        return widget;
    }

    private long saveWidgetInfo(Widget widget) {
        widget.setModificationDate(LocalDateTime.now());
        PreparedStatementCreator psc = insertPreparedStatement(widget);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private PreparedStatementCreator insertPreparedStatement(Widget widget) {
        PreparedStatementCreatorFactory psFactory = new PreparedStatementCreatorFactory(
                "insert into widget (xIndex, yIndex, zIndex, width, height, modificationDate) " +
                        "values(?,?,?,?,?,?)",
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.TIMESTAMP
        );
        psFactory.setReturnGeneratedKeys(true);
        Integer zIndex = widget.getZIndex();
        if (zIndex == null) {
            zIndex = getMaxZIndex() + Z_STEP;
        }
        return psFactory.newPreparedStatementCreator(Arrays.asList(
                widget.getXIndex(),
                widget.getYIndex(),
                zIndex,
                widget.getWidth(),
                widget.getHeight(),
                widget.getModificationDate()));
    }

    private Optional<Widget> findByZIndex(Integer zIndex) {
        Widget widgetResult = null;
        try {
            widgetResult = jdbcTemplate.queryForObject(
                    "select * from widget where zIndex=?",
                    this::mapRowToWidget, zIndex);
        } catch (EmptyResultDataAccessException e) {
            log.debug(e.getMessage());
        }
        return Optional.ofNullable(widgetResult);
    }

    private List<Widget> findAllWidgetWithZIndexBiggerOrEqual(Integer zIndex) {
        List<Widget> resultSet = jdbcTemplate.query("select * from widget where zIndex>=?",
                this::mapRowToWidget, zIndex);
        return resultSet;
    }

    private int[] switchAllWidgetsOnZindex(Integer zIndexCollision) {
        List<Widget> widgets = findAllWidgetWithZIndexBiggerOrEqual(zIndexCollision);
        return jdbcTemplate.batchUpdate(
                "update widget set zIndex = ? where widgetId=?",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setInt(1, widgets.get(i).getZIndex() + Z_STEP);
                        ps.setLong(2, widgets.get(i).getWidgetId());
                    }

                    public int getBatchSize() {
                        return widgets.size();
                    }

                });
    }

    @Override
    public Optional<Widget> findById(Long id) {
        Widget widget = jdbcTemplate.queryForObject(
                "select * from widget where widgetId=?",
                this::mapRowToWidget, id);
        return Optional.ofNullable(widget);
    }


    @Override
    public List<Widget> findAll() {
        List<Widget> result = jdbcTemplate.query("select * from widget order by zIndex",
                this::mapRowToWidget);
        return result;
    }

    @Override
    public boolean deleteById(Long id) {
        return jdbcTemplate.update("delete from widget where widgetId=?", id) == 1;
    }

    @Override
    public void resetMemory() {
    }

    private Widget mapRowToWidget(ResultSet resultSet, int rowNum) throws SQLException {
        return Widget.builder()
                .widgetId(resultSet.getLong("widgetId"))
                .xIndex(resultSet.getInt("xIndex"))
                .yIndex(resultSet.getInt("yIndex"))
                .zIndex(resultSet.getInt("zIndex"))
                .width(resultSet.getInt("width"))
                .height(resultSet.getInt("height"))
                .modificationDate(convertToLocalDateTimeViaInstant(resultSet.getTimestamp("modificationDate")))
                .build();
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Timestamp timestampToConvert) {
        return timestampToConvert.toLocalDateTime();
    }


    private Integer getMaxZIndex() {
        String sql = "select max(zIndex) from widget";
        Number number = jdbcTemplate.queryForObject(sql, null, Integer.class);
        return (number != null ? number.intValue() : 0);
    }
}
