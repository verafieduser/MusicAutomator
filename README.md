# Music Automator
## Introduction
An application designed to localize your music library from streaming services. 
It shall do this by downloading data of what music you listen to (from for example last.fm or spotify),
and using this, downloading music from a music downloading service. Using data from last.fm or similar, 
we can also get music recommendations of new things to download not currently in the library (to replace
streaming services recommendation algorithms). 

### Project Proposal:
https://docs.google.com/document/d/1vPJnStwDyO0_BepGv6_ZgO-fzX0Kz9-4FuImK4FWxbE/edit?usp=sharing

## Dependencies etc
jdk 21
Maven 3.8.x
## SITUATION
Trying to implement hibernate. Stuck with JPA annotations. Current plan is going over to EmbeddedId, i.e., create ID classes with the keys to make them a single value to refer to. The question is how to interact with this when the object is not in the DB, but rather in java in memory etc. 

Implement REST for interacting with the database!

## Current TODO:
Difficulty levels 1(easy)-5(hard)
1. ~~Convert project into Maven project to be able to handle dependencies better.~~
2. Compare library to local music library to connect them and find missing songs to add
    
    2.1. HALF SOLVED ISSUE: Library mismatch with local due to data mismatch. How to make more forgiving?
        
    - 2.1.1. Songs like Tambourine -N- Thyme / Tambourine - N - Thyme / Tambourine-n-Thyme / Tambourine - N: Thyme (3)
    
        - Current solution: Compare lowercase titles without any symbols that arent alphanumerical.
            
        - Issues with this solution: Differences with (.feat), (for x), (by x) are still understood as different.
                
    - 2.1.2. SQLite through JBDC through ORM?  
    - https://docs.jboss.org/hibernate/stable/orm/userguide/html_single/Hibernate_User_Guide.html
            
        - 2.4.2.1. Improve schema - ID based on songtitle (cleaned version)
            
        - 2.4.2.2. Should everything be loaded into java or should things be collected as it goes? 
            - Write a note on my thinking on this and the different factors that went into it.
            - Import what is needed at the time. GUI might start with a view of all artists, doesnt mean it is necessary to have Everything from the database loaded. Caching might help out too.
            
        - 2.4.2.3. How to ensure forgiving equals in SQL? Removes a lot of benefit of the system?
                
            -  Suggested way to solve this: For a new song, get all songs from the same album from database, and compare with those
                before adding? Requires asking a query every time you need to insert something! Inefficient...

3. Make sure library can be updated instead of just imported (1) 
    
    3.1. Solve better importing where you select songs to import and program create directory. 
    
    3.2. Import album from another local directory (2)
     structure for you. Will require good metadata structure! (3-4)

4. ~~Deletions that last (2)~~
    
    4.1. Make sure deletions and getting from library works better (guaranteed result, ask for specifications if wrong?)
    
    4.2. get a list of what has been deleted, and functionality to restore deleted based on that.

5. Bandcamp connectivity (2) 
    Note: this could be easy, as bandcamp links are usually:
        "[bandname].bandcamp.com/album/[albumname with "-" as replacement for space and dots. rest is removed?]"
6. Solve downloading (5)
    
    6.1. API for soulseek

    6.2. Create file directory for each download. 

    6.3. Ensure metadata is correct.

    6.4. What to do about other files that will be present, such as images?

7. User settings: (4)
    
    7.1. blacklisting artists, albums, songs, genres(?) (2)
    
    7.2. preferred music quality, file type (2)
    
    7.3. maximum storage size (2)

8. Data input APIs 
    
    8.1. Last.fm
    
    8.2. Spotify
    
    8.3. Tidal

10. Fix server for data backups, recommendations, API connections (for safety of keys etc), manual adding from database of albums etc?
11. Last.Fm Recommendations (optional, based on last.fm API success)
12. GUI
    12.1. Async check of song paths not broken after database has been loaded
13. Figure out project structure to make it into a jar

## Data sources:
- Last.fm:
    - .csv third party: https://benjaminbenben.com/lastfm-to-csv/
    - TODO: find more .csv providers!
    - last.fm official API
- Spotify:
    - Spotify API?
    - spotify .csv third party?
- Tidal 
    - Tidal API?
    - Tidal .csv?
- Manual Library additions (artist, album?)

- Internal database is currently a .csv with Artist,Album,Songtitle,FilePath,Deleted=true/false
- TODO: CONVERT to: https://protobuf.dev/ !!

## Conceptual problems: 
- Two artists share name
- Two versions of the same album (remastered version normal. Extended versions, and so on)
- Several versions of the same song (on different albums, as a single vs on album)
- Local library imported into database library not having accurate data 
- Data from last.fm or similar not being accurate

## Development plan for usage:

Download library:
1. Collect data on music from last.fm (LibraryCollector) (either last.fm .csv or API)
2. Create a .csv of the data (LibrarySaver)
3. Compare .csv with music in local music library (MissingMusic)
4. Create .csv column of everything that is missing (LibrarySaver)
7. Download what is missing (MusicDownloader)
8. Update library of what is missing (LibrarySaver)

Download recommendations:
1. Load data of what you've listened to lately (LibraryCollector)
2. Find recommendations (LibraryCollector)
3. Filter recommendations (RecommendationAlgorithm)
3. Compare recommendations with library, if not present, add (MissingMusic, LibrarySaver)
4. Download missing music (MusicDownloader)
5. Update library of what is missing (LibrarySaver)

Other features:
- Delete things from library (keeps them in it but in row deleted, fill in Yes) (deletes local file when garbage bin emptied?)
- Get back things that were deleted (go into "garbage bin" of all music with deleted=Yes)