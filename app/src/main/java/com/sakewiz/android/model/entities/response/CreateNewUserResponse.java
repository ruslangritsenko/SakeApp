package com.sakewiz.android.model.entities.response;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel
public class CreateNewUserResponse extends BaseServerResponse {
    private String id;
    private String hndl;
    private String pwd;
    private String type;
    private String email;
    private String name;
    private String motto;
    private String pic;
    private String pnts;
    private String affltCode;
    private String country;
    private String state;
    private String lang;
    private String active;
    private String confirmed;
    private String created;
    private String updated;
}
