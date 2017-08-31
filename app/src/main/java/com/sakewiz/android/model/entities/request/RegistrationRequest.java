package com.sakewiz.android.model.entities.request;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;
/**
 * Created by dilshan_e on 29/05/2017.
 */
@Getter
@Setter
@Parcel
public class RegistrationRequest {
        public String Email;
        public String Password;
        public String ConfirmPassword;
        public String UserName;
        public String Address1;
        public String Address2;
        public String Address3;
        public String Address4;
        public String PhoneNumber;
        public String PhoneNumberMobile;
        public String PostCode;
}
