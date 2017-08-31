package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.response.CountryListResponse;
import com.sakewiz.android.model.entities.response.CountryResponse;
import com.sakewiz.android.model.entities.response.RegionListResponse;
import com.sakewiz.android.model.entities.response.RegionResponse;
import com.sakewiz.android.model.entities.response.SubRegionListResponse;
import com.sakewiz.android.model.entities.response.SubRegionResponse;
import com.sakewiz.android.model.rest.SakeWizService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public class GeographyServiceImpl implements GeographyService {

    private SakeWizService sakeWizService;

    public GeographyServiceImpl(SakeWizService nzsService) {
        super();
        this.sakeWizService = nzsService;
    }

    @Override
    public Observable<CountryListResponse> getCountryListService(String authorizationKey) {
        return sakeWizService.getApi()
                .getCountryListAPI(authorizationKey)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CountryResponse> getCountryService(String authorizationKey, String countryCd) {
        return sakeWizService.getApi()
                .getCountryAPI(authorizationKey, countryCd)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<RegionResponse> getRegionService(String authorizationKey, String countryCd, String regionCd) {
        return sakeWizService.getApi()
                .getRegionAPI(authorizationKey, countryCd, regionCd)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<SubRegionResponse> getSubRegionService(String authorizationKey, String countryCd, String regionCd, String subRegionCd) {
        return sakeWizService.getApi()
                .getSubRegionAPI(authorizationKey, countryCd, regionCd, subRegionCd)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<SubRegionListResponse> getSubRegionListService(String authorizationKey, String countryCd, String regionCd) {
        return sakeWizService.getApi()
                .getSubRegionListAPI(authorizationKey, countryCd, regionCd)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<RegionListResponse> getRegionListService(String authorizationKey, String countryCd) {
        return sakeWizService.getApi()
                .getRegionListAPI(authorizationKey, countryCd)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
