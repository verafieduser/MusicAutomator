CREATE TABLE "artist" 
(   
    "name" TEXT NOT NULL,
    PRIMARY KEY (name) ON CONFLICT REPLACE
)