package com.sakewiz.android.mvp.views;

import com.sakewiz.android.model.entities.response.CountryListResponse;
import com.sakewiz.android.model.entities.response.CountryResponse;
import com.sakewiz.android.model.entities.response.RegionListResponse;
import com.sakewiz.android.model.entities.response.RegionResponse;
import com.sakewiz.android.model.entities.response.SubRegionListResponse;
import com.sakewiz.android.model.entities.response.SubRegionResponse;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public interface GeographyView extends View {
    void showGetCountryListResponse(CountryListResponse countryListResponse);
    void showGetCountryResponse(CountryResponse countryResponse);
    void showGetRegionResponse(RegionResponse regionResponse);
    void showGetSubRegionResponse(SubRegionResponse subRegionResponse);
    void showGetSubRegionListResponse(SubRegionListResponse subRegionListResponse);
    void showGetRegionListResponse(RegionListResponse regionListResponse);
}