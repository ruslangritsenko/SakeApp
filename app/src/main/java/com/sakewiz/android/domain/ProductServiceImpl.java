package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.response.ReviewForProductResponse;
import com.sakewiz.android.model.entities.request.ReviewForProductRequest;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.ProductDetailResponse;
import com.sakewiz.android.model.entities.response.ProductFacadeResponse;
import com.sakewiz.android.model.entities.response.ReviewResponse;
import com.sakewiz.android.model.entities.response.UnknownSakeResponse;
import com.sakewiz.android.model.rest.SakeWizService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by dilshan_e on 6/3/17.
 */

public class ProductServiceImpl implements ProductService {

    private SakeWizService sakeWizService;

    public ProductServiceImpl(SakeWizService nzsService) {
        super();
        this.sakeWizService = nzsService;
    }

    @Override
    public Observable<ProductDetailResponse> getProductDetailService(String authorizationKey, String productId) {
        return sakeWizService.getApi()
                .getProductDetailAPI(authorizationKey, productId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ProductFacadeResponse> getProductFacadeService(String authorizationKey, String productId) {
        return sakeWizService.getApi()
                .getProductFacadeAPI(authorizationKey, productId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ReviewResponse> getReviewsService(String authorizationKey, String entityId, String lastId, int size) {
        return sakeWizService.getApi()
                .getReviewsAPI(authorizationKey, entityId, lastId, size)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ReviewForProductResponse> doReviewProductService(String authorizationKey, String productId, ReviewForProductRequest reviewForProductRequest) {
        return sakeWizService.getApi()
                .doReviewProductAPI(authorizationKey, productId, reviewForProductRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ReviewForProductResponse> doLikeReviewService(String authorizationKey, String reviewId) {
        return sakeWizService.getApi()
                .doLikeReviewAPI(authorizationKey, reviewId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ReviewForProductResponse> doUnFavourProductService(String authorizationKey, String productId) {
        return sakeWizService.getApi()
                .doUnFavourProductAPI(authorizationKey, productId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ReviewForProductResponse> doFavourProductService(String authorizationKey, String productId) {
        return sakeWizService.getApi()
                .doFavourProductAPI(authorizationKey, productId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UnknownSakeResponse> getUnknownSakeService(String authorizationKey, int pageSize, String lastId) {
        return sakeWizService.getApi()
                .getUnknownSakeAPI(authorizationKey, pageSize, lastId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<FavouriteSakeResponse> getFavouritesakeService(String authorizationKey) {
        return sakeWizService.getApi()
                .getFavouriteSakeAPI(authorizationKey)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
