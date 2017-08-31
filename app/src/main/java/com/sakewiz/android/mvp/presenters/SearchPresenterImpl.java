package com.sakewiz.android.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.SearchService;
import com.sakewiz.android.domain.Service;
import com.sakewiz.android.model.entities.request.SearchPlacesRequest;
import com.sakewiz.android.model.entities.request.SearchProductsRequest;
import com.sakewiz.android.model.entities.response.SearchPlacesResponse;
import com.sakewiz.android.model.entities.response.SearchProductResponse;
import com.sakewiz.android.model.entities.response.SuggestPlaceResponse;
import com.sakewiz.android.model.entities.response.SuggestProductResponse;
import com.sakewiz.android.model.rest.exception.RetrofitException;
import com.sakewiz.android.mvp.views.SearchView;
import com.sakewiz.android.mvp.views.View;
import com.sakewiz.android.utils.IScheduler;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by dilshan_e on 6/10/17.
 */
public class SearchPresenterImpl extends BasePresenter implements SearchPresenter{

    private final static String TAG = "UserPresenterImpl";

    private SearchView mSearchView;

    public SearchPresenterImpl(Activity activityContext, Service userAccountService, IScheduler scheduler) {
        super(activityContext, userAccountService, scheduler);
    }

    @Override
    public void doSearchPlaces(int page, int size, SearchPlacesRequest searchPlacesRequest) {
        subscription = doSearchPlacesObservable(page, size, searchPlacesRequest).subscribe(doSearchPlacesSubscriber());
    }

    @Override
    public void doSearchBreweries(int page, int size, SearchPlacesRequest searchPlacesRequest) {
        subscription = doSearchBreweriesObservable(page, size, searchPlacesRequest).subscribe(doSearchBreweriesSubscriber());
    }

    @Override
    public void doSearchProducts(int page, int size, SearchProductsRequest searchProductsRequest) {
        subscription = doSearchProductsObservable(page, size, searchProductsRequest).subscribe(doSearchProductsSubscriber());
    }

    @Override
    public void getSuggestPlaces(String text, int count) {
        subscription = getSuggestPlacesObservable(text, count).subscribe(getSuggestPlacesSubscriber());
    }

    @Override
    public void getSuggestBreweries(String text, int count) {
        subscription = getSuggestBreweriesObservable(text, count).subscribe(getSuggestBreweriesSubscriber());
    }

    @Override
    public void getSuggestProducts(String text, int count) {
        subscription = getSuggestProductsObservable(text, count).subscribe(getSuggestProductsSubscriber());
    }

    @Override
    public void attachView(View v) {
        if (v instanceof SearchView) {
            mSearchView = (SearchView) v;
            mView = mSearchView;
        }
    }

    public Subscriber<SearchPlacesResponse> doSearchPlacesSubscriber() {
        return new DefaultSubscriber<SearchPlacesResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    SearchPlacesResponse response = error.getErrorBodyAs(SearchPlacesResponse.class);
                    if (response == null) {
                        response = new SearchPlacesResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mSearchView.showSearchPlacesResponse(response);
                } catch (IOException ex) {
                    SearchPlacesResponse exceptionResponse = new SearchPlacesResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mSearchView.showSearchPlacesResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(SearchPlacesResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mSearchView.showSearchPlacesResponse(response);
                }
            }
        };
    }

    public Observable<SearchPlacesResponse> doSearchPlacesObservable(int page, int size, SearchPlacesRequest searchPlacesRequest) {
        try {
            return getService().doSearchPlacesService(getAccessToken(), page, size, searchPlacesRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<SearchPlacesResponse> doSearchBreweriesSubscriber() {
        return new DefaultSubscriber<SearchPlacesResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    SearchPlacesResponse response = error.getErrorBodyAs(SearchPlacesResponse.class);
                    if (response == null) {
                        response = new SearchPlacesResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mSearchView.showSearchBreweriesResponse(response);
                } catch (IOException ex) {
                    SearchPlacesResponse exceptionResponse = new SearchPlacesResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mSearchView.showSearchBreweriesResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(SearchPlacesResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mSearchView.showSearchBreweriesResponse(response);
                }
            }
        };
    }


    public Observable<SearchPlacesResponse> doSearchBreweriesObservable(int page, int size, SearchPlacesRequest searchPlacesRequest) {
        try {
            return getService().doSearchBreweriesService(getAccessToken(), page, size, searchPlacesRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<SearchProductResponse> doSearchProductsSubscriber() {
        return new DefaultSubscriber<SearchProductResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    SearchProductResponse response = error.getErrorBodyAs(SearchProductResponse.class);
                    if (response == null) {
                        response = new SearchProductResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mSearchView.showSearchProductsResponse(response);
                } catch (IOException ex) {
                    SearchProductResponse exceptionResponse = new SearchProductResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mSearchView.showSearchProductsResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(SearchProductResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mSearchView.showSearchProductsResponse(response);
                }
            }
        };
    }


    public Observable<SearchProductResponse> doSearchProductsObservable(int page, int size, SearchProductsRequest searchProductsRequest) {
        try {
            return getService().doSearchProductsService(getAccessToken(), page, size, searchProductsRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<List<String>> getSuggestPlacesSubscriber() {
        return new DefaultSubscriber<List<String>>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    SuggestPlaceResponse response = error.getErrorBodyAs(SuggestPlaceResponse.class);
                    if (response == null) {
                        response = new SuggestPlaceResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mSearchView.showSuggestPlacesResponse(response);
                } catch (IOException ex) {
                    SuggestPlaceResponse exceptionResponse = new SuggestPlaceResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mSearchView.showSuggestPlacesResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(List<String> response) {
                if (response != null) {
//                    response.setSuccess(true);
                    mSearchView.showSuggestPlacesResponse(response);
                }
            }
        };
    }

    public Observable<List<String>> getSuggestPlacesObservable(String text, int count) {
        try {
            return getService().getSuggestPlacesService(getAccessToken(), text, count)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<List<String>> getSuggestBreweriesSubscriber() {
        return new DefaultSubscriber<List<String>>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    SuggestPlaceResponse response = error.getErrorBodyAs(SuggestPlaceResponse.class);
                    if (response == null) {
                        response = new SuggestPlaceResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mSearchView.showSuggestBreweriesResponse(response);
                } catch (IOException ex) {
                    SuggestPlaceResponse exceptionResponse = new SuggestPlaceResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mSearchView.showSuggestBreweriesResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(List<String> response) {
                if (response != null) {
//                    response.setSuccess(true);
                    mSearchView.showSuggestBreweriesResponse(response);
                }
            }
        };
    }

    public Observable<List<String>> getSuggestBreweriesObservable(String text, int count) {
        try {
            return getService().getSuggestBreweriesService(getAccessToken(), text, count)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<List<String>> getSuggestProductsSubscriber() {
        return new DefaultSubscriber<List<String>>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    SuggestProductResponse response = error.getErrorBodyAs(SuggestProductResponse.class);
                    if (response == null) {
                        response = new SuggestProductResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mSearchView.showSuggestProductsResponse(response);
                } catch (IOException ex) {
                    SuggestProductResponse exceptionResponse = new SuggestProductResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mSearchView.showSuggestProductsResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(List<String> response) {
                if (response != null) {
//                    response.setSuccess(true);
                    mSearchView.showSuggestProductsResponse(response);
                }
            }
        };
    }

    public Observable<List<String>> getSuggestProductsObservable(String text, int count) {
        try {
            return getService().getSuggestProductsService(getAccessToken(), text, count)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private SearchService getService() {
        return (SearchService) mService;
    }
}
