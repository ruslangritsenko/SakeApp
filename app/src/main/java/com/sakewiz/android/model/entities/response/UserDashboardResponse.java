package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.Product;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/9/17.
 */
@Getter
@Setter
@Parcel
public class UserDashboardResponse extends BaseServerResponse {
    private String userName;
    private String userHandle;
    private String userProfileImg;
    private String favSakeCount;
    private String favBreweryCount;
    private String favBarCount;
    private String followerCount;
    private String followingCount;
    private String labelCount;
    private String wizPoints;
    private String sakeIdentified;
    private String sakeUnidentified;
    private String notificationCount;
    private List<Product> recommendedProducts;
}
