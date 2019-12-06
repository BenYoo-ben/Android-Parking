package com.example.parking;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import ss.com.bannerslider.ImageLoadingService;

public class BannerImageLoadingService implements ImageLoadingService {
    private Context A;

    public BannerImageLoadingService(Context A)
    {
        this.A = A;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
       imageView.setImageBitmap(BitmapFactory.decodeFile(url));

    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
       imageView.setImageResource(resource);


    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
       //
    }
}

