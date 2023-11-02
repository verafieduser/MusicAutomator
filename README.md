## Introduction
An application designed to localize your music library from streaming services. 
It shall do this by downloading data of what music you listen to (from for example last.fm or spotify),
and using this, downloading music from a music downloading service. Using data from last.fm or similar, 
we can also get music recommendations of new things to download not currently in the library (to replace
streaming services recommendation algorithms). 

Project Proposal:
https://docs.google.com/document/d/1vPJnStwDyO0_BepGv6_ZgO-fzX0Kz9-4FuImK4FWxbE/edit?usp=sharing

## Current TODO:
1. Convert project into Maven project to be able to handle dependencies better.
2. Compare library to local music library to connect them and find missing songs to add
    Note: add local songs into library, and add library songs not in local to to-download
    - 2.1. Find a metadata reader for each file-extension to be supported. 
    - 2.2. For data that does not follow structure, what do?
3. Make sure library can be updated instead of just imported
     - 3.1. Solve better importing where you select songs to import and program create directory
     structure for you. Will require good metadata structure!
4. Bandcamp connectivity   
    Note: this could be easy, as bandcamp links are usually:
        "[bandname].bandcamp.com/album/[albumname with "-" as replacement for space and dots. rest is removed?]"
5. Solve downloading 
    - 5.1. API for soulseek
    - 5.2. Create file directory for each download. 
    - 5.3. Ensure metadata is correct.
    - 5.4. What to do about other files that will be present, such as images?
6. User settings:
    - 6.1. blacklisting artists, albums, songs, genres(?)
    - 6.2. preferred music quality, file type
    - 6.3. maximum storage size  
7. Data input APIs 
    - 7.1. Last.fm
    - 7.2. Spotify
    - 7.3. Tidal
8. Last.Fm Recommendations (optional, based on last.fm API success)
9. GUI

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
