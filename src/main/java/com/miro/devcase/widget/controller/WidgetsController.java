package com.miro.devcase.widget.controller;

import com.miro.devcase.widget.model.Widget;
import com.miro.devcase.widget.services.WidgetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api/widgets")
@Slf4j
public class WidgetsController {

    private WidgetService widgetService;

    @Autowired
    public WidgetsController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Widget> create(@RequestBody Widget widget) {
        Widget widgetCreated = widgetService.createWidget(widget);
        return new ResponseEntity<>(widgetCreated, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Widget widget) {
        Widget updatedWidget = widgetService.updateWidget(widget);
        return new ResponseEntity<>(updatedWidget, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        widgetService.deleteWidget(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Widget> findById(@PathVariable("id") Long id) {
        return widgetService.findWidgetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public Collection<Widget> getAll() {
        return widgetService.findAllWidgets();
    }





    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final String exceptionHandlerIllegalArgumentException(final IllegalArgumentException e) {
        return '"' + e.getMessage() + '"';
    }

}
