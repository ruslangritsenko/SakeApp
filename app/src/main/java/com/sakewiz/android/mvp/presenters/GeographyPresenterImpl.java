package com.sakewiz.android.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.GeographyService;
import com.sakewiz.android.domain.Service;
import com.sakewiz.android.model.entities.response.CountryListResponse;
import com.sakewiz.android.model.entities.response.CountryResponse;
import com.sakewiz.android.model.entities.response.RegionListResponse;
import com.sakewiz.android.model.entities.response.RegionResponse;
import com.sakewiz.android.model.entities.response.SubRegionListResponse;
import com.sakewiz.android.model.entities.response.SubRegionResponse;
import com.sakewiz.android.model.rest.exception.RetrofitException;
import com.sakewiz.android.mvp.views.GeographyView;
import com.sakewiz.android.mvp.views.View;
import com.sakewiz.android.utils.IScheduler;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public class GeographyPresenterImpl extends BasePresenter implements GeographyPresenter {

    private final static String TAG = "GeographyPresenterImpl";

    private GeographyView mGeographyView;

    public GeographyPresenterImpl(Activity activityContext, Service geographyService, IScheduler scheduler) {
        super(activityContext, geographyService, scheduler);
    }

    @Override
    public void getCountryList() {
        subscription = getCountryListObservable().subscribe(getCountryListSubscriber());
    }

    @Override
    public void getCountry(String countryCd) {
        subscription = getCountryObservable(countryCd).subscribe(getCountrySubscriber());
    }

    @Override
    public void getRegion(String countryCd, String regionCd) {
        subscription = getRegionObservable(countryCd, regionCd).subscribe(getRegionSubscriber());
    }

    @Override
    public void getSubRegion(String countryCd, String regionCd, String subRegionCd) {
        subscription = getSubRegionObservable(countryCd, regionCd, subRegionCd).subscribe(getSubRegionSubscriber());
    }

    @Override
    public void getSubRegionList(String countryCd, String regionCd) {
        subscription = getSubRegionListObservable(countryCd, regionCd).subscribe(getSubRegionListSubscriber());
    }

    @Override
    public void getRegionList(String countryCd) {
        subscription = getRegionListObservable(countryCd).subscribe(getRegionListSubscriber());
    }

    @Override
    public void attachView(View v) {
        if (v instanceof GeographyView) {
            mGeographyView = (GeographyView) v;
            mView = mGeographyView;
        }
    }

    private GeographyService getService() {
        return (GeographyService) mService;
    }

    public Subscriber<CountryListResponse> getCountryListSubscriber() {
        return new DefaultSubscriber<CountryListResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    CountryListResponse response = error.getErrorBodyAs(CountryListResponse.class);
                    if (response == null) {
                        response = new CountryListResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mGeographyView.showGetCountryListResponse(response);
                } catch (IOException ex) {
                    CountryListResponse exceptionResponse = new CountryListResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mGeographyView.showGetCountryListResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(CountryListResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mGeographyView.showGetCountryListResponse(response);
                }
            }
        };
    }

    public Observable<CountryListResponse> getCountryListObservable() {
        try {
            return getService().getCountryListService(getAccessToken())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<CountryResponse> getCountrySubscriber() {
        return new DefaultSubscriber<CountryResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    CountryResponse response = error.getErrorBodyAs(CountryResponse.class);
                    if (response == null) {
                        response = new CountryResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mGeographyView.showGetCountryResponse(response);
                } catch (IOException ex) {
                    CountryResponse exceptionResponse = new CountryResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mGeographyView.showGetCountryResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(CountryResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mGeographyView.showGetCountryResponse(response);
                }
            }
        };
    }

    public Observable<CountryResponse> getCountryObservable(String countryCd) {
        try {
            return getService().getCountryService(getAccessToken(),countryCd)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<RegionResponse> getRegionSubscriber() {
        return new DefaultSubscriber<RegionResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    RegionResponse response = error.getErrorBodyAs(RegionResponse.class);
                    if (response == null) {
                        response = new RegionResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mGeographyView.showGetRegionResponse(response);
                } catch (IOException ex) {
                    RegionResponse exceptionResponse = new RegionResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mGeographyView.showGetRegionResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(RegionResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mGeographyView.showGetRegionResponse(response);
                }
            }
        };
    }

    public Observable<RegionResponse> getRegionObservable(String countryCd, String regionCd) {
        try {
            return getService().getRegionService(getAccessToken(),countryCd, regionCd)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<SubRegionResponse> getSubRegionSubscriber() {
        return new DefaultSubscriber<SubRegionResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    SubRegionResponse response = error.getErrorBodyAs(SubRegionResponse.class);
                    if (response == null) {
                        response = new SubRegionResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mGeographyView.showGetSubRegionResponse(response);
                } catch (IOException ex) {
                    SubRegionResponse exceptionResponse = new SubRegionResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mGeographyView.showGetSubRegionResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(SubRegionResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mGeographyView.showGetSubRegionResponse(response);
                }
            }
        };
    }

    public Observable<SubRegionResponse> getSubRegionObservable(String countryCd, String regionCd,String subRegionCd) {
        try {
            return getService().getSubRegionService(getAccessToken(),countryCd, regionCd, subRegionCd)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<SubRegionListResponse> getSubRegionListSubscriber() {
        return new DefaultSubscriber<SubRegionListResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    SubRegionListResponse response = error.getErrorBodyAs(SubRegionListResponse.class);
                    if (response == null) {
                        response = new SubRegionListResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mGeographyView.showGetSubRegionListResponse(response);
                } catch (IOException ex) {
                    SubRegionListResponse exceptionResponse = new SubRegionListResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mGeographyView.showGetSubRegionListResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(SubRegionListResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mGeographyView.showGetSubRegionListResponse(response);
                }
            }
        };
    }

    public Observable<SubRegionListResponse> getSubRegionListObservable(String countryCd, String regionCd) {
        try {
            return getService().getSubRegionListService(getAccessToken(),countryCd, regionCd)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<RegionListResponse> getRegionListSubscriber() {
        return new DefaultSubscriber<RegionListResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    RegionListResponse response = error.getErrorBodyAs(RegionListResponse.class);
                    if (response == null) {
                        response = new RegionListResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mGeographyView.showGetRegionListResponse(response);
                } catch (IOException ex) {
                    RegionListResponse exceptionResponse = new RegionListResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mGeographyView.showGetRegionListResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(RegionListResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mGeographyView.showGetRegionListResponse(response);
                }
            }
        };
    }

    public Observable<RegionListResponse> getRegionListObservable(String countryCd) {
        try {
            return getService().getRegionListService(getAccessToken(),countryCd)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

}
