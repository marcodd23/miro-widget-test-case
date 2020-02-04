package com.miro.devcase.widget.model;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class Widget implements Serializable {
    private Long widgetId;
    private Integer xIndex;
    private Integer yIndex;
    private Integer zIndex;
    private Integer width;
    private Integer height;
    private LocalDateTime modificationDate;

}
