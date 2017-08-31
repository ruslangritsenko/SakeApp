package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.Product;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 6/3/17.
 */
@Getter
@Setter
@Parcel
public class SearchProductResponse extends BaseServerResponse {
    private String page;
    private List<Product> products;
    private String size;
    private String totalPages;
    private String totalSize;
}
