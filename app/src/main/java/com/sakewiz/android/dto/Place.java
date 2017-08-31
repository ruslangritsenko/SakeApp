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
public class Place {
    private String barType;
    private String favoured;
    private String id;
    private String imgUrl;
    private String lat;
    private String lon;
    private ProductName name;
    private String rate;
    private String reviewCount;
    private String type;
    private String updated;
    private String userId;
}
