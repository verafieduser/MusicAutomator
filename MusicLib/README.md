## Introduction

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
- Set the location of music library, is saved in settings file 1
- Blacklisted artists, is saved in settings file 2 
- Delete things from library (keeps them in it but in row deleted, fill in Yes)
- Get back things that were deleted (go into "garbage bin" of all music with deleted=Yes)
- Blacklisted tags/genres? saved in settings file 2
- How large do you want your library to get? settings file 1
- How often do you want your listening data to be downloaded and compared to local library. settings file 1
- How often do you want recommendations to be downloaded? settings file 1
