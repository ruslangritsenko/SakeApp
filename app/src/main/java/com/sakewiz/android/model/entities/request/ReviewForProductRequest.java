package com.sakewiz.android.model.entities.request;

import com.sakewiz.android.dto.Comments;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel

public class ReviewForProductRequest {
    private Comments comment;
    private int rate;
}
