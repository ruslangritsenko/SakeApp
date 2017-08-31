package com.sakewiz.android.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakewiz.android.R;
import com.sakewiz.android.dto.DashBoardHorizontalItem;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.List;

/**
 * Created by dilshan_e on 04/06/2017.
 */
public class EnchantedPagerAdapter extends PagerAdapter{

    Context mContext;

    LayoutInflater inflater;

    final List<DashBoardHorizontalItem> mAlbumlist;

    public EnchantedPagerAdapter(Context context,List<DashBoardHorizontalItem> albumList) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mAlbumlist = albumList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CardView mCurrentView = (CardView) inflater.inflate(R.layout.dashboard_horizontal_item, container, false);
        TextView sakeIdentified = (TextView) mCurrentView.findViewById(R.id.sake_identified);
        TextView sakeUnidentified = (TextView) mCurrentView.findViewById(R.id.sake_unidentified);
        DashBoardHorizontalItem album = this.mAlbumlist.get(position);
        if (!album.isTypeAdd()) {
            if(album.getSakeIdentified() != null)
                sakeIdentified.setText(album.getSakeIdentified());
            if(album.getSakeUnidentified() != null)
                sakeUnidentified.setText(album.getSakeUnidentified());
        }
//
//        mCurrentView.setImageBitmap(bitmap);
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

    public void updateData(List<DashBoardHorizontalItem> dashBoardHorizontalItems, int flag) {
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
