package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.response.ReviewForProductResponse;
import com.sakewiz.android.model.entities.request.ReviewForProductRequest;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.ProductDetailResponse;
import com.sakewiz.android.model.entities.response.ProductFacadeResponse;
import com.sakewiz.android.model.entities.response.ReviewResponse;
import com.sakewiz.android.model.entities.response.UnknownSakeResponse;

import rx.Observable;

/**
 * Created by dilshan_e on 6/3/17.
 */

public interface ProductService extends Service {
    Observable<ProductDetailResponse> getProductDetailService(String authorizationKey, String productId);
    Observable<ProductFacadeResponse> getProductFacadeService(String authorizationKey, String productId);
    Observable<ReviewResponse> getReviewsService(String authorizationKey, String entityId, String lastId, int size);
    Observable<ReviewForProductResponse> doReviewProductService(String authorizationKey, String productId, ReviewForProductRequest reviewForProductRequest);
    Observable<ReviewForProductResponse> doLikeReviewService(String authorizationKey, String reviewId);
    Observable<ReviewForProductResponse> doUnFavourProductService(String authorizationKey,  String productId);
    Observable<ReviewForProductResponse> doFavourProductService(String authorizationKey, String productId);
    Observable<UnknownSakeResponse> getUnknownSakeService(String authorizationKey, int pageSize, String lastId);
    Observable<FavouriteSakeResponse> getFavouritesakeService(String authorizationKey);
}
