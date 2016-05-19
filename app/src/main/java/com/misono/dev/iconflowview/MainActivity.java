package com.misono.dev.iconflowview;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends AppCompatActivity {

    String[] urls = new String[]{
            "http://img0.imgtn.bdimg.com/it/u=1002389095,2342209073&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2508119901,2102413332&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=997008680,2748412799&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=4248184023,1285925337&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3488207672,3352446836&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=682499833,287859835&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=1022049122,41854544&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=8705059,4082837378&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3412652926,25532554&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2958275000,1554317675&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=412942216,817709280&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=716273398,2168696384&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2198987318,312089596&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=3401986487,473287832&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2828624972,3235104617&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=883217823,3187734851&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2303826914,3127031927&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=4157184090,3736233525&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2152523337,1056902580&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2772452556,2752596537&fm=21&gp=0.jpg"
    };

    HeaderWrapper headerWrapper;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
//    int shadowHeightPx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

//        shadowHeightPx = getResources().getDimensionPixelSize(R.dimen.shadowHeight);
        headerWrapper = new HeaderWrapper();
        headerWrapper.init(this);
        headerWrapper.gone();
        headerWrapper.itemHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (headerWrapper.currentFirstVisibleItem != RecyclerView.NO_POSITION) {
//                    linearLayoutManager.scrollToPositionWithOffset(headerWrapper.currentFirstVisibleItem, 0);
                    recyclerView.smoothScrollToPosition(headerWrapper.currentFirstVisibleItem);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new Adapter(urls));
        recyclerView.addOnScrollListener(scrollListener);
    }


    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
            if (firstItem == RecyclerView.NO_POSITION) {
                return;
            }
            boolean isSwitch = headerWrapper.isItemSwitch(firstItem);
            if (isSwitch) {
                headerWrapper.show();
                headerWrapper.headerTextView.setText(urls[firstItem]);
                loadImg(headerWrapper.headerImageView, urls[firstItem]);
            }
            View firstView = linearLayoutManager.findViewByPosition(firstItem);
            int firstViewHeight = firstView.getHeight();
            int top = firstView.getTop();
            int offset = firstViewHeight - headerWrapper.height() - 0;
            int headerTop = Math.min(0, top + offset);
            headerWrapper.move(headerTop);
        }
    };


    static class HeaderWrapper {
        View itemHeaderView;
        SimpleDraweeView headerImageView;
        TextView headerTextView;
        int currentFirstVisibleItem;

        public void init(Activity activity) {
            itemHeaderView = activity.findViewById(R.id.itemHeader);
            headerImageView = (SimpleDraweeView) itemHeaderView.findViewById(R.id.headerIcon);
            headerTextView = (TextView) itemHeaderView.findViewById(R.id.headerText);
            currentFirstVisibleItem = -1;
        }

        public boolean isItemSwitch(int newItem) {
            boolean isSwitch = newItem != currentFirstVisibleItem;
            currentFirstVisibleItem = newItem;
            return isSwitch;
        }

        public void gone() {
            itemHeaderView.setVisibility(View.GONE);
        }

        public void show() {
            itemHeaderView.setVisibility(View.VISIBLE);
        }

        public void move(int top) {
            ViewCompat.setTranslationY(itemHeaderView, top);
        }

        public int height() {
            return itemHeaderView.getHeight();
        }
    }

    static class Adapter extends RecyclerView.Adapter<ViewHolder> {

        String[] urls;

        public Adapter(String[] urls) {
            this.urls = urls;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
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
                                loadImg(imageView, urls[img]);
                            }
                        }
                    }
            );

            loadImg(holder.headerIcon, urls[position]);
            loadImg(holder.image, urls[position]);
            holder.headerText.setText(urls[position]);
        }

        @Override
        public int getItemCount() {
            return urls.length;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout iconLayout = null;
        SimpleDraweeView headerIcon;
        SimpleDraweeView image;
        TextView headerText;

        public ViewHolder(ViewGroup recyclerView) {
            super(LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.item_all, recyclerView, false));
            iconLayout = (FrameLayout) itemView.findViewById(R.id.iconLayout);
            headerIcon = (SimpleDraweeView) itemView.findViewById(R.id.headerIcon);
            headerText = (TextView) itemView.findViewById(R.id.headerText);
            image = (SimpleDraweeView) itemView.findViewById(R.id.itemImg);
        }
    }

    static void loadImg(SimpleDraweeView imageView, String url) {
        imageView.setImageURI(Uri.parse(url));
    }
}
