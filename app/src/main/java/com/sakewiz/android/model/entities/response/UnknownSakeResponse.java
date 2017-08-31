package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.UnknownSakeItem;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 7/23/17.
 */
@Getter
@Setter
@Parcel
public class UnknownSakeResponse extends BaseServerResponse {
    public String lastId;
    public List<UnknownSakeItem> results;
}
