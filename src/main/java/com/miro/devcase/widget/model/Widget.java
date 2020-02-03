package com.miro.devcase.widget.model;

import lombok.Builder;
import lombok.Data;
/*import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;*/
import java.io.Serializable;
import java.time.LocalDateTime;

/*@Entity*/
@Data
@Builder
public class Widget implements Serializable {

/*    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")*/
    private Long widgetId;
    private Integer xIndex;
    private Integer yIndex;
    private Integer zIndex;
    private Integer width;
    private Integer height;
    private LocalDateTime modificationDate;

}
