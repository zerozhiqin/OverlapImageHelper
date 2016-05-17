package com.misono.dev.iconflowview;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    String[] urls = new String[]{
            "http://img0.imgtn.bdimg.com/it/u=1002389095,2342209073&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2508119901,2102413332&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=997008680,2748412799&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=4248184023,1285925337&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3488207672,3352446836&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=682499833,287859835&fm=21&gp=0.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout vg = find(R.id.layout);

        vg.removeAllViews();
        OverlapImageHelper.addImageIntoViewGroup(this, vg, 16, 60, Math.max(urls.length, 6),
                new OverlapImageHelper.ImageProcessor() {
                    @Override
                    public void process(ImageView imageView, int position) {
                        if (position == 5) {
                            imageView.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Picasso.with(imageView.getContext())
                                    .load(urls[position])
                                    .transform(CircleTransform.INSTANCE)
                                    .into(imageView);
                        }
                    }
                });
    }

    <T> T find(@IdRes int id) {
        return (T) findViewById(id);
    }

}
