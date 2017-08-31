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

public class NoteResponse extends BaseServerResponse{
    private String entity;
    private String entityId;
    private String id;
    private String note;
    private String updated;
    private String userId;
}
