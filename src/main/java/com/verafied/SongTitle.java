package com.verafied;

public class SongTitle {
    private String title;
    private String regex = "[^\\p{Alnum}]";


    public SongTitle(String title){
        this.title = title;
    }

    @Override 
    public boolean equals(Object other){
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        SongTitle otherTitle = (SongTitle) other;
        String otherStr = otherTitle.getTitle();
        if(otherStr.equalsIgnoreCase(title)){
            return true; //If they are identical already, no need to do compute
        }
        String thisTitle = title.replaceAll(regex, "");
        otherStr = otherStr.replaceAll(regex, "");
        return thisTitle.equalsIgnoreCase(otherStr);
    }

    @Override
    public int hashCode(){
        return title.replaceAll(regex, "").toLowerCase().hashCode();
    }

    public String getTitle(){
        return this.title;
    }

    @Override
    public String toString() {
        return "{" +
            " title='" + getTitle() + "'" +
            "}";
    }
    
}
