package com.example.saint.app001;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Saint on 2016/10/5.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> implements View.OnClickListener {
    private LayoutInflater mInflater;
    private List<Music> mDatas;
    //回调接口
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , int position);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public GalleryAdapter(Context context, List<Music> datats)
    {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        TextView title, artists;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.list_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.title = (TextView) view.findViewById(R.id.titleItem);
        viewHolder.artists = (TextView) view.findViewById(R.id.infoItem);
        view.setOnClickListener(this);
        return viewHolder;
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
    //
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        Music music = mDatas.get(i);
        viewHolder.title.setText(music.getTitle());
        viewHolder.artists.setText(music.getArtists());
        //viewHolder.artists.setText(Long.toString(music.getSize()));
        viewHolder.itemView.setTag(i);
    }
}
