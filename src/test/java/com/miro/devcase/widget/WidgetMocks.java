package com.miro.devcase.widget;

import com.miro.devcase.widget.model.Widget;

public class WidgetMocks {

    public static Widget createWidgetMock(Integer zIndex){
        Widget widget = Widget.builder().zIndex(zIndex).xIndex(33).yIndex(44).width(50).height(50).build();
        return widget;
    }

    public static Widget createWidgetMockWithId(Integer zIndex, Long id){
        Widget widget = Widget.builder().widgetId(id).zIndex(zIndex).xIndex(33).yIndex(44).width(50).height(50).build();
        return widget;
    }
}
