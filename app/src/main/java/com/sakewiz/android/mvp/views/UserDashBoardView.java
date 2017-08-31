package com.sakewiz.android.mvp.views;

import com.sakewiz.android.model.entities.response.BarFacadeResponse;
import com.sakewiz.android.model.entities.response.BreweryFacadeResponse;
import com.sakewiz.android.model.entities.response.FavouritePlacesResponse;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.NotificationResponse;
import com.sakewiz.android.model.entities.response.UserDashboardResponse;

/**
 * Created by dilshan_e on 6/9/17.
 */

public interface UserDashBoardView extends View {
    void showGetBarFacadeResponse(BarFacadeResponse barFacadeResponse);
    void showGetBreweryFacadeResponse(BreweryFacadeResponse breweryFacadeResponse);
    void showGetUserDashboardResponse(UserDashboardResponse userDashboardResponse);
    void showUserNotificationsResponse(NotificationResponse notificationResponse);
}
