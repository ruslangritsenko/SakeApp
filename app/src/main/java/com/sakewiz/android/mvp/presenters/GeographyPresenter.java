package com.sakewiz.android.mvp.presenters;


/**
 * Created by dilshan_e on 29/05/2017.
 */
public interface GeographyPresenter extends Presenter {
    void getCountryList();
    void getCountry(String countryCd);
    void getRegion(String countryCd, String regionCd);
    void getSubRegion(String countryCd, String regionCd, String subRegionCd);
    void getSubRegionList(String countryCd, String regionCd);
    void getRegionList(String countryCd);
}
