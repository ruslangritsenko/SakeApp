package com.sakewiz.android.dto;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel

public class UserReview {
    private String created;
    private String currentUserLiked;
    private String id;
    private String likes;
    private String rate;
    private String userAffltCode;
    private String userHandle;
    private String userAddress;
    private String userId;
    private UserComment review;
}
