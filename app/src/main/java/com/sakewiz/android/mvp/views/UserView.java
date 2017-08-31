package com.sakewiz.android.mvp.views;


import com.sakewiz.android.model.entities.response.CreateNewUserResponse;
import com.sakewiz.android.model.entities.response.LoginResponse;
import com.sakewiz.android.model.entities.response.ResetPwdNotifyResponse;
import com.sakewiz.android.model.entities.response.UserDetailOfGivenUserResponse;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public interface UserView extends View {
    void showCreateNewUserResponse(CreateNewUserResponse createNewUserResponse);
    void showLoginResponse(LoginResponse loginResponse);
    void showResetPasswordNotifyResponse(ResetPwdNotifyResponse resetPwdNotifyResponse);
    void showGetUserDetailOfGivenUserResponse(UserDetailOfGivenUserResponse userDetailOfGivenUserResponse);
}
