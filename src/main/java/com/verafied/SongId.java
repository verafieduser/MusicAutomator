package com.verafied;

import jakarta.persistence.*;

@Embeddable
public class SongId {
    
    private String titleId;
    private Album album;
    private Artist artist;

    
}
