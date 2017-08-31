package com.sakewiz.android.dto;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class SettingsItem {
    private String itemName;
    private String itemCategory;
    private boolean setDivider;
}
