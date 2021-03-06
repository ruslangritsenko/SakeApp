package com.sakewiz.android.ui.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.sakewiz.android.common.constants.DomainConstants;
import com.sakewiz.android.dto.UserReview;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.ui.fragments.ProductDetailsFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dilshan_e on 6/23/17.
 */
public class FriendsListRecycleAdapter extends RecyclerView.Adapter<FriendsListRecycleAdapter.FriendsListItemRowHolder> {

    private List<UserReview> mUserReviews;
    private Activity mContext;
    private int lastPosition = -1;


    public FriendsListRecycleAdapter(Activity context, List<UserReview> userReviews) {
        this.mUserReviews = userReviews;
        this.mContext = context;
    }

    @Override
    public FriendsListItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friends_list_item, null);
        FriendsListItemRowHolder mh = new FriendsListItemRowHolder(v);

        return mh;
    }

    public void updateData(List<UserReview> userReviews, int flag) {
        if (flag == 0) { //append
            for (int i = 0; i < userReviews.size(); i++) {
                mUserReviews.add(userReviews.get(i));
                //notifyItemInserted(getItemCount());
            }
            notifyDataSetChanged();
        } else { //clear all
            lastPosition = -1;
            mUserReviews.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(final FriendsListItemRowHolder latestRecommendItemRowHolder, final int position) {
        final UserReview item = mUserReviews.get(position);

        if (item != null) {

//            if (item.getUserHandle() != null && !item.getUserHandle().isEmpty())
//                latestRecommendItemRowHolder.mReviewerName.setText(item.getUserHandle());
//            if (item.getUserAddress() != null && !item.getUserAddress().isEmpty())
//                latestRecommendItemRowHolder.mAddress.setText(item.getUserAddress());
//            if (item.getReview() != null && item.getReview().getEn() != null)
//                latestRecommendItemRowHolder.mReview.setText(item.getReview().getEn());
//            if (item.getCurrentUserLiked() != null && item.getCurrentUserLiked().equals("true"))
//                latestRecommendItemRowHolder.mImgLike.setImageResource(R.drawable.ic_like_fill);
//            if (item.getLikes() != null && !item.getLikes().isEmpty())
//                latestRecommendItemRowHolder.mCountLike.setText(item.getLikes());
//            else latestRecommendItemRowHolder.mCountLike.setText("0");

            // Here you apply the animation when the view is bound
            setAnimation(latestRecommendItemRowHolder.parentContainer, position);

            latestRecommendItemRowHolder.parentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) mContext).addFragment(new ProductDetailsFragment(), ProductDetailsFragment.getTAG(), 2);
                }
            });
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
        return (null != mUserReviews ? mUserReviews.size() : 0);
    }

    public UserReview getItem(int position) {
        return mUserReviews.get(position);
    }

    @Override
    public void onViewDetachedFromWindow(FriendsListItemRowHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((FriendsListItemRowHolder) holder).clearAnimation();
    }

    public class FriendsListItemRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.parent_container_featured_product) CardView parentContainer;
//        @Bind(R.id.txt_reviewer_name) TextView mReviewerName;
//        @Bind(R.id.txt_address) TextView mAddress;
//        @Bind(R.id.txt_review) TextView mReview;
//        @Bind(R.id.ic_like) ImageView mImgLike;
//        @Bind(R.id.txt_like_count) TextView mCountLike;
//        @Bind(R.id.txt_comment_count) TextView mCountComment;


        public FriendsListItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void clearAnimation() {
            parentContainer.clearAnimation();
        }
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
