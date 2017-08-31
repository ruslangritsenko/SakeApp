package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.Place;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel
public class SearchPlacesResponse extends BaseServerResponse {
    private String page;
    private List<Place> places;
    private String size;
    private String totalPages;
    private String totalSize;
}
