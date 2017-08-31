package com.sakewiz.android.model.entities.request;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel
public class CreateNewUserRequest {
    private String email;
    private String hndl;
    private String lang;
    private String pwd;
}
