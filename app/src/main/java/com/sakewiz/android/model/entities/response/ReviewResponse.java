package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.UserReview;

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

public class ReviewResponse extends BaseServerResponse {
    private String avgRate;
    private String lastId;
    private List<UserReview> reviews;
    private String totalReviews;
}
