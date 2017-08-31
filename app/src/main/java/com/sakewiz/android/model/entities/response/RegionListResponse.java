package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.RegionResult;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 /**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel

public class RegionListResponse extends BaseServerResponse{
    private List<RegionResult> results;
}
