package com.misono.dev.iconflowview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.misono.dev.iconflowview.MainActivity;
import com.misono.dev.iconflowview.R;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubViewHolder> {
    String[] urls;
    int count;

    int currentPosition;
    View.OnClickListener subItemClickListener;

    public SubAdapter(View.OnClickListener subItemClickListener) {
        this.subItemClickListener = subItemClickListener;
    }

    public void setUp(String[] urls, int count, int currentPosition) {
        this.urls = urls;
        this.count = count;
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    @Override
    public SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubViewHolder(parent, subItemClickListener);
    }

    @Override
    public void onBindViewHolder(SubViewHolder holder, int position) {
        holder.itemView.setTag(R.id.tag_item_position, currentPosition);
        holder.itemView.setTag(R.id.tag_sub_item_position, position);
        MainActivity.loadImg(holder.imgs, urls[position]);
    }

    @Override
    public int getItemCount() {
        return urls == null ? 0 : Math.min(urls.length, count);
    }

    public static class SubViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView imgs;

        public SubViewHolder(ViewGroup recyclerView, View.OnClickListener subItemClickListener) {
            super(LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.sub_item, recyclerView, false));
            imgs = (SimpleDraweeView) itemView.findViewById(R.id.itemImg);
            itemView.setOnClickListener(subItemClickListener);
        }
    }
}