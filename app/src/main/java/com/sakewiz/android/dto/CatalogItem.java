package com.sakewiz.android.dto;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
* Created by dilshan_e on 29/05/2017.
*/
@Getter
@Setter
@Parcel
public class CatalogItem {
        public String GroupID;
        public String Name;
        public List<CatalogItem> SubCategories;
}
