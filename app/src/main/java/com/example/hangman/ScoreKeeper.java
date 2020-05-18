package com.example.hangman;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Property;

public class ScoreKeeper implements Parcelable {
    private String user;
    private String list;
    private int currentScore;
    private int highScore;

    public ScoreKeeper(String user, String list) {
        this.user = user;
        this.list = list;
        this.currentScore = 0;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getList() {
        return list;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void increaseScore(int points) {
        currentScore = currentScore + points;
    }

    public void updateHighScore() {
        if (currentScore >= highScore) {
            highScore = currentScore;
        }
    }

    // Parcelable stuff
    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user);
        dest.writeString(list);
        dest.writeInt(currentScore);
        dest.writeInt(highScore);
    }

    //constructor used for parcel
    public ScoreKeeper(Parcel parcel) {
        user = parcel.readString();
        list = parcel.readString();
        currentScore = parcel.readInt();
        highScore = parcel.readInt();
    }

    //creator - used when un-parceling our parcel (creating the object)
    public static final Parcelable.Creator<ScoreKeeper> CREATOR = new Parcelable.Creator<ScoreKeeper>() {

        @Override
        public ScoreKeeper createFromParcel(Parcel parcel) {
            return new ScoreKeeper(parcel);
        }

        @Override
        public ScoreKeeper[] newArray(int size) {
            return new ScoreKeeper[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }

}
