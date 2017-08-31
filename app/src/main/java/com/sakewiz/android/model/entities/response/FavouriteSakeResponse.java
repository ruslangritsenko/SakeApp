package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.Place;
import com.sakewiz.android.dto.Product;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Wang Xiaoming on 09/08/17.
 */
@Getter
@Setter
@Parcel
public class FavouriteSakeResponse extends BaseServerResponse {
    private String page;
    private List<Product> products;
    private String size;
    private String totalPages;
    private String totalSize;
}