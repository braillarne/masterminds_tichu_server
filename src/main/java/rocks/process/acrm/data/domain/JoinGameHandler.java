package rocks.process.acrm.data.domain;

public class JoinGameHandler {
    public Long getProfileID() {
        return profileID;
    }

    public void setProfileID(Long profileID) {
        this.profileID = profileID;
    }

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

    private Long profileID;
    private Long gameID;
}