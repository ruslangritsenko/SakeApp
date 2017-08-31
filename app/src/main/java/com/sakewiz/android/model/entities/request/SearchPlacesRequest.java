package com.sakewiz.android.model.entities.request;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;
/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel
public class SearchPlacesRequest {
    private String barType;
    private double distance;
    private String keyword;
    private float lat;
    private float lon;
//    private String type;
    private String userId;
}
