package com.example.saint.app001;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.io.IOException;

import static com.example.saint.app001.MainActivity.musicList;

public class MusicService extends Service {
    static int mode, status;
    static int position, position0;
    static MediaPlayer mediaPlayer;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;

    private EarphoneChangeReceiver earphoneChangeReceiver;
    class EarphoneChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            //Toast.makeText(context, "Earphone changes", Toast.LENGTH_SHORT).show();
            //只处理拔出
            if (intent.hasExtra("state") && intent.getIntExtra("state", 0) == 0) {
                if (status == 1)
                    mediaPlayer.pause();
                status = 0;
            }
        }
    }
    private MyReceiver myReceiver;
    class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            if (intent.getAction().equals(Constants.CHANGE)) {
                if (status == 1) {
                    mediaPlayer.pause();
                }
                else{
                    mediaPlayer.start();
                }
                Intent intent0 = new Intent(Constants.REFRESH_LAYOUT);
                intent0.putExtra("pos", "-1");
                localBroadcastManager.sendBroadcast(intent0);
                status ^= 1;
            }
        }
    }
    public MusicService() {
    }
    private Music music;
    private int[] Last;

    private LocalReceiver localReceiver;
    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case Constants.PLAY:
                    if (position == Integer.valueOf(intent.getStringExtra("pos"))) {
                        Toast.makeText(MusicService.this, musicList.get(Last[Last[0]]).getTitle()+musicList.get(Integer.valueOf(intent.getStringExtra("pos"))).getTitle(), Toast.LENGTH_SHORT).show();
                        if (status == 0) {
                            Toast.makeText(MusicService.this, "Two", Toast.LENGTH_SHORT).show();
                            mediaPlayer.start();
                            status = 1;
                        }
                        break;
                    }
                    status = 1;
                    position0 = position;
                    position = Integer.valueOf(intent.getStringExtra("pos"));
                    if (position >= 0)
                    {
                        Last[++Last[0]] = position0;
                        ResetPlayer();
                    }
                   mediaPlayer.start();
                    break;
                case Constants.PAUSE:
                    if (status == 1)
                    {
//                        Toast.makeText(MusicService.this, Integer.toString(status), Toast.LENGTH_SHORT).show();
                        status = 0;
                        mediaPlayer.pause();
                    }
                    break;
                case Constants.LAST:
                    int Pos;
                    Pos = (position - 1) % musicList.size();
                    intent = new Intent(Constants.PLAY);
                    intent.putExtra("pos", Integer.toString(Pos));
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    localBroadcastManager.sendBroadcast(intent);
                    intent = new Intent(Constants.REFRESH_LAYOUT);
                    intent.putExtra("pos", Integer.toString(Pos));
                    localBroadcastManager.sendBroadcast(intent);
                    break;
                case Constants.NEXT:
                    switch (mode){
                        case 0:
                            Pos = (position+1)%musicList.size();
                            break;
                        case 1:
                            Pos = Random(System.currentTimeMillis());
                            break;
                        default:
                            Pos = (position+1)%musicList.size();
                    }
                    intent = new Intent(Constants.PLAY);
                    intent.putExtra("pos", Integer.toString(Pos));
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    localBroadcastManager.sendBroadcast(intent);
                    intent = new Intent(Constants.REFRESH_LAYOUT);
                    intent.putExtra("pos", Integer.toString(Pos));
                    localBroadcastManager.sendBroadcast(intent);
                    break;
                case Constants.RANDOM_PLAY:
                    break;
                case Constants.ROUND_PLAY:
                    break;
            }

        }
    }
    private void ResetPlayer()
    {
        mediaPlayer.stop();
        mediaPlayer.reset();
        music = musicList.get(position);
        try{
            mediaPlayer.setDataSource(music.getUrl());
            mediaPlayer.prepare();
        }catch (IOException ex) {
            System.out.println("Exception encountered: " + ex);
        }
    }
    static int Random(long hh)
    {
        int a = 233, b = 213;
        int c = a*(int)(hh%musicList.size())+b;
        return c % musicList.size();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        //耳机拔插
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        earphoneChangeReceiver = new EarphoneChangeReceiver();
        registerReceiver(earphoneChangeReceiver, intentFilter);

        //广播接收
        localBroadcastManager = LocalBroadcastManager.getInstance(MusicService.this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PLAY);
        intentFilter.addAction(Constants.PAUSE);
        intentFilter.addAction(Constants.LAST);
        intentFilter.addAction(Constants.NEXT);
        intentFilter.addAction(Constants.RANDOM_PLAY);
        intentFilter.addAction(Constants.ROUND_PLAY);
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        //通知栏广播接收
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.CHANGE);
        intentFilter.addAction(Constants.LAST);
        intentFilter.addAction(Constants.NEXT);
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, intentFilter);

        //数组
        Last = new int[10001];
        mode = 0;//0为循环,1为随机
        status = 0;//0为暂停,1为播放

        mediaPlayer = new MediaPlayer();
        //设置播放结束后的处理
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            Intent intent;
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Toast.makeText(MusicService.this, "播放完毕", Toast.LENGTH_SHORT).show();
                intent = new Intent(Constants.NEXT);
                localBroadcastManager.sendBroadcast(intent);
                //TODO:如果Play显示在顶层需要刷新布局
                /*switch (mode)
                {
                    case 1://random
                        intent = new Intent(Constants.PLAY);
                        intent.putExtra("pos", Integer.toString(Random()));
                        localBroadcastManager.sendBroadcast(intent);
                        break;
                    case 0://round
                        intent = new Intent(Constants.NEXT);
                        localBroadcastManager.sendBroadcast(intent);
                        break;
                }*/
            }
        });
    }
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }
    public void onDestroy()
    {
        mediaPlayer.reset();
        mediaPlayer.release();
        unregisterReceiver(earphoneChangeReceiver);
        unregisterReceiver(localReceiver);
        localBroadcastManager.unregisterReceiver(localReceiver);
        super.onDestroy();
    }
}
