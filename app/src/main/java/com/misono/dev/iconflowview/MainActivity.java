package com.misono.dev.iconflowview;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.misono.dev.iconflowview.adapter.MainAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

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
    SwipeRefreshLayout swipeRefresh;

    ImageView loadingHeader;
//    int shadowHeightPx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        loadingHeader = (ImageView) findViewById(R.id.loadingHeader);
//        shadowHeightPx = getResources().getDimensionPixelSize(R.dimen.shadowHeight);
        headerWrapper = new HeaderWrapper();
        headerWrapper.init(this);
        headerWrapper.gone();
        headerWrapper.reset();
        headerWrapper.itemHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (headerWrapper.currentFirstVisibleItem != RecyclerView.NO_POSITION) {
//                    linearLayoutManager.scrollToPositionWithOffset(headerWrapper.currentFirstVisibleItem, 0);
                    recyclerView.smoothScrollToPosition(headerWrapper.currentFirstVisibleItem);
                }
            }
        });

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MainAdapter(urls, itemClickListener, subItemClickListener));
        recyclerView.addOnScrollListener(scrollListener);
    }

    boolean isOnTop = false;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
            if (firstItem == RecyclerView.NO_POSITION) {
                headerWrapper.reset();
                headerWrapper.gone();
                return;
            }

            boolean isSwitch = headerWrapper.isItemSwitch(firstItem);
            if (isSwitch) { // item改变
                headerWrapper.headerTextView.setText(urls[firstItem]);
                loadImg(headerWrapper.headerImageView, urls[firstItem]);
            }

            if (firstItem != 0) {
                isOnTop = false;
            } else if (firstItem == RecyclerView.NO_POSITION) {
                isOnTop = true; // 当前recyclerView没有item
            } else {
                View firstChild = linearLayoutManager.findViewByPosition(firstItem);
                isOnTop = firstChild.getTop() == 0;
            }

            if (isOnTop || swipeRefresh.isRefreshing()) {
                headerWrapper.gone();
            } else {
                headerWrapper.show();
            }
            View firstView = linearLayoutManager.findViewByPosition(firstItem);
            int firstViewHeight = firstView.getHeight();
            int top = firstView.getTop();
            int offset = firstViewHeight - headerWrapper.height() - 0;
            int headerTop = Math.min(0, top + offset);
            Log.v("recyclerView.getTop()", "recyclerView.getTop()" + recyclerView.getTop());
            headerWrapper.move(headerTop, recyclerView.getTop());
        }
    };


    static class HeaderWrapper {
        View itemHeaderView;
        SimpleDraweeView headerImageView;
        TextView headerTextView;
        FrameLayout headerParent;
        int currentFirstVisibleItem;

        public void init(Activity activity) {
            itemHeaderView = activity.findViewById(R.id.itemHeader);
            headerParent = (FrameLayout) activity.findViewById(R.id.headerParent);
            headerImageView = (SimpleDraweeView) itemHeaderView.findViewById(R.id.headerIcon);
            headerTextView = (TextView) itemHeaderView.findViewById(R.id.headerText);
            currentFirstVisibleItem = RecyclerView.NO_POSITION;
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

        public void move(int top, int marginTop) {
            ViewCompat.setTranslationY(itemHeaderView, top);
            ViewCompat.setTranslationY(headerParent, marginTop);
        }

        public int height() {
            return itemHeaderView.getHeight();
        }

        public void reset() {
            isItemSwitch(RecyclerView.NO_POSITION);
        }
    }

    View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tag_item_position);
            Log.v(TAG, "item position : " + position);
            if (loadingHeader.getVisibility() == View.GONE) {
                loadingHeader.setVisibility(View.VISIBLE);
            } else {
                loadingHeader.setVisibility(View.GONE);
            }

        }
    };

    View.OnClickListener subItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tag_item_position);
            int subPosition = (int) v.getTag(R.id.tag_sub_item_position);
            Log.v(TAG, "sub item position : " + position + " : " + subPosition);
        }
    };


    public static void loadImg(SimpleDraweeView imageView, String url) {
        imageView.setImageURI(Uri.parse(url));
    }


}
