package com.sakewiz.android.domain;


import com.sakewiz.android.model.entities.request.CreateNewUserRequest;
import com.sakewiz.android.model.entities.response.CreateNewUserResponse;
import com.sakewiz.android.model.entities.response.LoginResponse;
import com.sakewiz.android.model.entities.response.ResetPwdNotifyResponse;
import com.sakewiz.android.model.entities.response.UserDetailOfGivenUserResponse;
import com.sakewiz.android.model.rest.SakeWizService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public class UserServiceImpl implements UserService {

    private SakeWizService sakeWizService;

    public UserServiceImpl(SakeWizService nzsService) {
        super();
        this.sakeWizService = nzsService;
    }

    @Override
    public Observable<CreateNewUserResponse> createNewUserService(String authorizationKey, CreateNewUserRequest createNewUserRequest) {
        return sakeWizService.getApi()
                .createNewUserAPI(authorizationKey,createNewUserRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<LoginResponse> doLoginService(String authorizationKey) {
        return sakeWizService.getApi()
                .doLoginAPI(authorizationKey)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResetPwdNotifyResponse> doResetPasswordNotifyService(String authorizationKey, String userHandle, String email) {
        return sakeWizService.getApi()
                .doResetPasswordNotifyAPI(authorizationKey,userHandle,email)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserDetailOfGivenUserResponse> getUserDetailOfGivenUserService(String authorizationKey, String userHandle) {
        return sakeWizService.getApi()
                .getUserDetailOfGivenUserAPI(authorizationKey,userHandle)
                .observeOn(AndroidSchedulers.mainThread());
    }

}
