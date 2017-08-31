package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.response.CountryListResponse;
import com.sakewiz.android.model.entities.response.CountryResponse;
import com.sakewiz.android.model.entities.response.RegionListResponse;
import com.sakewiz.android.model.entities.response.RegionResponse;
import com.sakewiz.android.model.entities.response.SubRegionListResponse;
import com.sakewiz.android.model.entities.response.SubRegionResponse;

import rx.Observable;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public interface GeographyService extends Service {
    Observable<CountryListResponse> getCountryListService(String authorizationKey);
    Observable<CountryResponse> getCountryService(String authorizationKey, String countryCd);
    Observable<RegionResponse> getRegionService(String authorizationKey,String countryCd, String regionCd);
    Observable<SubRegionResponse> getSubRegionService(String authorizationKey, String countryCd, String regionCd, String subRegionCd);
    Observable<SubRegionListResponse> getSubRegionListService(String authorizationKey, String countryCd, String regionCd);
    Observable<RegionListResponse> getRegionListService(String authorizationKey, String countryCd);
}
