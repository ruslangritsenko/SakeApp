package com.sakewiz.android.dto;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 7/23/17.
 */
@Getter
@Setter
@Parcel
public class UnknownSakeItem {
    private String scannedUserHndl;
    private String scannedUserId;
    private String scanImg;
    private String created;
}
