package com.brainsocket.globalpages.utilities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.brainsocket.globalpages.R;
import com.brainsocket.globalpages.api.ServerInfo;
import com.brainsocket.globalpages.configrations.GlideApp;
import com.brainsocket.globalpages.data.entities.Attachment;
import com.brainsocket.globalpages.data.entities.BusinessGuide;
import com.brainsocket.globalpages.data.entities.MediaEntity;
import com.brainsocket.globalpages.data.entities.Post;
import com.brainsocket.globalpages.data.entities.ProductThumb;

import java.util.Random;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Adhamkh on 2018-06-29.
 */

public class BindingUtils {

    public static void loadBusinessGuideImage(ImageView view, BusinessGuide businessGuide) {
        try {
            if (businessGuide.getLogo().isEmpty()) {
                view.setVisibility(View.GONE);
                return;
            }
            Context context = view.getContext();
            String url = businessGuide.getLogo();
            if (!url.startsWith("http"))
                url = "http://" + url;
            GlideApp.with(context).load(url)
                    .transform(new RoundedCornersTransformation(24, 0))
                    .error(R.drawable.ic_launcher_web)
                    .placeholder(R.drawable.ic_launcher_web)
                    .into(view);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadBusinessGuideImage2(ImageView view, BusinessGuide businessGuide) {
        try {
            if (businessGuide.getCovers().size() <= 0) {
                view.setVisibility(View.GONE);
                return;
            } else {
                view.setVisibility(View.VISIBLE);
            }
            Context context = view.getContext();
            String url = /*ServerInfo.Companion.getImagesBaseUrl() +*/ businessGuide.getCovers().get(0).getUrl();
            if (!url.startsWith("http"))
                url = "http://" + url;
            GlideApp.with(context).load(url).error(R.drawable.ic_launcher_web)
                    .placeholder(R.drawable.ic_launcher_web).transform(new RoundedCornersTransformation(24, 0)).into(view);
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
            String url = /*ServerInfo.Companion.getImagesBaseUrl() +*/ post.getMedia().get(0).getUrl();
            if (!url.startsWith("http"))
                url = "http://" + url;
            GlideApp.with(context).load(url).error(R.drawable.ic_launcher_web)
                    .placeholder(R.drawable.ic_launcher_web).into(view);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadMediaImage(ImageView imageView, MediaEntity mediaEntity) {
        try {
            String url = /* ServerInfo.Companion.getImagesBaseUrl() +*/ mediaEntity.getUrl();
            if (!url.startsWith("http"))
                url = "http://" + url;
            Context context = imageView.getContext();
            GlideApp.with(context).load(url).error(R.drawable.businesslogo)
                    .placeholder(R.drawable.ic_launcher_web).into(imageView);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadAttachmentImage(ImageView imageView, Attachment attachment) {
        try {
            String url = /* ServerInfo.Companion.getImagesBaseUrl() +*/ attachment.getName();
            if (!url.startsWith("http"))
                url = "http://" + url;
            Context context = imageView.getContext();
            GlideApp.with(context).load(url).error(R.drawable.businesslogo)
                    .placeholder(R.mipmap.ic_launcher).into(imageView);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadProductImage(ImageView imageView, ProductThumb productThumb) {
        try {
            String url = /* ServerInfo.Companion.getImagesBaseUrl() +*/ productThumb.getImage();
            if (!url.startsWith("http"))
                url = "http://" + url;
            Context context = imageView.getContext();
            GlideApp.with(context).load(url).error(R.drawable.businesslogo)
                    .placeholder(R.drawable.ic_launcher_web).into(imageView);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadImage(ImageView imageView, String url) {
        try {
            if (!url.startsWith("http"))
                url = "http://" + url;
            Context context = imageView.getContext();
            GlideApp.with(context).load(url).error(R.drawable.businesslogo)
                    .placeholder(R.drawable.ic_launcher_web).into(imageView);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

}
