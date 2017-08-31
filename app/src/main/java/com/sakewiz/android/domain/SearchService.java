package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.request.SearchPlacesRequest;
import com.sakewiz.android.model.entities.request.SearchProductsRequest;
import com.sakewiz.android.model.entities.response.SearchPlacesResponse;
import com.sakewiz.android.model.entities.response.SearchProductResponse;

import java.util.List;
import rx.Observable;

/**
 * Created by dilshan_e on 6/10/17.
 */

public interface SearchService extends Service {
    Observable<SearchPlacesResponse> doSearchPlacesService(String authorizationKey,  int page, int size, SearchPlacesRequest searchPlacesRequest);
    Observable<SearchPlacesResponse> doSearchBreweriesService(String authorizationKey,  int page, int size, SearchPlacesRequest searchPlacesRequest);
    Observable<SearchProductResponse> doSearchProductsService(String authorizationKey, int page, int size, SearchProductsRequest searchProductsRequest);
    Observable<List<String>> getSuggestPlacesService(String authorizationKey, String text, int count);
    Observable<List<String>> getSuggestBreweriesService(String authorizationKey, String text, int count);
    Observable<List<String>> getSuggestProductsService(String authorizationKey, String text, int count);
}
