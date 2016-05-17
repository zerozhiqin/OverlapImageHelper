package com.misono.dev.iconflowview;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class OverlapImageHelper {

    /**
     * @param context
     * @param frameLayout parent of icons
     * @param coverSizeDp
     * @param imgSizeDp
     * @param count
     * @param processor
     */
    public static void addImageIntoViewGroup(Context context,
                                             FrameLayout frameLayout,
                                             int coverSizeDp, int imgSizeDp,
                                             int count,
                                             ImageProcessor processor) {
        float density = context.getResources().getDisplayMetrics().density;
        int coverSizePx = (int) (density * coverSizeDp + .5f);
        int imgSizePx = (int) (density * imgSizeDp + .5f);

        for (int i = count - 1; i >= 0; i--) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(imgSizePx, imgSizePx);
            flp.setMargins(i * (imgSizePx - coverSizePx), 0, 0, 0);
            frameLayout.addView(imageView, flp);
            processor.process(imageView, i);
        }
    }


    interface ImageProcessor {
        void process(ImageView imageView, int position);
    }
}
