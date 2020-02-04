CREATE TABLE IF NOT EXISTS widget (
    widgetId LONG NOT NULL AUTO_INCREMENT,
    xIndex INTEGER,
    yIndex INTEGER,
    zIndex INTEGER not null,
    width INTEGER,
    height INTEGER,
    modificationDate DATE not null,
    PRIMARY KEY (widgetId)
);