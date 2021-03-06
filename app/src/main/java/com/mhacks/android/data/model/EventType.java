package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Omkar Moghe on 10/13/2014.
 */
@ParseClassName("EventType")
public class EventType extends ParseObject implements Parcelable{

    public static final String COLOR_COL = "color";
    public static final String TITLE_COL = "title";

    public String getColor() {
        return getString(COLOR_COL);
    }

    public void setColor(String color) {
        put(COLOR_COL, color);
    }

    public String getTitle() {
        return getString(TITLE_COL);
    }

    public void setTitle(String title) {
        put(TITLE_COL, title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getObjectId());
        parcel.writeString(getColor());
        parcel.writeString(getTitle());
    }

    public static final Creator<EventType> CREATOR = new Creator<EventType>() {
        @Override
        public EventType createFromParcel(Parcel source) {
            return new EventType(source);
        }

        @Override
        public EventType[] newArray(int size) {
            return new EventType[size];
        }
    };

    private EventType(Parcel source) {
        setObjectId(source.readString());
        setColor(source.readString());
        setTitle(source.readString());
    }

}
