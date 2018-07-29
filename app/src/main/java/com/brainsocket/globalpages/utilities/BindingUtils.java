package com.brainsocket.globalpages.utilities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.brainsocket.globalpages.R;
import com.brainsocket.globalpages.configrations.GlideApp;
import com.brainsocket.globalpages.data.entities.BusinessGuide;
import com.brainsocket.globalpages.data.entities.MediaEntity;
import com.brainsocket.globalpages.data.entities.Post;

import java.util.Random;

/**
 * Created by Adhamkh on 2018-06-29.
 */

public class BindingUtils {

    public static void loadBusinessGuideImage(ImageView view, BusinessGuide businessGuide) {
        try {
            if (businessGuide.getImageUrl().isEmpty()) {
                view.setVisibility(View.GONE);
                return;
            }
            Context context = view.getContext();
            GlideApp.with(context).load(businessGuide.getImageUrl()).error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher).into(view);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadPostImage(ImageView view,/* View container,*/ Post post) {
        try {
            /*if (post.getImage().isEmpty()) {
                view.setVisibility(View.GONE);
                return;
            } else {
                container.setVisibility(View.GONE);
            }*/
            Context context = view.getContext();
            GlideApp.with(context).load(post.getImage()).error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher).into(view);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadMediaImage(ImageView imageView, MediaEntity mediaEntity) {
        try {
            Context context = imageView.getContext();
            GlideApp.with(context).load(mediaEntity.getUrl()).error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher).into(imageView);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

}
