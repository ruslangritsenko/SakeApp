package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.CountryName;
import com.sakewiz.android.dto.RegionName;
import com.sakewiz.android.dto.UserComment;
import com.sakewiz.android.dto.UserReview;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel

public class ProductFacadeResponse extends BaseServerResponse {
    private String avgRate;
    private CountryName countryName;
    private ProductDetailResponse product;
    private RegionName regionName;
    private String totalFavourites;
    private String totalRates;
    private String userFavourite;
    private String userHasNote;
    private String userRate;
    private UserComment userReview;
}
