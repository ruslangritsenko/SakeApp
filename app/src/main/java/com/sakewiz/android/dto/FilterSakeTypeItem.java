package com.sakewiz.android.dto;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 29/05/2017.
 */
@Getter
@Setter
@Parcel
public class FilterSakeTypeItem {
    private String sakeType;
    private boolean selected;
}
