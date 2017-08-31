package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.response.BarFacadeResponse;
import com.sakewiz.android.model.entities.response.BreweryFacadeResponse;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.NotificationResponse;
import com.sakewiz.android.model.entities.response.UserDashboardResponse;

import rx.Observable;

/**
 * Created by dilshan_e on 6/9/17.
 */

public interface UserDashBoardService extends Service {
    Observable<BarFacadeResponse> getBarFacadeService(String authorizationKey, String barId);
    Observable<BreweryFacadeResponse> getBreweryFacadeService(String authorizationKey, String breweryId);
    Observable<UserDashboardResponse> getUserDashboardService(String authorizationKey);
    Observable<NotificationResponse> getUserNotificationsService(String authorizationKey, int pageSize, String lastId);
}
