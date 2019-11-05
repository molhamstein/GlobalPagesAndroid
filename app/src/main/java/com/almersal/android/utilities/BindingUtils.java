package com.almersal.android.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.almersal.android.R;
import com.almersal.android.configrations.GlideApp;
import com.almersal.android.data.entities.Attachment;
import com.almersal.android.data.entities.BusinessGuide;
import com.almersal.android.data.entities.MediaEntity;
import com.almersal.android.data.entities.Post;
import com.almersal.android.data.entities.ProductThumb;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class BindingUtils {

    public static void loadPostSliderGuideImage(final ImageView view, Post post) {
        try {
            if (post.getMedia().size() <= 0) {
                view.setImageResource(R.drawable.ic_launcher_web);
                return;
            }
            Context context = view.getContext();
            String url = post.getMedia().get(0).getUrl();
            if (!url.startsWith("http"))
                url = "http://" + url;
            GlideApp.with(context).load(url)
                    .transform(new RoundedCornersTransformation(24, 4))
//                    .error(R.drawable.ic_launcher_web)
//                    .placeholder(R.drawable.ic_launcher_web)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            view.setImageDrawable(resource);
                            return false;
                        }
                    }).into(view);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadBusinessGuideImage2(ImageView view, BusinessGuide businessGuide) {
        try {
            if (businessGuide.getCovers().size() <= 0) {
                view.setVisibility(View.INVISIBLE);
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
            GlideApp.with(context).load(url).error(R.drawable.ic_launcher_web)
                    .placeholder(R.drawable.ic_launcher_web).into(imageView);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadMediaImage(ImageView imageView, String url) {
        try {
            if (!url.startsWith("http"))
                url = "http://" + url;
            Context context = imageView.getContext();
            GlideApp.with(context).load(url).error(R.drawable.ic_launcher_web)
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
            GlideApp.with(context).load(url).error(R.drawable.ic_launcher_web)
                    .placeholder(R.drawable.ic_launcher_web).into(imageView);
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
            GlideApp.with(context).load(url).error(R.drawable.ic_launcher_web)
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
            GlideApp.with(context).load(url).error(R.drawable.ic_launcher_web)
                    .placeholder(R.drawable.ic_launcher_web).into(imageView);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadProfileImage(ImageView imageView, String url) {
        try {
            if (!url.startsWith("http"))
                url = "http://" + url;
            RequestOptions options = new RequestOptions();
            Context context = imageView.getContext();
            GlideApp.with(context).load(url).error(R.drawable.ic_user_placeholder_24dp)
//                    .transform(new CropCircleTransformation())
                    .apply(options.circleCrop())
                    .placeholder(R.drawable.ic_user_placeholder_24dp).into(imageView);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

    public static void loadProfileThumbnailImage(final ImageView imageView, String url) {
        try {
            if (!url.startsWith("http"))
                url = "http://" + url;

            Context context = imageView.getContext();
            Resources res = context.getResources();
//            final int margin = (int) res.getDimension(R.dimen.md_margin);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
//            layoutParams.setMargins(margin, margin, margin, margin);
//            imageView.setLayoutParams(layoutParams);
            final int padding = (int) res.getDimension(R.dimen.md_padding);

            RequestListener<Drawable> listener = new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                    imageView.setPadding(padding, padding, padding, padding);
                    return false;
                }
            };

            RequestOptions options = new RequestOptions();
            GlideApp.with(context).load(url).error(R.mipmap.ic_profile_24dp)
//                    .transform(new CropCircleTransformation())
//                    .apply(new RequestOptions().override(imageView.getWidth(), imageView.getHeight() ))
                    .apply(options.circleCrop())
                    .listener(listener)
                    .placeholder(R.mipmap.ic_profile_24dp).into(imageView);

        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }


    public static void loadBusinessImage(ImageView imageView, String url) {
        try {
            if (!url.startsWith("http"))
                url = "http://" + url;
            Context context = imageView.getContext();
            GlideApp.with(context).load(url).error(R.drawable.job_holder)
                    .placeholder(R.drawable.job_holder).into(imageView);
        } catch (Exception ex) {
            Log.v("image load", ex.getMessage());
        }
    }

}
