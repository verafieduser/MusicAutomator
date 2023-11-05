package com.verafied;

/**
 * Options:
 * BENBEN : .csv consisting of each occasion of listening to songs, 
 *          in the format of Artist,Album,Song,Date 
 * LASTFM : not implemented
 * SPOTIFY : not implemented
 * TIDAL : not implemented
 */
public enum DataSource { 
    BENBEN, 
    LASTFM,
    SPOTIFY,
    TIDAL }