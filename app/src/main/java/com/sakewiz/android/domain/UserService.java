package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.request.CreateNewUserRequest;
import com.sakewiz.android.model.entities.response.CreateNewUserResponse;
import com.sakewiz.android.model.entities.response.LoginResponse;
import com.sakewiz.android.model.entities.response.ResetPwdNotifyResponse;
import com.sakewiz.android.model.entities.response.UserDetailOfGivenUserResponse;

import rx.Observable;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public interface UserService extends Service {
    Observable<CreateNewUserResponse> createNewUserService(String authorizationKey, CreateNewUserRequest createNewUserRequest);
    Observable<LoginResponse> doLoginService(String authorizationKey);
    Observable<ResetPwdNotifyResponse> doResetPasswordNotifyService(String authorizationKey, String userHandle, String email);
    Observable<UserDetailOfGivenUserResponse> getUserDetailOfGivenUserService(String authorizationKey, String userHandle);

}
