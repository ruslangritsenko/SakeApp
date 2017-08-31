package com.sakewiz.android.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakewiz.android.R;
import com.sakewiz.android.dto.ProductDetailHorizontalItem;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.List;

/**
 * Created by dilshan_e on 22/06/2017.
 */
public class ProductDetailEnchantedPagerAdapter extends PagerAdapter{

    Context mContext;

    LayoutInflater inflater;

    final List<ProductDetailHorizontalItem> mAlbumlist;

    public ProductDetailEnchantedPagerAdapter(Context context, List<ProductDetailHorizontalItem> albumList) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mAlbumlist = albumList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CardView mCurrentView = (CardView) inflater.inflate(R.layout.product_detail_horizontal_item, container, false);

        TextView country = (TextView) mCurrentView.findViewById(R.id.txt_country);
        TextView region = (TextView) mCurrentView.findViewById(R.id.txt_region);
        TextView smv = (TextView) mCurrentView.findViewById(R.id.txt_smv);
        TextView polishRate = (TextView) mCurrentView.findViewById(R.id.txt_polish_rate);
        TextView alcoholContent = (TextView) mCurrentView.findViewById(R.id.txt_alcohol_content);
        TextView breweryYear = (TextView) mCurrentView.findViewById(R.id.txt_brewing_year);
        TextView storageType = (TextView) mCurrentView.findViewById(R.id.txt_storage_type);
        TextView pressSqueez = (TextView) mCurrentView.findViewById(R.id.txt_press_squeeze);

        ProductDetailHorizontalItem album = this.mAlbumlist.get(position);

        if (!album.isTypeAdd()) {
            if (album.getCountry() != null) country.setText(album.getCountry());
            if (album.getRegion() != null) region.setText(album.getRegion());
            if (album.getSmv() != null) smv.setText(album.getSmv());
            if (album.getPolishRate() != null) polishRate.setText(album.getPolishRate());
            if (album.getAlcoholContent() != null) alcoholContent.setText(album.getAlcoholContent());
            if (album.getBrewingYear() != null) breweryYear.setText(album.getBrewingYear());
            if (album.getStorageType() != null) storageType.setText(album.getStorageType());
            if (album.getPressSqueez() != null) pressSqueez.setText(album.getPressSqueez());
        }

        mCurrentView.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
        container.addView(mCurrentView);

        return mCurrentView;
    }

    @Override
    public int getCount() {
        return mAlbumlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void removePosition(int position) {
        mAlbumlist.remove(mAlbumlist.get(position));
        notifyDataSetChanged();
    }

    public void updateData(List<ProductDetailHorizontalItem> dashBoardHorizontalItems, int flag) {
        if (flag == 0) { //append
            for (int i = 0; i < dashBoardHorizontalItems.size(); i++) {
                mAlbumlist.add(dashBoardHorizontalItems.get(i));
                //notifyItemInserted(getItemCount());
            }
            notifyDataSetChanged();
        } else { //clear all
            mAlbumlist.clear();
            notifyDataSetChanged();
        }
    }
}
