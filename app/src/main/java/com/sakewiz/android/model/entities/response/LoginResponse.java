package com.sakewiz.android.model.entities.response;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;
/**
 * Created by dilshan_e on 29/05/2017.
 */
@Getter
@Setter
@Parcel
public class LoginResponse extends BaseServerResponse {
        private String active;
        private String affltCode;
        private String confirmed;
        private String country;
        private String created;
        private String email;
        private String hndl;
        private String id;
        private String lang;
        private String motto;
        private String name;
        private String pic;
        private String pnts;
        private String pwd;
        private String state;
        private String type;
        private String updated;
}
