package com.example.android.arkanoid;

public class UsersModal {
    private String points;
    private String username;

    public UsersModal( ) {

    }

    public UsersModal(String points, String username) {
        this.points = points;
        this.username = username;
    }

    public String getPoints() {
        return points;
    }



    public void setPoints(String points) {
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
