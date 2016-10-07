package com.example.saint.app001;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.saint.app001.MainActivity.musicList;
import static com.example.saint.app001.MusicService.mode;
import static com.example.saint.app001.MusicService.position;

public class Play extends AppCompatActivity {
    private LocalReceiver localReceiver;
    private IntentFilter intentFilter;
    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(Play.this, "REFRESH", Toast.LENGTH_SHORT).show();
            switch (intent.getAction())
            {
                case Constants.REFRESH_LAYOUT:
                    Button button1 = (Button) findViewById(R.id.PlayandPause);
//                    if (MusicService.status == 1)
                        button1.setBackground(getDrawable(R.drawable.pause));
  //                  else
    //                    button1.setBackground(getDrawable(R.drawable.play));
                    int pos = Integer.valueOf(intent.getStringExtra("pos"));
                    if (pos < 0)
                        break;
                    Music music = musicList.get(pos);
                    TextView title, artists;
                    title = (TextView) findViewById(R.id.TextTitle);
                    title.setText(music.getTitle());
                    artists = (TextView) findViewById(R.id.TextArtists);
                    artists.setText(music.getArtists());
                    break;
            }
        }
    }

    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        final Button button1 = (Button)findViewById(R.id.PlayandPause);
        Button button2 = (Button)findViewById(R.id.Last);
        Button button3 = (Button)findViewById(R.id.Next);
        final Button button4 = (Button)findViewById(R.id.ModeChange);

        if (MusicService.status == 1)
            button1.setBackground(getDrawable(R.drawable.pause));
        else
            button1.setBackground(getDrawable(R.drawable.play));

        if (mode == 0)
            button4.setBackground(getDrawable(R.drawable.roundplay));
        else
            button4.setBackground(getDrawable(R.drawable.randomplay));

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        Music music = musicList.get(position);
        TextView title, artists;
        title = (TextView) findViewById(R.id.TextTitle);
        title.setText(music.getTitle());
        artists = (TextView) findViewById(R.id.TextArtists);
        artists.setText(music.getArtists());

        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.REFRESH_LAYOUT);
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        //播放和暂停
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (MusicService.status == 0) {
                    button1.setBackground((Drawable) (getDrawable(R.drawable.pause)));
                    intent = new Intent(Constants.PLAY);
                    intent.putExtra("pos", Integer.toString(position));
                }
                else {
                    button1.setBackground((Drawable) (getDrawable(R.drawable.play)));
                    intent = new Intent(Constants.PAUSE);
                    //intent.putExtra("LAYOUT", 0);
                }
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        //上一首
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Constants.LAST);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        //下一首
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Constants.NEXT);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        //循环和随机播放
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (mode == 1) {
                    button4.setBackground((Drawable) (getDrawable(R.drawable.roundplay)));
                    mode = 0;
                    intent = new Intent(Constants.ROUND_PLAY);
                }
                else {
                    button4.setBackground((Drawable) (getDrawable(R.drawable.randomplay)));
                    mode = 1;
                    intent = new Intent(Constants.RANDOM_PLAY);
                }
                //localBroadcastManager.sendBroadcast(intent);
            }
        });
    }
    /*
    @Override
    public void onResume()
    {
        final Button button1 = (Button)findViewById(R.id.PlayandPause);
        final Button button4 = (Button)findViewById(R.id.ModeChange);
        if (MusicService.getStatus() == 0)
            button1.setBackground(getDrawable(R.drawable.play));
        else
            button1.setBackground(getDrawable(R.drawable.pause));

        if (MusicService.getmode() == 0)
            button4.setBackground(getDrawable(R.drawable.roundplay));
        else
            button4.setBackground(getDrawable(R.drawable.randomplay));
    }*/
    @Override
    public void onDestroy()
    {
        localBroadcastManager.unregisterReceiver(localReceiver);
        super.onDestroy();
    }
}
