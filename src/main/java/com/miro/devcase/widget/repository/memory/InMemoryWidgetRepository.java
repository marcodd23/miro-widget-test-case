package com.miro.devcase.widget.repository.memory;

import com.miro.devcase.widget.model.Widget;
import com.miro.devcase.widget.repository.WidgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Profile("memory")
public class InMemoryWidgetRepository implements WidgetRepository {

    private final int Z_STEP = 1;
    private final Lock readLock;
    private final Lock writeLock;
    private static volatile Long widgetIdentifier = 0L;

    /**
     * Map to store the Widgets [WidgetId, Widget]
     */
    private final Map<Long, Widget> widgetStorage = new HashMap();

    /**
     * A map [Z-Index, WidgetId]
     */
    private final NavigableMap<Integer, Long> zetaIndexStorage = new TreeMap<>();

    public InMemoryWidgetRepository() {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    @Override
    public Widget save(final Widget widget) {
        if (widget == null) {
            throw new IllegalArgumentException("{\"error\":\"Widget can't be null\"}");
        }
        writeLock.lock();
        try {
            if (widget.getWidgetId() == null) {
                widget.setWidgetId(generateWidgetIdentifier());
            } else {
                mergeNewWidgetAndExistingWidget(widget);
            }

            if (widget.getZIndex() == null) {
                widget.setZIndex(getMaxZeta());
            }
            //Check if there is already a widget with the same Z-index
            Long widgetIdAtSameZidx = zetaIndexStorage.get(widget.getZIndex());
            if (widgetIdAtSameZidx != null && widgetIdAtSameZidx != widget.getWidgetId().longValue()) {
                // If exist a widget at the same Z-index but with different ID
                // So I'm inserting a new widget
                insertWidgetAndShift(widget, true);
            } else {
                // If exist a widget at the same Z-index but with same ID (we update it), Or doesn't exist widget at Z-index
                // So we don't need to switch
                insertWidgetAndShift(widget, false);
            }
            return widget;
        } finally {
            writeLock.unlock();
        }
    }

    private void insertWidgetAndShift(final Widget widget, final boolean shift) {
        //Switch widgets on Z index
        if (shift) {
            Optional.ofNullable(widget)
                    .map(newWidget -> widgetStorage.get(newWidget.getWidgetId()))
                    .ifPresent(oldWidget -> {
                        log.debug("removing from ZetaIndexMap the old widget: {}", oldWidget);
                        zetaIndexStorage.remove(oldWidget.getZIndex());
                    });
            List<Integer> zetaIndexReverseKeySubList = new ArrayList<>(zetaIndexStorage
                    .navigableKeySet()
                    .subSet(widget.getZIndex(), true, zetaIndexStorage.lastKey(), true)
                    .descendingSet());

            zetaIndexReverseKeySubList.forEach(z -> {
                Long widgetIdToSwitch = zetaIndexStorage.get(z);
                Widget widgetToSwitch = widgetStorage.get(widgetIdToSwitch);
                Integer newZeta = z + Z_STEP;
                widgetToSwitch.setZIndex(newZeta);
                zetaIndexStorage.remove(z);
                zetaIndexStorage.put(newZeta, widgetIdToSwitch);
            });
        }
        widgetStorage.put(widget.getWidgetId(), widget);
        zetaIndexStorage.put(widget.getZIndex(), widget.getWidgetId());
    }

    private void mergeNewWidgetAndExistingWidget(final Widget widget) {
        Optional<Widget> optionalWidget = findById(widget.getWidgetId());
        optionalWidget.ifPresent(w -> {
            log.debug("Updating old widget: {}, with the new widget: {}", w, widget);
            if (widget.getZIndex() == null)
                widget.setZIndex(w.getZIndex());
            if (widget.getXIndex() == null)
                widget.setXIndex(w.getXIndex());
            if (widget.getYIndex() == null)
                widget.setYIndex(w.getYIndex());
            if (widget.getHeight() == null)
                widget.setHeight(w.getHeight());
            if (widget.getWidth() == null)
                widget.setWidth(w.getWidth());
        });
    }

    @Override
    public Optional<Widget> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("{\"error\":\"Widget ID can't be null\"}");
        }
        readLock.lock();
        try {
            return Optional.ofNullable(widgetStorage.get(id));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Collection<Widget> findAll() {
        readLock.lock();
        try {
            return zetaIndexStorage
                    .values()
                    .stream()
                    .map(widgetStorage::get)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("{\"error\":\"Widget ID can't be null\"}");
        }
        writeLock.lock();
        try {
            Widget removedWidget = widgetStorage.remove(id);
            zetaIndexStorage.remove(removedWidget.getZIndex());
        } finally {
            writeLock.unlock();
        }
    }

    private Integer getMaxZeta() {
        if (zetaIndexStorage.isEmpty()) {
            return 0;
        } else {
            return zetaIndexStorage.lastKey() + 1;
        }
    }

    @Override
    public void resetMemory() {
        widgetStorage.clear();
        zetaIndexStorage.clear();
        widgetIdentifier = 0L;
    }

    private synchronized static Long generateWidgetIdentifier() {
        return ++widgetIdentifier;
    }
}
