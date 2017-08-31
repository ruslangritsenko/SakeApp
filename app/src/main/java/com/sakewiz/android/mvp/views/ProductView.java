package com.sakewiz.android.mvp.views;

import com.sakewiz.android.model.entities.response.ReviewForProductResponse;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.ProductDetailResponse;
import com.sakewiz.android.model.entities.response.ProductFacadeResponse;
import com.sakewiz.android.model.entities.response.ReviewResponse;
import com.sakewiz.android.model.entities.response.UnknownSakeResponse;

/**
 * Created by dilshan_e on 6/10/17.
 */

public interface ProductView extends View{
    void showProductDetailResponse(ProductDetailResponse productDetailResponse);
    void showProductFacadeResponse(ProductFacadeResponse productFacadeResponse);
    void showReviewsResponse(ReviewResponse reviewResponse);
    void showReviewProductResponse(ReviewForProductResponse reviewForProductResponse);
    void showLikeReviewResponse(ReviewForProductResponse reviewForProductResponse);
    void showUnFavourProductResponse(ReviewForProductResponse reviewForProductResponse);
    void showFavourProductResponse(ReviewForProductResponse reviewForProductResponse);
    void showUnknownSakeResponse(UnknownSakeResponse unknownSakeResponse);
    void showGetFavouriteSakeResponse(FavouriteSakeResponse favouriteSakeResponse);
}
