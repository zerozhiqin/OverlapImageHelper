package com.misono.dev.iconflowview.adapter;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.misono.dev.iconflowview.MainActivity;
import com.misono.dev.iconflowview.OverlapImageHelper;
import com.misono.dev.iconflowview.R;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    String[] urls;
    View.OnClickListener itemClickListener, subItemClickListener;

    public MainAdapter(String[] urls, View.OnClickListener itemClickListener, View.OnClickListener subItemClickListener) {
        this.itemClickListener = itemClickListener;
        this.subItemClickListener = subItemClickListener;
        this.urls = urls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, itemClickListener, subItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.v("INIT", "setting : " + position);
        holder.itemView.setTag(R.id.tag_item_position, position);
        holder.iconLayout.removeAllViews();
        OverlapImageHelper.addImageIntoViewGroup(
                holder.itemView.getContext(),
                holder.iconLayout,
                8,
                42,
                position % 5 + 1,
                new OverlapImageHelper.ImageProcessor() {
                    @Override
                    public void process(SimpleDraweeView imageView, int position) {
                        if (position == 4) {
                            Uri uri = new Uri.Builder()
                                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                                    .path(String.valueOf(R.drawable.ic_more_horiz_blue_700_24dp))
                                    .build();
                            imageView.setImageURI(uri);
                        } else {
                            int img = (int) (Math.random() * urls.length);
                            MainActivity.loadImg(imageView, urls[img]);
                        }
                    }
                }
        );

        MainActivity.loadImg(holder.headerIcon, urls[position]);
//            loadImg(holder.image, urls[position]);
        holder.headerText.setText(urls[position]);
        SubAdapter subAdapter = (SubAdapter) holder.subRecycler.getAdapter();
        subAdapter.setUp(urls, (int) (2 + (Math.random() * 5)), position);
    }

    @Override
    public int getItemCount() {
        return urls.length;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout iconLayout = null;
        SimpleDraweeView headerIcon;
        RecyclerView subRecycler;
        TextView headerText;

        public ViewHolder(ViewGroup recyclerView, View.OnClickListener itemClickListener, View.OnClickListener subItemClickListener) {
            super(LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.item_all, recyclerView, false));
            iconLayout = (FrameLayout) itemView.findViewById(R.id.iconLayout);
            headerIcon = (SimpleDraweeView) itemView.findViewById(R.id.headerIcon);
            headerText = (TextView) itemView.findViewById(R.id.headerText);
            subRecycler = (RecyclerView) itemView.findViewById(R.id.subRecycler);
            subRecycler.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            subRecycler.setAdapter(new SubAdapter(subItemClickListener));

            subRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                int dx;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Log.v("TAG", newState + "+ newState");
                    if (newState != 0) {
                        int itemLeft = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getLeft();

                        if (dx > 0) {
                            // scroll Right

                        } else {
                            // scroll Left

                        }
                    }
//                    subRecycler.smoothScrollToPosition();
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    this.dx = dx;
                }
            });
            itemView.setOnClickListener(itemClickListener);
        }
    }

}