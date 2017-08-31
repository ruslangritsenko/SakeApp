package com.sakewiz.android.mvp.presenters;

import com.sakewiz.android.model.entities.request.CreateNewUserRequest;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public interface UserPresenter extends Presenter {
    void createNewUser(String authorizationKey,CreateNewUserRequest createNewUserRequest);
    void doLogin(String authorizationKey);
    void doResetPasswordNotify(String authorizationKey, String userHandle, String email);
    void getUserDetailOfGivenUser(String userHandle);
}
