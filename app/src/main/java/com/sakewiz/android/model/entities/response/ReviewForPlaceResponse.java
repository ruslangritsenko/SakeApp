package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.model.entities.response.BaseServerResponse;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Wang Xiaoming on 08/08/17.
 */
@Getter
@Setter
@Parcel

public class ReviewForPlaceResponse extends BaseServerResponse {
    private String favoured;
    private String id;
    private String likes;
    private String rate;
}