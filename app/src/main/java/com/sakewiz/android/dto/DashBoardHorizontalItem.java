package com.sakewiz.android.dto;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/11/17.
 */
@Getter
@Setter
@Parcel
public class DashBoardHorizontalItem {
    private boolean typeAdd;
    private String sakeIdentified;
    private String sakeUnidentified;
}
