package com.sakewiz.android.mvp.presenters;


import com.sakewiz.android.model.entities.request.ReviewForProductRequest;

/**
 * Created by dilshan_e on 6/3/17.
 */

public interface ProductPresenter extends Presenter {
    void getProductDetails(String productId);
    void getProductFacade(String productId);
    void getReviews(String entityId, String lastId, int size);
    void doReviewProduct(String productId, ReviewForProductRequest reviewForProductRequest);
    void doLikeReview(String reviewId);
    void getFavouriteSake();
    void doUnFavourProduct(String productId);
    void doFavourProduct(String productId);
    void getUnknownSakeList(int pageSize, String lastId);

}
