package com.example.parking;

import android.content.Context;
import android.util.Log;

import java.io.File;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class BannerSliderAdapter extends SliderAdapter {
    Context A;
    public BannerSliderAdapter(Context A)
    {
        this.A =A;

    }

    public void getContext(Context A)
    {
     this.A = A;
    }
    @Override
    public int getItemCount() {
        return FirebaseController.Vehicles.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        int i = 0;
        Log.d("Exception@#@", "Checking for excemption with POS :"+position);
        if(new File(A.getFilesDir().getAbsolutePath()
                + "/" + FirebaseController.Vehicles.get(position).getImagecode() + ".jpg").isFile()) {

        }else
        {
            Log.d("Exception@#@", "Exception occured");
            imageSlideViewHolder.bindImageSlide(R.mipmap.lost_item);
        }


        while(i<FirebaseController.Vehicles.size())
        {
            if(position==i)
            {
                if( (new File(A.getFilesDir().getAbsolutePath()
                        + "/" + FirebaseController.Vehicles.get(i).getImagecode() + ".jpg")).isFile()) {
                    Log.d("Exception@#@", "NO Exception, going as planned");

                    imageSlideViewHolder.bindImageSlide(A.getFilesDir().getAbsolutePath()
                            + "/" + FirebaseController.Vehicles.get(i).getImagecode() + ".jpg");
                }
                else {

                }

                }
            i++;
        }

    }
}
