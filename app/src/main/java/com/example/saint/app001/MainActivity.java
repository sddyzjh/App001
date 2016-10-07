package com.example.saint.app001;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static List<Music> musicList;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;
    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(MainActivity.this, "Broadcast Received", Toast.LENGTH_SHORT).show();

        }
    }
    private IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //启动通知
        //TODO
        //NotificationBuilder.setNotification(MainActivity.this);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        //获取文件信息
        musicList = MusicList.getMusicData(this);
        //适配器
        GalleryAdapter galleryAdapter = new GalleryAdapter(this, musicList);
        //设置RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(galleryAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //启动后台服务
        if (!isWorked("MusicService")){
            final Intent intent = new Intent(MainActivity.this, MusicService.class);
            startService(intent);
        }
        //监听点击事件
        galleryAdapter.setOnItemClickListener(new GalleryAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view , int position){
//                Toast toast=Toast.makeText(getApplicationContext(), "默认的Toast", Toast.LENGTH_SHORT);
  //              toast.show();
                Intent intent1 = new Intent(Constants.PLAY);
                intent1.putExtra("pos", Integer.toString(position));
//                Toast.makeText(MainActivity.this, "Broadcast Send Ready", Toast.LENGTH_SHORT).show();
                Log.v("BROADCASTPLAY", "READY");
                localBroadcastManager.sendBroadcast(intent1);
                Log.v("BROADCASTPLAY", "GOOD");
                //Toast.makeText(MainActivity.this, "GOOD", Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                final Intent intent2 = new Intent(MainActivity.this, Play.class);
                intent2.putExtra("pos", Integer.toString(position));
                //启动播放界面
                startActivity(intent2);
            }
        });
        intentFilter = new IntentFilter();
//        intentFilter.addAction(Constants.PLAY);
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }
    @Override
    public void onDestroy()
    {
        localBroadcastManager.unregisterReceiver(localReceiver);

        super.onDestroy();
    }
    private boolean isWorked(String className) {
        ActivityManager myManager = (ActivityManager) this
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName()
                    .equals(className)) {
                return true;
            }
        }
        return false;
    }
}
