package com.sakewiz.android.dto;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/23/17.
 */
@Getter
@Setter
@Parcel
public class ProductDetailHorizontalItem {
    private boolean typeAdd;
    private String country;
    private String region;
    private String smv;
    private String polishRate;
    private String alcoholContent;
    private String brewingYear;
    private String storageType;
    private String pressSqueez;
}
