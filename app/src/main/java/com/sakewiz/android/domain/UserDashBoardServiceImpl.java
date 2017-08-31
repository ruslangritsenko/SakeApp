package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.response.BarFacadeResponse;
import com.sakewiz.android.model.entities.response.BreweryFacadeResponse;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.NotificationResponse;
import com.sakewiz.android.model.entities.response.UserDashboardResponse;
import com.sakewiz.android.model.rest.SakeWizService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by dilshan_e on 6/9/17.
 */

public class UserDashBoardServiceImpl implements UserDashBoardService{

    private SakeWizService sakeWizService;

    public UserDashBoardServiceImpl(SakeWizService nzsService) {
        super();
        this.sakeWizService = nzsService;
    }

    @Override
    public Observable<BarFacadeResponse> getBarFacadeService(String authorizationKey, String barId) {
        return sakeWizService.getApi()
                .getBarFacadeAPI(authorizationKey,barId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BreweryFacadeResponse> getBreweryFacadeService(String authorizationKey, String breweryId) {
        return sakeWizService.getApi()
                .getBreweryFacadeAPI(authorizationKey,breweryId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserDashboardResponse> getUserDashboardService(String authorizationKey) {
        return sakeWizService.getApi()
                .getUserDashboardAPI(authorizationKey)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<NotificationResponse> getUserNotificationsService(String authorizationKey, int pageSize, String lastId) {
        return sakeWizService.getApi()
                .getNotificationsAPI(authorizationKey, pageSize, lastId)
                .observeOn(AndroidSchedulers.mainThread());
    }

}
