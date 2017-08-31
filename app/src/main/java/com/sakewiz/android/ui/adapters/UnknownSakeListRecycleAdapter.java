package com.sakewiz.android.ui.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.common.constants.DomainConstants;
import com.sakewiz.android.dto.UnknownSakeItem;
import com.sakewiz.android.dto.UserReview;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.ui.fragments.ProductDetailsFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dilshan_e on 7/23/17.
 */
public class UnknownSakeListRecycleAdapter extends RecyclerView.Adapter<UnknownSakeListRecycleAdapter.UnknownSakeListItemRowHolder> {

    private List<UnknownSakeItem> mUnknownSake;
    private Activity mContext;
    private int lastPosition = -1;


    public UnknownSakeListRecycleAdapter(Activity context, List<UnknownSakeItem> unknownSake) {
        this.mUnknownSake = unknownSake;
        this.mContext = context;
    }

    @Override
    public UnknownSakeListItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.unknown_sake_list_item, null);
        UnknownSakeListItemRowHolder mh = new UnknownSakeListItemRowHolder(v);

        return mh;
    }

    public void updateData(List<UnknownSakeItem> unknownSakeItems, int flag) {
        if (flag == 0) { //append
            for (int i = 0; i < unknownSakeItems.size(); i++) {
                System.out.println("i = " + i);
                mUnknownSake.add(unknownSakeItems.get(i));
                //notifyItemInserted(getItemCount());
            }
            notifyDataSetChanged();
        } else { //clear all
            lastPosition = -1;
            mUnknownSake.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(final UnknownSakeListItemRowHolder unknownSakeItemRowHolder, final int position) {
        final UnknownSakeItem item = mUnknownSake.get(position);

        if (item != null) {

            if(item.getScanImg() != null && !item.getScanImg().isEmpty()) {
                createImageView(item.getScanImg(), unknownSakeItemRowHolder.productImage, unknownSakeItemRowHolder.imageProgress);
            }


            if(item.getScannedUserHndl() != null && !item.getScannedUserHndl().isEmpty()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    unknownSakeItemRowHolder.userHandle.setText(Html.fromHtml(mContext.getString(R.string.scn_by) + " "
                            + CommonUtils.getInstance().getColoredSpanned(item.getScannedUserHndl(), "#69a574"), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    unknownSakeItemRowHolder.userHandle.setText(Html.fromHtml(mContext.getString(R.string.scn_by) + " "
                            + CommonUtils.getInstance().getColoredSpanned(item.getScannedUserHndl(), "#69a574")));
                }
            }

            if(item.getCreated()!= null && !item.getCreated().isEmpty()) {
                unknownSakeItemRowHolder.addedOnDate.setText(mContext.getString(R.string.added_on) + " "
                        + getAddedOnDate(Long.parseLong(item.getCreated())));
            }




//
//            latestRecommendItemRowHolder.parentContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ((MainActivity) mContext).addFragment(new ProductDetailsFragment(), ProductDetailsFragment.getTAG(), 2);
//                }
//            });
        }
    }

    private void setProductImage(String imageUrl, ImageView productImage, final ProgressBar imageProgress) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            ImageLoader.getInstance().displayImage(DomainConstants.DEFAULT_IMAGE_URL + imageUrl, productImage, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    if (imageProgress != null) imageProgress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if (imageProgress != null) imageProgress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (imageProgress != null) imageProgress.setVisibility(View.GONE);
                }
            });
        } else {
            if (imageProgress != null) imageProgress.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return (null != mUnknownSake ? mUnknownSake.size() : 0);
    }

    public UnknownSakeItem getItem(int position) {
        return mUnknownSake.get(position);
    }

    @Override
    public void onViewDetachedFromWindow(UnknownSakeListItemRowHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        ((FriendsListItemRowHolder) holder).clearAnimation();
    }

    public class UnknownSakeListItemRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.product_image) ImageView productImage;
        @Bind(R.id.image_progress) ProgressBar imageProgress;
        @Bind(R.id.txt_scanned_by) TextView userHandle;
        @Bind(R.id.txt_address) TextView addedOnDate;

        public UnknownSakeListItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            System.out.println("view = " + view);
        }

//        public void clearAnimation() {
//            parentContainer.clearAnimation();
//        }
    }

    private void createImageView(final String imageURL, final ImageView productImageView, final ProgressBar imageProgress) {
        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if (imageURL != null && !imageURL.isEmpty())
                        setProductImage(imageURL, productImageView, imageProgress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
        new Handler(callback).sendEmptyMessage(1);
    }

    public String getAddedOnDate(long date) {
        SimpleDateFormat fmtDateAndTime = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return fmtDateAndTime.format(date);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.up_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
