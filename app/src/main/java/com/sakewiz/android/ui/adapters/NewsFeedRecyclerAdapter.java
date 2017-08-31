package com.sakewiz.android.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sakewiz.android.R;
import com.sakewiz.android.dto.NotificationItem;
import com.sakewiz.android.ui.fragments.NewsFeedFragment;
import com.sakewiz.android.ui.utils.TimeUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsFeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotificationItem> itemList;
    private Context mContext;
    private NewsFeedFragment mNewsFeedFragment;

    private final int POINTS_NOTIFICATION = 0;
    private final int LIKE_NOTIFICATION = 1;
    private final int FOLLOWER_NOTIFICATION = 2;
    private final int MANUAL_MATCH_NOTIFICATION = 3;

    public NewsFeedRecyclerAdapter(Context context, List<NotificationItem> itemList, NewsFeedFragment newsFeedFragment) {
        this.itemList = itemList;
        this.mContext = context;
        this.mNewsFeedFragment = newsFeedFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == POINTS_NOTIFICATION) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_notification_points_view, null);
            PointsNotificationListRowHolder mh = new PointsNotificationListRowHolder(v);
            return mh;
        } else if (viewType == LIKE_NOTIFICATION) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_notification_like_view, null);
            LikeNotificationListRowHolder mh = new LikeNotificationListRowHolder(v);
            return mh;
        } else if (viewType == FOLLOWER_NOTIFICATION) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_notification_followers_view, null);
            FollowerNotificationListRowHolder mh = new FollowerNotificationListRowHolder(v);
            return mh;
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_notification_manual_match_view, null);
            ManualMatchNotificationListRowHolder mh = new ManualMatchNotificationListRowHolder(v);
            return mh;
        }
    }

    public void updateData(List<NotificationItem> notificationListArray, int flag) {
        if (flag == 0) { //append
            itemList.clear();
            for (int i = 0; i < notificationListArray.size(); i++) {
                itemList.add(notificationListArray.get(i));
            }
            notifyDataSetChanged();
        } else { //clear all
            itemList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final NotificationItem child = itemList.get(position);

        if (holder instanceof PointsNotificationListRowHolder) {
            if (child != null) {

                if (!TextUtils.isEmpty(child.getProductName().getEn())) ((PointsNotificationListRowHolder) holder).itemDescription.setText(child.getProductName().getEn());

//                String reviewedProductName = "";
//                String reviewedUserName = "";
//
//                if (!TextUtils.isEmpty(child.getProductName().getEn()))
//                    reviewedProductName = child.getProductName().getEn();
//                else if (!TextUtils.isEmpty(child.getBarName().getEn()))
//                    reviewedProductName = child.getBarName().getEn();
//                else if (!TextUtils.isEmpty(child.getBreweryName().getEn()))
//                    reviewedProductName = child.getBreweryName().getEn();
//
//                if (!TextUtils.isEmpty(child.getLikedUserHandle()))
//                    likedUserName = child.getLikedUserHandle();
//
//                if (!TextUtils.isEmpty(child.getCreated()))
//                    ((LikeNotificationListRowHolder) holder).itemTime.setText(String.valueOf(TimeUtils.getRelativeTime(Long.parseLong(child.getCreated()))));
//
//                if(!likedUserName.equals("") && !likedProductName.equals(""))
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                        ((LikeNotificationListRowHolder) holder).itemDescription.setText(Html.fromHtml(getColoredSpanned(likedUserName, "#69a574") + " "
//                                + mContext.getString(R.string.like_comment_string) + " " + getColoredSpanned(likedProductName, "#69a574"), Html.FROM_HTML_MODE_LEGACY));
//                    } else {
//                        ((LikeNotificationListRowHolder) holder).itemDescription.setText(Html.fromHtml(getColoredSpanned(likedUserName, "#69a574") + " "
//                                + mContext.getString(R.string.like_comment_string) + " " + getColoredSpanned(likedProductName, "#69a574")));
//                    }
            }

        } else if (holder instanceof LikeNotificationListRowHolder) {
            if (child != null) {
                String likedProductName = "";
                String likedUserName = "";

                if (!TextUtils.isEmpty(child.getLikedProductName().getEn()))
                    likedProductName = child.getLikedProductName().getEn();
                else if (!TextUtils.isEmpty(child.getLikedBarName().getEn()))
                    likedProductName = child.getLikedBarName().getEn();
                else if (!TextUtils.isEmpty(child.getLikedBreweryName().getEn()))
                    likedProductName = child.getLikedBreweryName().getEn();

                if (!TextUtils.isEmpty(child.getLikedUserHandle()))
                    likedUserName = child.getLikedUserHandle();

                if (!TextUtils.isEmpty(child.getCreated()))
                    ((LikeNotificationListRowHolder) holder).itemTime.setText(String.valueOf(TimeUtils.getRelativeTime(Long.parseLong(child.getCreated()))));

                if(!likedUserName.equals("") && !likedProductName.equals(""))
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        ((LikeNotificationListRowHolder) holder).itemDescription.setText(Html.fromHtml(getColoredSpanned(likedUserName, "#69a574") + " "
                                + mContext.getString(R.string.like_comment_string) + " " + getColoredSpanned(likedProductName, "#69a574"), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        ((LikeNotificationListRowHolder) holder).itemDescription.setText(Html.fromHtml(getColoredSpanned(likedUserName, "#69a574") + " "
                                + mContext.getString(R.string.like_comment_string) + " " + getColoredSpanned(likedProductName, "#69a574")));
                    }
            }

        } else if (holder instanceof FollowerNotificationListRowHolder) {
            if (child != null) {
                String followerName = "";

                if (!TextUtils.isEmpty(child.getLikedUserHandle()))
                    followerName = child.getFollowerUserName();

                if (!TextUtils.isEmpty(child.getCreated()))
                    ((LikeNotificationListRowHolder) holder).itemTime.setText(String.valueOf(TimeUtils.getRelativeTime(Long.parseLong(child.getCreated()))));

                if(!followerName.equals(""))
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        ((LikeNotificationListRowHolder) holder).itemDescription.setText(Html.fromHtml(getColoredSpanned(followerName, "#69a574") + " "
                                + mContext.getString(R.string.started_following_you_string), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        ((LikeNotificationListRowHolder) holder).itemDescription.setText(Html.fromHtml(getColoredSpanned(followerName, "#69a574") + " "
                                + mContext.getString(R.string.started_following_you_string)));
                    }


            }
        } else if (holder instanceof ManualMatchNotificationListRowHolder) {
            if (child != null) {
                if (!TextUtils.isEmpty(child.getMatchedProductName().getEn())) ((ManualMatchNotificationListRowHolder) holder).itemName.setText(child.getMatchedProductName().getEn());


            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    public NotificationItem getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(itemList.get(position) != null) {
            if(itemList.get(position).getType().equals("POINTS_NOTIFICATION"))
                return POINTS_NOTIFICATION;
            else if(itemList.get(position).getType().equals("LIKE_NOTIFICATION"))
                return LIKE_NOTIFICATION;
            else if(itemList.get(position).getType().equals("FOLLOWER_NOTIFICATION"))
                return FOLLOWER_NOTIFICATION;
            else if(itemList.get(position).getType().equals("MANUAL_MATCH_NOTIFICATION"))
                return MANUAL_MATCH_NOTIFICATION;
        };
        return -1;
    }

    public class PointsNotificationListRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_description) TextView itemDescription;
        @Bind(R.id.txt_time) TextView itemTime;

        public PointsNotificationListRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class LikeNotificationListRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_description) TextView itemDescription;
        @Bind(R.id.txt_time) TextView itemTime;

        public LikeNotificationListRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class FollowerNotificationListRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_description) TextView itemDescription;
        @Bind(R.id.txt_time) TextView itemTime;

        public FollowerNotificationListRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ManualMatchNotificationListRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_item_title) TextView itemName;

        public ManualMatchNotificationListRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

//    protected void showAlertDialog(boolean setCancelable, String title, String message, String positiveBtnTxt, String negativeBtnTxt,
//                                   DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
//        alertDialog.setCancelable(setCancelable);
//
//        String titleTxt = (title != null) ? title : "Warning";
//        String messageTxt = (message != null) ? message : "";
//        alertDialog.setTitle(titleTxt);
//        alertDialog.setMessage(messageTxt);
//
//        if (positiveBtnTxt != null) {
//            DialogInterface.OnClickListener positiveClickListener =
//                    (positiveListener != null) ? positiveListener : defaultDialogClickListener();
//            alertDialog.setPositiveButton(positiveBtnTxt, positiveClickListener);
//        }
//
//        if (negativeBtnTxt != null) {
//            DialogInterface.OnClickListener negativeClickListener =
//                    (negativeListener != null) ? negativeListener : defaultDialogClickListener();
//            alertDialog.setNeutralButton(negativeBtnTxt, negativeClickListener);
//        }
//
//        if (!((MainActivity)mContext).isFinishing()) alertDialog.show();
//    }

    protected DialogInterface.OnClickListener defaultDialogClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        };
    }

//    private void callFragment(Fragment fragment, String tag) {
//        if (mNewsFeedFragment != null) mNewsFeedFragment.callFragment(fragment, tag, true, false);
//    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}
