CREATE TABLE IF NOT EXISTS widget (
    widgetId bigint(20) NOT NULL AUTO_INCREMENT,
    xIndex INTEGER,
    yIndex INTEGER,
    zIndex INTEGER,
    width INTEGER,
    height INTEGER,
    modificationDate TIMESTAMP not null,
    PRIMARY KEY (widgetId)
);