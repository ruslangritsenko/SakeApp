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
public class BaseServerResponse {
    private int status_code;
    private boolean success;
    private boolean isAPIError;
    private String timestamp;
    private String status;
    private  String error;
    private String message;
    private String path;
    private String[] codes;
}