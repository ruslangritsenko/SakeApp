package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.request.SearchPlacesRequest;
import com.sakewiz.android.model.entities.request.SearchProductsRequest;
import com.sakewiz.android.model.entities.response.SearchPlacesResponse;
import com.sakewiz.android.model.entities.response.SearchProductResponse;
import com.sakewiz.android.model.rest.SakeWizService;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by dilshan_e on 6/10/17.
 */
public class SearchServiceImpl implements SearchService {

    private SakeWizService sakeWizService;

    public SearchServiceImpl(SakeWizService nzsService) {
        super();
        this.sakeWizService = nzsService;
    }

    @Override
    public Observable<SearchPlacesResponse> doSearchPlacesService(String authorizationKey, int page, int size, SearchPlacesRequest searchPlacesRequest) {
        return sakeWizService.getApi()
                .doSearchPlacesAPI(authorizationKey, page, size, searchPlacesRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<SearchPlacesResponse> doSearchBreweriesService(String authorizationKey, int page, int size, SearchPlacesRequest searchPlacesRequest) {
        return sakeWizService.getApi()
                .doSearchBreweriesAPI(authorizationKey, page, size, searchPlacesRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<SearchProductResponse> doSearchProductsService(String authorizationKey, int page, int size, SearchProductsRequest searchProductsRequest) {
        return sakeWizService.getApi()
                .doSearchProductsAPI(authorizationKey, page, size, searchProductsRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<String>> getSuggestPlacesService(String authorizationKey, String text, int count) {
        return sakeWizService.getApi()
                .getSuggestPlacesAPI(authorizationKey, text, count)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<String>> getSuggestBreweriesService(String authorizationKey, String text, int count) {
        return sakeWizService.getApi()
                .getSuggestBreweriesAPI(authorizationKey, text, count)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<String>> getSuggestProductsService(String authorizationKey, String text, int count) {
        return sakeWizService.getApi()
                .getSuggestProductsAPI(authorizationKey, text, count)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
