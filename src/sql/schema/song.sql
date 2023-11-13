CREATE TABLE song
(
    id TEXT NOT NULL,
    title TEXT NOT NULL, 
    artist TEXT NOT NULL,
    album TEXT NOT NULL,
    path TEXT NOT NULL,
    deleted TEXT NOT NULL,
    PRIMARY KEY (id, artist, album) ON CONFLICT REPLACE, 
    FOREIGN KEY (album, artist) REFERENCES album(name, artist) 
)