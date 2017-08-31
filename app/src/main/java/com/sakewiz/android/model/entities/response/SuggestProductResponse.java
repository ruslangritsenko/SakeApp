package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.Product;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/10/17.
 */
@Getter
@Setter
@Parcel
public class SuggestProductResponse extends BaseServerResponse {
    private String page;
    private Product products;
    private String size;
    private String totalPages;
    private String totalSize;
}
