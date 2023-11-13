CREATE TABLE "album"
(
    "name" TEXT NOT NULL,
    "artist" TEXT NOT NULL,
    PRIMARY KEY (name, artist) ON CONFLICT REPLACE, 
    FOREIGN KEY (artist) REFERENCES artist(name) 
)