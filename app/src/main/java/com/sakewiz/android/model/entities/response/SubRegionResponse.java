package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.SubRegionName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel
public class SubRegionResponse extends BaseServerResponse {
    private String id;
    private SubRegionName name;
}
