package com.sakewiz.android.mvp.presenters;

import android.app.Activity;
import android.util.Log;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.PlaceService;
import com.sakewiz.android.domain.Service;
import com.sakewiz.android.model.entities.response.ReviewForPlaceResponse;
import com.sakewiz.android.model.entities.response.ReviewForProductResponse;
import com.sakewiz.android.model.entities.response.FavouritePlacesResponse;
import com.sakewiz.android.model.rest.exception.RetrofitException;
import com.sakewiz.android.mvp.views.PlaceView;
import com.sakewiz.android.mvp.views.View;
import com.sakewiz.android.utils.IScheduler;
import java.io.IOException;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by dilshan_e on 08/08/17.
 */

public class PlacePresenterImpl extends BasePresenter implements PlacePresenter {

    private final static String TAG = "PlacePresenterImpl";

    private PlaceView mPlaceView;

    public PlacePresenterImpl(Activity activityContext, Service presentService, IScheduler scheduler) {
        super(activityContext, presentService, scheduler);
    }

    @Override
    public void getFavouritePlacesBars() {
        subscription = getFavouritePlacesBarsObservable().subscribe(getFavouritePlacesBarsSubscriber());
    }

    @Override
    public void getFavouritePlacesBreweries() {
        subscription = getFavouritePlacesBreweriesObservable().subscribe(getFavouritePlacesBreweriesSubscriber());
    }

    @Override
    public void doUnFavouritePlacesBar(String placeId) {
        subscription = doUnFavourPlaceBarObservable(placeId).subscribe(doUnFavourPlaceBarSubscriber());
    }

    @Override
    public void doUnFavouritePlacesBrewery(String breweryId) {
        subscription = doUnFavourPlaceBreweryObservable(breweryId).subscribe(doUnFavourPlaceBrewerySubscriber());
    }

    private PlaceService getService() {
        return (PlaceService) mService;
    }

    @Override
    public void attachView(View v) {
        if (v instanceof PlaceView) {
            mPlaceView = (PlaceView) v;
            mView = mPlaceView;
        }
    }


    public Subscriber<FavouritePlacesResponse> getFavouritePlacesBarsSubscriber() {
        return new DefaultSubscriber<FavouritePlacesResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    FavouritePlacesResponse response = error.getErrorBodyAs(FavouritePlacesResponse.class);
                    if (response == null) {
                        response = new FavouritePlacesResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mPlaceView.showGetFavouritePlacesBarsResponse(response);
                } catch (IOException ex) {
                    FavouritePlacesResponse exceptionResponse = new FavouritePlacesResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mPlaceView.showGetFavouritePlacesBarsResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(FavouritePlacesResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mPlaceView.showGetFavouritePlacesBarsResponse(response);
                }
            }
        };
    }

    public Observable<FavouritePlacesResponse> getFavouritePlacesBarsObservable() {
        try {
            return getService().getFavouritePlacesBars(getAccessToken())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<ReviewForPlaceResponse> doUnFavourPlaceBarSubscriber() {
        return new DefaultSubscriber<ReviewForPlaceResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    ReviewForPlaceResponse response = error.getErrorBodyAs(ReviewForPlaceResponse.class);
                    if (response == null) {
                        response = new ReviewForPlaceResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mPlaceView.showDoUnFavourPlaceBarResponse(response);
                } catch (IOException ex) {
                    ReviewForPlaceResponse exceptionResponse = new ReviewForPlaceResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mPlaceView.showDoUnFavourPlaceBarResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ReviewForPlaceResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mPlaceView.showDoUnFavourPlaceBarResponse(response);
                }
            }
        };
    }

    public Observable<ReviewForPlaceResponse> doUnFavourPlaceBarObservable(String barId) {
        try {
            return getService().doUnfavourPlaceBar(getAccessToken(), barId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }



    public Subscriber<FavouritePlacesResponse> getFavouritePlacesBreweriesSubscriber() {
        return new DefaultSubscriber<FavouritePlacesResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    FavouritePlacesResponse response = error.getErrorBodyAs(FavouritePlacesResponse.class);
                    if (response == null) {
                        response = new FavouritePlacesResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mPlaceView.showGetFavouritePlacesBreweriesResponse(response);
                } catch (IOException ex) {
                    FavouritePlacesResponse exceptionResponse = new FavouritePlacesResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mPlaceView.showGetFavouritePlacesBreweriesResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(FavouritePlacesResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mPlaceView.showGetFavouritePlacesBreweriesResponse(response);
                }
            }
        };
    }

    public Observable<FavouritePlacesResponse> getFavouritePlacesBreweriesObservable() {
        try {
            return getService().getFavouritePlacesBreweries(getAccessToken())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<ReviewForPlaceResponse> doUnFavourPlaceBrewerySubscriber() {
        return new DefaultSubscriber<ReviewForPlaceResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    ReviewForPlaceResponse response = error.getErrorBodyAs(ReviewForPlaceResponse.class);
                    if (response == null) {
                        response = new ReviewForPlaceResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mPlaceView.showDoUnFavourPlaceBreweryResponse(response);
                } catch (IOException ex) {
                    ReviewForPlaceResponse exceptionResponse = new ReviewForPlaceResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mPlaceView.showDoUnFavourPlaceBreweryResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ReviewForPlaceResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mPlaceView.showDoUnFavourPlaceBreweryResponse(response);
                }
            }
        };
    }

    public Observable<ReviewForPlaceResponse> doUnFavourPlaceBreweryObservable(String breweryId) {
        try {
            return getService().doUnfavourPlaceBrewery(getAccessToken(),breweryId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

}
