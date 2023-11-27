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
- jdk 21
- Maven 3.8.x
## SITUATION
Refactor database into its own package / folder. Put song(+id+songtitle)/album(+id)/artist in their own folder. 

https://github.com/jpdillingham/Soulseek.NET for downloading. 

Download soulseek.net, create a wrapper for it for login, and downloads/uploads.  
Use https://github.com/dotnet/samples/tree/main/core/nativeaot/NativeLibrary for tagging what should be exported etc.
Use https://github.com/openjdk/panama-foreign/blob/foreign-jextract/doc/panama_jextract.md to exporting specifically to java. 

## Current TODO:
Difficulty levels 1(easy)-5(hard)
1. ~~Convert project into Maven project to be able to handle dependencies better.~~
2. Compare library to local music library to connect them and find missing songs to add
    
    2.1. HALF SOLVED ISSUE: Library mismatch with local due to data mismatch. How to make more forgiving?

3. Make sure library can be updated instead of just imported (1) 
    
    3.1. Solve better importing where you select songs to import and program create directory. 
    
    3.2. Import album from another local directory (2)
     structure for you. Will require good metadata structure! (3-4)

4. ~~Deletions that last (2)~~

5. ~~Bandcamp connectivity (2) 
    Note: this could be easy, as bandcamp links are usually:
        "[bandname].bandcamp.com/album/[albumname with "-" as replacement for space and dots. rest is removed?]"~~
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

- TODO: CONVERT to: https://protobuf.dev/ !!

## Conceptual problems: 
- Two artists share name
- Two versions of the same album (remastered version normal. Extended versions, and so on)
- Several versions of the same song (on different albums, as a single vs on album)
- Local library imported into database library not having accurate data 
- Data imported not being accurate

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