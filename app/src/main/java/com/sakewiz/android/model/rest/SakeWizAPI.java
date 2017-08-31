package com.sakewiz.android.model.rest;


import com.sakewiz.android.model.entities.request.CreateProductNoteRequest;
import com.sakewiz.android.model.entities.response.CreateProductNoteResponse;
import com.sakewiz.android.model.entities.response.FavouritePlacesResponse;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.NoteResponse;
import com.sakewiz.android.model.entities.response.ReviewForPlaceResponse;
import com.sakewiz.android.model.entities.response.ReviewForProductResponse;
import com.sakewiz.android.model.entities.request.CreateNewSuperUserRequest;
import com.sakewiz.android.model.entities.request.CreateNewUserRequest;
import com.sakewiz.android.model.entities.request.FollowUserRequest;
import com.sakewiz.android.model.entities.request.ResetPwdModifyRequest;
import com.sakewiz.android.model.entities.request.ReviewForProductRequest;
import com.sakewiz.android.model.entities.request.SearchPlacesRequest;
import com.sakewiz.android.model.entities.request.SearchProductsRequest;
import com.sakewiz.android.model.entities.request.UnfollowUserRequest;
import com.sakewiz.android.model.entities.request.UpdateUserInfoRequest;
import com.sakewiz.android.model.entities.request.UploadProfilePicRequest;
import com.sakewiz.android.model.entities.request.VerifyUserEmailRequest;
import com.sakewiz.android.model.entities.response.BarFacadeResponse;
import com.sakewiz.android.model.entities.response.BreweryFacadeResponse;
import com.sakewiz.android.model.entities.response.CountryListResponse;
import com.sakewiz.android.model.entities.response.CountryResponse;
import com.sakewiz.android.model.entities.response.CreateNewSuperUserResponse;
import com.sakewiz.android.model.entities.response.CreateNewUserResponse;
import com.sakewiz.android.model.entities.response.FollowUserResponse;
import com.sakewiz.android.model.entities.response.FollowedByResponse;
import com.sakewiz.android.model.entities.response.FollowersResponse;
import com.sakewiz.android.model.entities.response.LoggedUserProfileResponse;
import com.sakewiz.android.model.entities.response.LoginResponse;
import com.sakewiz.android.model.entities.response.NotificationCountResponse;
import com.sakewiz.android.model.entities.response.NotificationResponse;
import com.sakewiz.android.model.entities.response.ProductDetailResponse;
import com.sakewiz.android.model.entities.response.ProductFacadeResponse;
import com.sakewiz.android.model.entities.response.RegionListResponse;
import com.sakewiz.android.model.entities.response.RegionResponse;
import com.sakewiz.android.model.entities.response.ResetPwdModifyResponse;
import com.sakewiz.android.model.entities.response.ResetPwdNotifyResponse;
import com.sakewiz.android.model.entities.response.ReviewResponse;
import com.sakewiz.android.model.entities.response.SearchPlacesResponse;
import com.sakewiz.android.model.entities.response.SearchProductResponse;
import com.sakewiz.android.model.entities.response.SubRegionListResponse;
import com.sakewiz.android.model.entities.response.SubRegionResponse;
import com.sakewiz.android.model.entities.response.UnfollowUserResponse;
import com.sakewiz.android.model.entities.response.UnknownSakeResponse;
import com.sakewiz.android.model.entities.response.UpdateUserInfoResponse;
import com.sakewiz.android.model.entities.response.UploadProfilePicResponse;
import com.sakewiz.android.model.entities.response.UserByIdsResponse;
import com.sakewiz.android.model.entities.response.UserDashboardResponse;
import com.sakewiz.android.model.entities.response.UserDetailOfGivenUserResponse;
import com.sakewiz.android.model.entities.response.UsersDetailResponse;
import com.sakewiz.android.model.entities.response.VerifyUserEmailResponse;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public interface SakeWizAPI {

    // -------------------------- user-service ------------------------------------------

    @POST("/account/email/verification")
    Observable<VerifyUserEmailResponse> doVerifyUserEmailAPI(@Header("Authorization") String access_token, @Body VerifyUserEmailRequest verifyUserEmailRequest);

    @POST("/account/password/reset/modify")
    Observable<ResetPwdModifyResponse> doResetPasswordModifyAPI(@Header("Authorization") String access_token, @Body ResetPwdModifyRequest resetPwdModifyRequest);

    @POST("/account/password/reset/notify")
    Observable<ResetPwdNotifyResponse> doResetPasswordNotifyAPI(@Header("Authorization") String access_token, @Query("userHandle") String userHandle, @Query("email") String email);

    @POST("/super-user")
    Observable<CreateNewSuperUserResponse> doCreateNewSuperUserAPI(@Header("Authorization") String access_token, @Body CreateNewSuperUserRequest createNewSuperUserRequest);

    @GET("/user")
    Observable<LoggedUserProfileResponse> getLoggedUserProfileAPI(@Header("Authorization") String access_token);

    @POST ("/user")
    Observable<UpdateUserInfoResponse> updateUserInfoAPI(@Header("Authorization") String access_token, @Body UpdateUserInfoRequest updateUserInfoRequest);

    @PUT("/user")
    Observable<CreateNewUserResponse> createNewUserAPI(@Header("Authorization") String authorizationKey, @Body CreateNewUserRequest createNewUserRequest);

    @POST ("/user/follow/{userId}")
    Observable<FollowUserResponse> followUserAPI(@Header("Authorization") String access_token, @Body FollowUserRequest followUserRequest);

    @GET ("/user/followedBy/{userId}")
    Observable<FollowedByResponse> getFollowedByAPI(@Header("Authorization") String access_token);

    @GET ("/user/followers/{userId}")
    Observable<FollowersResponse> getFollowersAPI(@Header("Authorization") String access_token);

    @GET ("/user/ids")
    Observable<UserByIdsResponse> getUsersByIdsAPI(@Header("Authorization") String access_token);

    @POST ("/user/login")
    Observable<LoginResponse> doLoginAPI(@Header("Authorization") String authorizationKey);

    @GET ("/facade/user/notifications")
    Observable<NotificationResponse> getNotificationsAPI(@Header("Authorization") String access_token, @Query("size") int pageSize, @Query("lastId") String lastId);

    @GET ("/user/notifications/count")
    Observable<NotificationCountResponse> getNotificationCountAPI (@Header("Authorization") String access_token);

    @POST ("/user/profilepic")
    Observable<UploadProfilePicResponse> uploadUserProfilePicAPI(@Header("Authorization") String access_token, @Body UploadProfilePicRequest uploadProfilePicRequest);

    @POST ("/user/unfollow/{userId}")
    Observable<UnfollowUserResponse> doUnfollowUserAPI(@Header("Authorization") String access_token, @Body UnfollowUserRequest unfollowUserRequest);

    @GET ("/user/{userHandle}")
    Observable<UserDetailOfGivenUserResponse> getUserDetailOfGivenUserAPI(@Header("Authorization") String access_token, @Path(value = "userHandle", encoded = true) String userHandle);

    @GET ("/users")
    Observable<UsersDetailResponse> getUsersDetailWithPaginationsAPI(@Header("Authorization") String access_token);


    // -------------------------- user-dashboard-facade ------------------------------------------

    @GET ("/facade/bar/{barId}")
    Observable<BarFacadeResponse> getBarFacadeAPI(@Header("Authorization") String access_token, @Path(value = "barId", encoded = true) String barId);

    @GET ("/facade/brewery/{breweryId}")
    Observable<BreweryFacadeResponse> getBreweryFacadeAPI(@Header("Authorization") String access_token, @Path(value = "breweryId", encoded = true) String breweryId);

    @GET ("/facade/user")
    Observable<UserDashboardResponse> getUserDashboardAPI(@Header("Authorization") String access_token);

    @GET ("facade/user/fav/products")
    Observable<FavouriteSakeResponse> getFavouriteSakeAPI(@Header("Authorization") String access_token);

    // -------------------------- search-service ------------------------------------------

    @POST ("/search/bars")
    Observable<SearchPlacesResponse> doSearchPlacesAPI(@Header("Authorization") String access_token,  @Query("page") int page, @Query("size") int size, @Body SearchPlacesRequest searchPlacesRequest);

    @POST ("/search/breweries")
    Observable<SearchPlacesResponse> doSearchBreweriesAPI(@Header("Authorization") String access_token,  @Query("page") int page, @Query("size") int size, @Body SearchPlacesRequest searchPlacesRequest);

    @POST ("/search/products")
    Observable<SearchProductResponse> doSearchProductsAPI(@Header("Authorization") String access_token, @Query("page") int page, @Query("size") int size, @Body SearchProductsRequest searchProductsRequest);

    @GET ("/search/suggest/bars")
    Observable<List<String>> getSuggestPlacesAPI(@Header("Authorization") String access_token, @Query("text") String text, @Query("count") int count);

    @GET ("/search/suggest/breweries")
    Observable<List<String>> getSuggestBreweriesAPI(@Header("Authorization") String access_token, @Query("text") String text, @Query("count") int count);

    @GET ("/search/suggest/products")
    Observable<List<String>> getSuggestProductsAPI(@Header("Authorization") String access_token, @Query("text") String text, @Query("count") int count);


    // -------------------------- geography-controller ---------------------------------------------

    @GET ("/geography/countries")
    Observable<CountryListResponse> getCountryListAPI(@Header("Authorization") String access_token);

    @GET ("/geography/country/{countryCd}")
    Observable<CountryResponse> getCountryAPI(@Header("Authorization") String access_token, @Path(value = "countryCd", encoded = true) String countryCd);

    @GET ("/geography/country/{countryCd}/region/{regionCd}")
    Observable<RegionResponse> getRegionAPI(@Header("Authorization") String access_token, @Path(value = "countryCd", encoded = true) String countryCd,
                                            @Path(value = "regionCd", encoded = true) String regionCd);

    @GET ("/geography/country/{countryCd}/region/{regionCd}/subregion/{subRegionCd}")
    Observable<SubRegionResponse> getSubRegionAPI(@Header("Authorization") String access_token, @Path(value = "countryCd", encoded = true) String countryCd,
                                                   @Path(value = "regionCd", encoded = true) String regionCd, @Path(value = "subRegionCd", encoded = true) String subRegionCd);

    @GET ("/geography/country/{countryCd}/region/{regionCd}/subregions")
    Observable<SubRegionListResponse> getSubRegionListAPI(@Header("Authorization") String access_token, @Path(value = "countryCd", encoded = true) String countryCd,
                                                          @Path(value = "regionCd", encoded = true) String regionCd);

    @GET ("/geography/country/{countryCd}/regions")
    Observable<RegionListResponse> getRegionListAPI(@Header("Authorization") String access_token, @Path(value = "countryCd", encoded = true) String countryCd);


    // -------------------------- product-service ---------------------------------------------

    @GET ("/product/{productId}")
    Observable<ProductDetailResponse> getProductDetailAPI(@Header("Authorization") String access_token, @Path(value = "productId", encoded = true) String productId);

    @GET ("/facade/product/{productId}")
    Observable<ProductFacadeResponse> getProductFacadeAPI(@Header("Authorization") String access_token, @Path(value = "productId", encoded = true) String productId);

    @GET ("/reviews/{entityId}")
    Observable<ReviewResponse> getReviewsAPI(@Header("Authorization") String access_token, @Path(value = "entityId", encoded = true) String entityId, @Query("lastId") String lastId, @Query("size") int size);

    @POST ("/review/product/{productId}")
    Observable<ReviewForProductResponse> doReviewProductAPI(@Header("Authorization") String access_token, @Path(value = "productId", encoded = true) String productId, @Body ReviewForProductRequest reviewForProductRequest);

    @POST ("/like/review/{reviewId}")
    Observable<ReviewForProductResponse> doLikeReviewAPI(@Header("Authorization") String access_token, @Path(value = "reviewId", encoded = true) String reviewId);

    @POST ("/unfavour/product/{productId}")
    Observable<ReviewForProductResponse> doUnFavourProductAPI(@Header("Authorization") String access_token, @Path(value = "productId", encoded = true) String productId);

    @POST ("/favour/product/{productId}")
    Observable<ReviewForProductResponse> doFavourProductAPI(@Header("Authorization") String access_token, @Path(value = "productId", encoded = true) String productId);

    @GET ("/facade/products/unidentified")
    Observable<UnknownSakeResponse> getUnknownSakeAPI(@Header("Authorization") String access_token, @Query("size") int pageSize, @Query("lastId") String lastId);


    // -------------------------- place-service ---------------------------------------------
    @GET ("facade/user/fav/bars")
    Observable<FavouritePlacesResponse> getFavouritePlaceBarsAPI(@Header("Authorization") String access_token);

    @GET ("facade/user/fav/breweries")
    Observable<FavouritePlacesResponse> getFavouritePlaceBreweriesAPI(@Header("Authorization") String access_token);

    @POST ("/unfavour/bar/{barId}")
    Observable<ReviewForPlaceResponse> doUnFavourPlaceBarAPI(@Header("Authorization") String access_token, @Path(value = "barId", encoded = true) String barId);

    @POST ("/unfavour/brewery/{breweryId}")
    Observable<ReviewForPlaceResponse> doUnFavourPlaceBreweryAPI(@Header("Authorization") String access_token, @Path(value = "breweryId", encoded = true) String breweryId);

    // --------------------------  NoteResponse Service ---------------------------------------------

    @GET ("/notes")
    Observable<List<NoteResponse>> getNotesAPI(@Header("Authorization") String access_token, @Query("type") String type);

    @GET ("/note/{entityId}")
    Observable<NoteResponse> getNoteByUserIdAndEntityIdAPI(@Header("Authorization") String access_token, @Path(value = "entityId", encoded = true) String entityId);

    @POST ("/note/product/{productId}")
    Observable<CreateProductNoteResponse> doCreateProductNoteAPI(@Header("Authorization") String access_token, @Path(value = "productId", encoded = true) String productId, @Body CreateProductNoteRequest createProductNoteRequest);
}
