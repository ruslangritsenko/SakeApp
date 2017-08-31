package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.model.entities.response.BaseServerResponse;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel

public class ReviewForProductResponse extends BaseServerResponse {
    private String favoured;
    private String id;
    private String likes;
    private String rate;
}
