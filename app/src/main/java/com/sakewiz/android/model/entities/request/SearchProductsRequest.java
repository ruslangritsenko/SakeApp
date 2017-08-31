package com.sakewiz.android.model.entities.request;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel
public class SearchProductsRequest {
//    private int acidityFrom;
//    private int acidityTo;
//    private int alcPercFrom;
//    private int alcPercTo;
//    private int aminoAcidFrom;
//    private int aminoAcidTo;
    private String availablityType;
//    private int brewYearFrom;
//    private int brewYearTo;
    private String category;
    private String countryCd;
    private String createType;
    private String filterWater;
    private List<String> filterWaters;
//    private int kakeRatioFrom;
//    private int kakeRatioTo;
    private String keyword;
//    private int kojiRatioFrom;
//    private int kojiRatioTo;
    private String pastorizeTankStorage;
    private String pressAndSqueeze;
    private List<String> pressAndSqueezes;
    private String regionCd;
    private String riceKake;
    private String riceKoji;
//    private int smvfrom;
//    private int smvto;
    private String sortBy;
    private String sortOrder;
    private String subRegionCd;
    private String type;
    private List<String> types;
    private String userId;
    private String yeast;
    private List<String> yeasts;
//    private String radius;
}
