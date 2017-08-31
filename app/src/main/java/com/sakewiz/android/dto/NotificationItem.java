package com.sakewiz.android.dto;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 7/12/17.
 */
@Getter
@Setter
@Parcel
public class NotificationItem {
    private String id;
    private String type;
    private String created;
    private String points;
    private String reviewedProductId;
    private String commentId;
    private String likedUserId;
    private String likedUserHandle;
    private String likedProductId;
    private String likedUserName;
    private String likedUserImg;
    private String likedBarId;
    private String likedBreweryId;
    private String followerUserId;
    private String followerUserHandle;
    private String followerUserName;
    private String followerUserImg;
    private String matchedProductId;
    private String matchedProductImg;
    private String reviewedBarId;
    private String reviewedBreweryId;
    private ProductName productName;
    private ProductName likedProductName;
    private ProductName likedBarName;
    private ProductName likedBreweryName;
    private ProductName matchedProductName;
    private ProductName barName;
    private ProductName breweryName;
}
