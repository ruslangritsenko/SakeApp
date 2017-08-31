package com.sakewiz.android.ui.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.common.constants.DomainConstants;
import com.sakewiz.android.dto.Product;
import com.sakewiz.android.ui.fragments.MainFragment;
import com.sakewiz.android.ui.utils.TimeUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dilshan_e on 6/11/17.
 */
public class SearchSakeProductRecycleAdapter extends RecyclerView.Adapter<SearchSakeProductRecycleAdapter.SearchSakeProductRowHolder>  {

    private List<Product> mSearchResultList;
    private Activity mContext;
    private int lastPosition = -1;


    public SearchSakeProductRecycleAdapter(Activity context, List<Product> searchResultList) {
        this.mSearchResultList = searchResultList;
        this.mContext = context;
    }

    @Override
    public SearchSakeProductRecycleAdapter.SearchSakeProductRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.latest_recommendation_item, null);
        SearchSakeProductRecycleAdapter.SearchSakeProductRowHolder mh = new SearchSakeProductRecycleAdapter.SearchSakeProductRowHolder(v);

        return mh;
    }

    public void updateData(List<Product> searchResultList, int flag) {
        if (flag == 0) { //append
            for (int i = 0; i < searchResultList.size(); i++) {
                mSearchResultList.add(searchResultList.get(i));
                //notifyItemInserted(getItemCount());
            }
            notifyDataSetChanged();
        } else { //clear all
            lastPosition = -1;
            mSearchResultList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(final SearchSakeProductRecycleAdapter.SearchSakeProductRowHolder searchSakeProductRowHolder, final int position) {
        final Product item = mSearchResultList.get(position);
        if(item != null) {
            if(item.getMainImgUrl() != null && !item.getMainImgUrl().isEmpty()) {
                createImageView(item.getMainImgUrl(),searchSakeProductRowHolder.productImage,searchSakeProductRowHolder.imageProgress);
            }
            if(item.getName() != null )
                searchSakeProductRowHolder.mSakeTitle.setText(item.getName().getEn());
            if(item.getType() != null && !item.getType().isEmpty())
                searchSakeProductRowHolder.mSakeType.setText(item.getType());
            if(item.getCountryName() != null && !item.getCountryName().getEn().isEmpty()) {
                if (item.getRegionName() != null && !item.getRegionName().getEn().isEmpty())
                    searchSakeProductRowHolder.mSakePlace.setText(item.getCountryName().getEn() + "," + item.getRegionName().getEn());
                else
                    searchSakeProductRowHolder.mSakePlace.setText(item.getCountryName().getEn());
            }else if (item.getRegionName() != null && !item.getRegionName().getEn().isEmpty()) {
                searchSakeProductRowHolder.mSakePlace.setText(item.getRegionName().getEn());
            }

            if (item.getRate() != null && !item.getRate().isEmpty()) {
                String rate = item.getRate();
                switch (rate) {
                    case "1.0" :
                        searchSakeProductRowHolder.starPoint_1.setImageResource(R.drawable.ic_star);
                        break;
                    case "2.0" :
                        searchSakeProductRowHolder.starPoint_1.setImageResource(R.drawable.ic_star);
                        searchSakeProductRowHolder.starPoint_2.setImageResource(R.drawable.ic_star);
                        break;
                    case "3.0" :
                        searchSakeProductRowHolder.starPoint_1.setImageResource(R.drawable.ic_star);
                        searchSakeProductRowHolder.starPoint_2.setImageResource(R.drawable.ic_star);
                        searchSakeProductRowHolder.starPoint_3.setImageResource(R.drawable.ic_star);
                        break;
                    case "4.0" :
                        searchSakeProductRowHolder.starPoint_1.setImageResource(R.drawable.ic_star);
                        searchSakeProductRowHolder.starPoint_2.setImageResource(R.drawable.ic_star);
                        searchSakeProductRowHolder.starPoint_3.setImageResource(R.drawable.ic_star);
                        searchSakeProductRowHolder.starPoint_4.setImageResource(R.drawable.ic_star);
                        break;
                    case "5.0" :
                        searchSakeProductRowHolder.starPoint_1.setImageResource(R.drawable.ic_star);
                        searchSakeProductRowHolder.starPoint_2.setImageResource(R.drawable.ic_star);
                        searchSakeProductRowHolder.starPoint_3.setImageResource(R.drawable.ic_star);
                        searchSakeProductRowHolder.starPoint_4.setImageResource(R.drawable.ic_star);
                        searchSakeProductRowHolder.starPoint_5.setImageResource(R.drawable.ic_star);
                        break;
                    default:
                        break;
                }
            }
            if(item.getReviewCount() != null && !item.getReviewCount().isEmpty())
                searchSakeProductRowHolder.mCommentCount.setText(item.getReviewCount());
            if(item.getFavoured() != null && !item.getFavoured().isEmpty())
                searchSakeProductRowHolder.mLikeCount.setText(item.getFavoured());
            if(item.getUpdated() != null && !item.getUpdated().isEmpty())
                searchSakeProductRowHolder.mTime.setText(TimeUtils.getRelativeTime(Long.parseLong(item.getUpdated()))
                        + " " + mContext.getString(R.string.ago));

            searchSakeProductRowHolder.mainContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MainFragment.mainFragment !=null && item.getId() != null && !item.getId().isEmpty())
                        MainFragment.mainFragment.performProductFacadeRequest(item.getId());
                }
            });
        }

        // Here you apply the animation when the view is bound
        setAnimation(searchSakeProductRowHolder.parentContainer, position);
    }

    private void setProductImage(String imageUrl, ImageView productImage, final ProgressBar imageProgress) {
        if(imageUrl != null && !imageUrl.isEmpty()){
            ImageLoader.getInstance().displayImage(DomainConstants.DEFAULT_IMAGE_URL+imageUrl, productImage, new SimpleImageLoadingListener() {
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
        return (null != mSearchResultList ? mSearchResultList.size() : 0);
    }

    public Product getItem(int position) {
        return mSearchResultList.get(position);
    }

    @Override
    public void onViewDetachedFromWindow(SearchSakeProductRecycleAdapter.SearchSakeProductRowHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((SearchSakeProductRecycleAdapter.SearchSakeProductRowHolder) holder).clearAnimation();
    }

    public class SearchSakeProductRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.main_product_items_container) RelativeLayout mainContainer;
        @Bind(R.id.product_image) ImageView productImage;
        @Bind(R.id.image_progress) ProgressBar imageProgress;
        @Bind(R.id.parent_container_featured_product) CardView parentContainer;
        @Bind(R.id.sake_title_text) TextView mSakeTitle;
        @Bind(R.id.sake_type_text) TextView mSakeType;
        @Bind(R.id.sake_place_text) TextView mSakePlace;
        @Bind(R.id.comment_count_text) TextView mCommentCount;
        @Bind(R.id.like_count_text) TextView mLikeCount;
        @Bind(R.id.clock_count_text) TextView mTime;
        @Bind(R.id.star_point_1) ImageView starPoint_1;
        @Bind(R.id.star_point_2) ImageView starPoint_2;
        @Bind(R.id.star_point_3) ImageView starPoint_3;
        @Bind(R.id.star_point_4) ImageView starPoint_4;
        @Bind(R.id.star_point_5) ImageView starPoint_5;


        public SearchSakeProductRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void clearAnimation() {
            parentContainer.clearAnimation();
        }
    }

    private void createImageView( final String imageURL, final ImageView productImageView, final ProgressBar imageProgress) {
        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if(imageURL != null && !imageURL.isEmpty())setProductImage(imageURL, productImageView, imageProgress);
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
