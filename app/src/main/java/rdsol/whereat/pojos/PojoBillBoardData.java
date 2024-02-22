package rdsol.whereat.pojos;

public class PojoBillBoardData {
    private String chanellName ,artistName ;
    private int chanell_id,numberOfShows , numberOfViews;

    public PojoBillBoardData(String chanellName, String artistName, int chanell_id, int numberOfShows, int numberOfViews) {
        this.chanellName = chanellName;
        this.artistName = artistName;
        this.chanell_id = chanell_id;
        this.numberOfShows = numberOfShows;
        this.numberOfViews = numberOfViews;
    }

    public String getChanellName() {
        return chanellName;
    }

    public void setChanellName(String chanellName) {
        this.chanellName = chanellName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getChanell_id() {
        return chanell_id;
    }

    public void setChanell_id(int chanell_id) {
        this.chanell_id = chanell_id;
    }

    public int getNumberOfShows() {
        return numberOfShows;
    }

    public void setNumberOfShows(int numberOfShows) {
        this.numberOfShows = numberOfShows;
    }

    public int getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(int numberOfViews) {
        this.numberOfViews = numberOfViews;
    }
}
