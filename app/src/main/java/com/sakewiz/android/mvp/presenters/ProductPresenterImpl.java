package com.sakewiz.android.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.ProductService;
import com.sakewiz.android.domain.Service;
import com.sakewiz.android.model.entities.response.ReviewForProductResponse;
import com.sakewiz.android.model.entities.request.ReviewForProductRequest;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.ProductDetailResponse;
import com.sakewiz.android.model.entities.response.ProductFacadeResponse;
import com.sakewiz.android.model.entities.response.ReviewResponse;
import com.sakewiz.android.model.entities.response.UnknownSakeResponse;
import com.sakewiz.android.model.rest.exception.RetrofitException;
import com.sakewiz.android.mvp.views.ProductView;
import com.sakewiz.android.mvp.views.View;
import com.sakewiz.android.utils.IScheduler;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by dilshan_e on 6/3/17.
 */

public class ProductPresenterImpl extends BasePresenter implements ProductPresenter {

    private final static String TAG = "UserPresenterImpl";

    private ProductView mProductView;

    public ProductPresenterImpl(Activity activityContext, Service productService, IScheduler scheduler) {
        super(activityContext, productService, scheduler);
    }

    @Override
    public void getProductDetails(String productId) {
        subscription = getProductDetailsObservable(productId).subscribe(getProductDetailsSubscriber());
    }

    @Override
    public void getProductFacade(String productId) {
        subscription = getProductFacadeObservable(productId).subscribe(getProductFacadeSubscriber());
    }

    @Override
    public void getReviews(String entityId, String lastId, int size) {
        subscription = getReviewsObservable(entityId, lastId, size).subscribe(getReviewsSubscriber());
    }

    @Override
    public void doReviewProduct(String productId, ReviewForProductRequest reviewForProductRequest) {
        subscription = doReviewProductObservable(productId, reviewForProductRequest).subscribe(doReviewProductSubscriber());
    }

    @Override
    public void doLikeReview(String reviewId) {
        subscription = doLikeReviewObservable(reviewId).subscribe(doLikeReviewSubscriber());
    }

    @Override
    public void getFavouriteSake() {
        subscription = getFavouriteSakeObservable().subscribe(getFavouriteSakeSubscriber());
    }

    @Override
    public void doUnFavourProduct(String productId) {
        subscription = doUnFavourProductObservable(productId).subscribe(doUnFavourProductSubscriber());
    }

    @Override
    public void doFavourProduct(String productId) {
        subscription = doFavourProductObservable(productId).subscribe(doFavourProductSubscriber());
    }

    @Override
    public void getUnknownSakeList(int pageSize, String lastId) {
        subscription = getUnknownSakeListObservable(pageSize, lastId).subscribe(getUnknownSakeListSubscriber());
    }

    private ProductService getService() {
        return (ProductService) mService;
    }

    @Override
    public void attachView(View v) {
        if (v instanceof ProductView) {
            mProductView = (ProductView) v;
            mView = mProductView;
        }
    }


    public Subscriber<ProductDetailResponse> getProductDetailsSubscriber() {
        return new DefaultSubscriber<ProductDetailResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    ProductDetailResponse response = error.getErrorBodyAs(ProductDetailResponse.class);
                    if (response == null) {
                        response = new ProductDetailResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mProductView.showProductDetailResponse(response);
                } catch (IOException ex) {
                    ProductDetailResponse exceptionResponse = new ProductDetailResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mProductView.showProductDetailResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ProductDetailResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mProductView.showProductDetailResponse(response);
                }
            }
        };
    }

    public Observable<ProductDetailResponse> getProductDetailsObservable(String productId) {
        try {
            return getService().getProductDetailService(getAccessToken(), productId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<ProductFacadeResponse> getProductFacadeSubscriber() {
        return new DefaultSubscriber<ProductFacadeResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    ProductFacadeResponse response = error.getErrorBodyAs(ProductFacadeResponse.class);
                    if (response == null) {
                        response = new ProductFacadeResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mProductView.showProductFacadeResponse(response);
                } catch (IOException ex) {
                    ProductFacadeResponse exceptionResponse = new ProductFacadeResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mProductView.showProductFacadeResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ProductFacadeResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mProductView.showProductFacadeResponse(response);
                }
            }
        };
    }

    public Observable<ProductFacadeResponse> getProductFacadeObservable(String productId) {
        try {
            return getService().getProductFacadeService(getAccessToken(), productId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<FavouriteSakeResponse> getFavouriteSakeSubscriber() {
        return new DefaultSubscriber<FavouriteSakeResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    FavouriteSakeResponse response = error.getErrorBodyAs(FavouriteSakeResponse.class);
                    if (response == null) {
                        response = new FavouriteSakeResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mProductView.showGetFavouriteSakeResponse(response);
                } catch (IOException ex) {
                    FavouriteSakeResponse exceptionResponse = new FavouriteSakeResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mProductView.showGetFavouriteSakeResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(FavouriteSakeResponse response) {
                if (response != null) {
                    response.setSuccess(true);
//                    preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, response.getToken()).apply();
                    mProductView.showGetFavouriteSakeResponse(response);
                }
            }
        };
    }

    public Observable<FavouriteSakeResponse> getFavouriteSakeObservable() {
        try {
            return getService().getFavouritesakeService(getAccessToken())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<ReviewResponse> getReviewsSubscriber() {
        return new DefaultSubscriber<ReviewResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    ReviewResponse response = error.getErrorBodyAs(ReviewResponse.class);
                    if (response == null) {
                        response = new ReviewResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mProductView.showReviewsResponse(response);
                } catch (IOException ex) {
                    ReviewResponse exceptionResponse = new ReviewResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mProductView.showReviewsResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ReviewResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mProductView.showReviewsResponse(response);
                }
            }
        };
    }

    public Observable<ReviewResponse> getReviewsObservable(String entityId, String lastId, int size) {
        try {
            return getService().getReviewsService(getAccessToken(), entityId, lastId, size)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<ReviewForProductResponse> doReviewProductSubscriber() {
        return new DefaultSubscriber<ReviewForProductResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    ReviewForProductResponse response = error.getErrorBodyAs(ReviewForProductResponse.class);
                    if (response == null) {
                        response = new ReviewForProductResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mProductView.showReviewProductResponse(response);
                } catch (IOException ex) {
                    ReviewForProductResponse exceptionResponse = new ReviewForProductResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mProductView.showReviewProductResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ReviewForProductResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mProductView.showReviewProductResponse(response);
                }
            }
        };
    }

    public Observable<ReviewForProductResponse> doReviewProductObservable(String productId, ReviewForProductRequest reviewForProductRequest) {
        try {
            return getService().doReviewProductService(getAccessToken(), productId, reviewForProductRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<ReviewForProductResponse> doLikeReviewSubscriber() {
        return new DefaultSubscriber<ReviewForProductResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    ReviewForProductResponse response = error.getErrorBodyAs(ReviewForProductResponse.class);
                    if (response == null) {
                        response = new ReviewForProductResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mProductView.showLikeReviewResponse(response);
                } catch (IOException ex) {
                    ReviewForProductResponse exceptionResponse = new ReviewForProductResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mProductView.showLikeReviewResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ReviewForProductResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mProductView.showLikeReviewResponse(response);
                }
            }
        };
    }

    public Observable<ReviewForProductResponse> doLikeReviewObservable(String reviewId) {
        try {
            return getService().doLikeReviewService(getAccessToken(), reviewId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<ReviewForProductResponse> doUnFavourProductSubscriber() {
        return new DefaultSubscriber<ReviewForProductResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    ReviewForProductResponse response = error.getErrorBodyAs(ReviewForProductResponse.class);
                    if (response == null) {
                        response = new ReviewForProductResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mProductView.showUnFavourProductResponse(response);
                } catch (IOException ex) {
                    ReviewForProductResponse exceptionResponse = new ReviewForProductResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mProductView.showUnFavourProductResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ReviewForProductResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mProductView.showUnFavourProductResponse(response);
                }
            }
        };
    }

    public Observable<ReviewForProductResponse> doUnFavourProductObservable(String productId) {
        try {
            return getService().doUnFavourProductService(getAccessToken(), productId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }



    public Subscriber<ReviewForProductResponse> doFavourProductSubscriber() {
        return new DefaultSubscriber<ReviewForProductResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    ReviewForProductResponse response = error.getErrorBodyAs(ReviewForProductResponse.class);
                    if (response == null) {
                        response = new ReviewForProductResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mProductView.showFavourProductResponse(response);
                } catch (IOException ex) {
                    ReviewForProductResponse exceptionResponse = new ReviewForProductResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mProductView.showFavourProductResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ReviewForProductResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mProductView.showFavourProductResponse(response);
                }
            }
        };
    }

    public Observable<ReviewForProductResponse> doFavourProductObservable(String productId) {
        try {
            return getService().doFavourProductService(getAccessToken(), productId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<UnknownSakeResponse> getUnknownSakeListSubscriber() {
        return new DefaultSubscriber<UnknownSakeResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    UnknownSakeResponse response = error.getErrorBodyAs(UnknownSakeResponse.class);
                    if (response == null) {
                        response = new UnknownSakeResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mProductView.showUnknownSakeResponse(response);
                } catch (IOException ex) {
                    UnknownSakeResponse exceptionResponse = new UnknownSakeResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mProductView.showUnknownSakeResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(UnknownSakeResponse response) {
                if (response != null) {
                    response.setSuccess(true);
//                    preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, response.getToken()).apply();
                    mProductView.showUnknownSakeResponse(response);
                }
            }
        };
    }

    public Observable<UnknownSakeResponse> getUnknownSakeListObservable(int pageSize, String lastId) {
        try {
            return getService().getUnknownSakeService(getAccessToken(), pageSize, lastId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}
