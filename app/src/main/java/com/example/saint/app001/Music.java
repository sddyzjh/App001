package com.example.saint.app001;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saint on 2016/10/3.
 */

public class Music
{
    public Music () {}
    private long Time;
    private String album;
    private long size;
    private String name;
    private String type;
    private String url;
    private String artists;
    private String title;
    public long getSize() {
        return size;
    }
    public String getName() {
        return name;
    }
    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }
    public String getType() {
        return type;
    }
    public String getArtists() {
        return artists;
    }
    public void setSize(long _size) {
        size = _size;
    }
    public void setName(String _name) { name = _name; }
    public void setUrl(String _url) { url = _url; }
    public void setType(String _type) { type = _type; }
    public void setArtists(String _artists){ artists = _artists; }
    public void setTitle(String _title){ title = _title; }
};
