package com.verafied;

public class SongTitle {
    private String title;
    private String id; 
    private String regex = "[^\\p{Alnum}]";


    public SongTitle(String title){
        this.title = title;
        this.id = createID(title);
    }

    private String createID(String str){
        return str.toLowerCase().replaceAll(regex, "");
    }

    @Override 
    public boolean equals(Object other){
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        SongTitle otherTitle = (SongTitle) other;
        String otherId = otherTitle.getID();
        return id.equalsIgnoreCase(otherId);
    }

    @Override
    public int hashCode(){
        return title.replaceAll(regex, "").toLowerCase().hashCode();
    }

    public String getTitle(){
        return this.title;
    }

    public String getID(){
        return this.id;
    }

    @Override
    public String toString() {
        return "{" +
            " title='" + getTitle() + "'" +
            "}";
    }
    
}
