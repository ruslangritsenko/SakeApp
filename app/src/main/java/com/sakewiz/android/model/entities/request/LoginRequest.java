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
public class LoginRequest {
    private String Email;
    private String Password;
}
