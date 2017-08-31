package com.sakewiz.android.dto;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel
public class Product {
    private String acidity;
    private String alcPerc;
    private String aminoAcid;
    private String avlToPublic;
    private String brewYear;
    private String category;
    private String countryCd;
    private String createType;
    private String created;
    private String favoured;
    private String filterWater;
    private String id;
    private String inProduction;
    private String kakeRatio;
    private String kojiRatio;
    private String labelExist;
    private String mainImgUrl;
    private ProductName name;
    private String pastorizeTankStorage;
    private String pressAndSqueeze;
    private String rate;
    private String regionCd;
    private String reviewCount;
    private String riceKake;
    private String riceKoji;
    private String smv;
    private String subRegionCd;
    private String type;
    private String updated;
    private String userId;
    private String yeast;
    private CountryName countryName;
    private RegionName regionName;
    private SubRegionName subRegionName;
}
