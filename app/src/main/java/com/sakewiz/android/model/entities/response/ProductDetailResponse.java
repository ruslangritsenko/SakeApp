package com.sakewiz.android.model.entities.response;

import com.sakewiz.android.dto.LongDesc;
import com.sakewiz.android.dto.ProductName;
import com.sakewiz.android.dto.ShortDesc;

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
public class ProductDetailResponse extends BaseServerResponse {

    private String acidity;
    private String alcPerc;
    private String aminoAcid;
    private List<String> aromaObservation;
    private String avlToPublic;
    private String brewYear;
    private String category;
    private String countryCd;
    private String createType;
    private String created;
    private String deleted;
    private String filterWater;
    private String flavorType;
    private String id;
    private List<String> imgs;
    private String inProduction;
    private String kakeRatio;
    private String kojiRatio;
    private String lblImg;
    private List<String> lblImgs;
    private LongDesc longDesc;
    private String mainImg;
    private ProductName name;
    private String pastorizeTankStorage;
    private String pressAndSqueeze;
    private String regionCd;
    private String riceKake;
    private String riceKoji;
    private ShortDesc shortDesc;
    private String size;
    private String sizeType;
    private String smv;
    private String subRegionCd;
    private List<String> tasteObservation;
    private String tempChilled;
    private String tempHot;
    private String tempIce;
    private String tempRoom;
    private String tempVHot;
    private String tempWarm;
    private String type;
    private String updated;
    private String userId;
    private String yeast;
}
