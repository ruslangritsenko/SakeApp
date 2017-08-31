package com.sakewiz.android.mvp.presenters;

/**
 * Created by dilshan_e on 6/9/17.
 */
public interface UserDashBoardPresenter extends Presenter {
    void getBarFacade(String barId);
    void getBreweryFacade(String breweryId);
    void getUserDashboard();
    void getUserNotifications(int pageSize, String lastId);
}
