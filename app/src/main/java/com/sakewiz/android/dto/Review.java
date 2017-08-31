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
public class Review {
    private String countryName;
    private String smv;
    private RegionName regionName;
}
