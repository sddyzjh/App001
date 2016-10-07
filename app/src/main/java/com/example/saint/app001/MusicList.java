package com.example.saint.app001;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class MusicList
{
    public static List<Music> getMusicData(Context context)
    {
        String s = "<unknown>";
        List<Music> musicList = new ArrayList<Music>();
        ContentResolver resolver = context.getContentResolver();
        if (resolver != null) {
            //Cursor c = resolver.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            Cursor c = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                if (c == null) {
                    return null;
                }
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    Music music = new Music();
                    long size = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.SIZE));
//                    if (size < 5*1024*1024) {
  //                      c.moveToNext();
    //                    continue;
      //              }
                    //String name = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artists = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    //String type = c.getString(c.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
                    String url = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    if (artists.equals(s))
                        artists = "V.A.";
                    music.setSize(size);
                    //music.setName(name);
                    music.setArtists(artists);
                    //music.setType(type);
                    music.setUrl(url);
                    music.setTitle(title);
                    musicList.add(music);
                    c.moveToNext();
                }
        }
        return musicList;
    }
};

