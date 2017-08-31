package com.sakewiz.android.ui.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sakewiz.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dilshan_e on 30/05/2017.
 */
public class NavigationRecyclerAdapter extends RecyclerView.Adapter<NavigationRecyclerAdapter.MenuListRowHolder> {

    private List<String> feedItemList;

    private Context mContext;

    public NavigationRecyclerAdapter(Context context, List<String> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    public void updateData(List<String> messageList, int flag) {
        if (flag == 0) { //append
            feedItemList = messageList;
            notifyDataSetChanged();
        } else { //clear all
            feedItemList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public MenuListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_list_item, null);
        MenuListRowHolder mh = new MenuListRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final MenuListRowHolder menuListRowHolder, final int position) {
        final String item = feedItemList.get(position);
        if(item != null) {
            menuListRowHolder.itemName.setText(item);
            menuListRowHolder.nContainer.setVisibility(View.GONE);
            switch (item) {
                case "Search Places":
                    menuListRowHolder.itemImage.setBackgroundResource(R.drawable.ic_search);
                    break;
                case "My Profile":
                    menuListRowHolder.itemImage.setBackgroundResource(R.drawable.ic_my_profile);
                    break;
                case "Followers":
                    menuListRowHolder.itemImage.setBackgroundResource(R.drawable.ic_followers);
                    break;
                case "Private Notes":
                    menuListRowHolder.itemImage.setBackgroundResource(R.drawable.ic_private_notes);
                    break;
                case "Saved Sakes":
                    menuListRowHolder.itemImage.setBackgroundResource(R.drawable.ic_saved_sakes);
                    break;
                case "Newsfeed":
                    menuListRowHolder.itemImage.setBackgroundResource(R.drawable.ic_newsfeed);
                    menuListRowHolder.nContainer.setVisibility(View.VISIBLE);
                    menuListRowHolder.nCount.setText("15");
                    break;
                case "My Purchases":
                    menuListRowHolder.itemImage.setBackgroundResource(R.drawable.ic_my_purchases);
                    break;
                case "Inventory Add":
                    menuListRowHolder.itemImage.setBackgroundResource(R.drawable.ic_inventory_add);
                    break;
                case "Settings":
                    menuListRowHolder.itemImage.setBackgroundResource(R.drawable.ic_settings);
                    break;
                case "Logout":
                    menuListRowHolder.itemImage.setBackgroundResource(R.drawable.ic_logout);
                    break;
            }
        }
    }

    public String getItem(int position) {
        return feedItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class MenuListRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.icon) ImageView itemImage;
        @Bind(R.id.title) TextView itemName;
        @Bind(R.id.nContainer) RelativeLayout nContainer;
        @Bind(R.id.txt_notification_count) TextView nCount;

        public MenuListRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}