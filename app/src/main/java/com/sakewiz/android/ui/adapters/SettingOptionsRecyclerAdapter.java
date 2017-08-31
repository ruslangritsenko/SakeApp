package com.sakewiz.android.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.dto.SettingsItem;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.ui.fragments.SettingsFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingOptionsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SettingsItem> itemList;
    private Context mContext;
    private SettingsFragment mSettingsFragment;

    private final int VIEW_ITEM_HEADER = 0;
    private final int VIEW_ITEM_NAME = 1;
    private final int VIEW_RATE_REMINDER = 2;

    public SettingOptionsRecyclerAdapter(Context context, List<SettingsItem> itemList, SettingsFragment settingsFragment) {
        this.itemList = itemList;
        this.mContext = context;
        this.mSettingsFragment = settingsFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_ITEM_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_setting_header_view, null);
            HeaderListRowHolder mh = new HeaderListRowHolder(v);
            return mh;
        } else if (viewType == VIEW_ITEM_NAME) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_setting_item_view, null);
            ItemListRowHolder mh = new ItemListRowHolder(v);
            return mh;
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_setting_rate_reminder_view, null);
            RateReminderListRowHolder mh = new RateReminderListRowHolder(v);
            return mh;
        }
    }

    public void updateData(List<SettingsItem> messageListArray, int flag) {
        if (flag == 0) { //append
            itemList.clear();
            for (int i = 0; i < messageListArray.size(); i++) {
                itemList.add(messageListArray.get(i));
            }
            notifyDataSetChanged();
        } else { //clear all
            itemList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final SettingsItem child = itemList.get(position);
        if (holder instanceof HeaderListRowHolder) {

            if (child != null) {

                if (!TextUtils.isEmpty(child.getItemName())) {
                    ((HeaderListRowHolder) holder).itemHeader.setText(child.getItemName().toUpperCase());
                }
            }

        } else if (holder instanceof ItemListRowHolder) {
            if (child != null) {
                if (!TextUtils.isEmpty(child.getItemName())) {
                    ((ItemListRowHolder) holder).itemName.setText(child.getItemName());
                }
                if (child.isSetDivider()) ((ItemListRowHolder) holder).separator.setVisibility(View.VISIBLE);
            }

            /*((ItemListRowHolder) holder).container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (child.getItemName()) {
                        case "View Profile":
                            callFragment(new ProfileFragment(), ProfileFragment.getTAG());
                            break;
                        case "Change Password":
                            callFragment(new ChangePasswordFragment(), ChangePasswordFragment.getTAG());
                            break;
                        case "Family Access":
                            callFragment(new FamilyAccessFragment(), FamilyAccessFragment.getTAG());
                            break;
                        case "NotificationItem Center":
                            callFragment(new NotificationCenterFragment(), NotificationCenterFragment.getTAG());
                            break;
                        case "My Campus":
                            if (mSettingsFragment != null) mSettingsFragment.performGetCampusContacts();
                            break;
                        case "Contact OAC":
                            callFragment(new ContactFragment(), ContactFragment.getTAG());
                            break;
                        default: break;
                    }
                }
            });*/

        } else {
            if (child != null) {
                if (!TextUtils.isEmpty(child.getItemName())) ((RateReminderListRowHolder) holder).itemName.setText(child.getItemName());
                if (child.isSetDivider()) ((RateReminderListRowHolder) holder).separator.setVisibility(View.VISIBLE);


                /*((RateReminderListRowHolder) holder).logoutContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogInterface.OnClickListener logOutOnClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (CommonUtils.getInstance().isNetworkConnected()) {
                                    ((MainActivity)mContext).userLogOut();
                                } else {
                                    showToast(ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
                                }
                            }
                        };

                        showAlertDialog(false, ApplicationConstants.CONFIRM_LOGOUT, mContext.getString(R.string.logout_message),
                                mContext.getString(R.string.logout), mContext.getString(R.string.cancel), logOutOnClickListener, null);
                    }
                });*/
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    public SettingsItem getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(itemList.get(position) != null) {
            if(itemList.get(position).getItemCategory().equals("header")) return VIEW_ITEM_HEADER;
            else if(itemList.get(position).getItemCategory().equals("item")) return VIEW_ITEM_NAME;
            else return VIEW_RATE_REMINDER;
        };
        return -1;
    }

    public class HeaderListRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_item_title) TextView itemHeader;

        public HeaderListRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ItemListRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.main_container) RelativeLayout container;
        @Bind(R.id.txt_item_name) TextView itemName;
        @Bind(R.id.item_separator) View separator;

        public ItemListRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class RateReminderListRowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.spinner_container) RelativeLayout spinnerContainer;
        @Bind(R.id.txt_item_name) TextView itemName;
        @Bind(R.id.item_separator) View separator;
        @Bind(R.id.sp_rate_reminder) Spinner rateReminderSpinner;


        public RateReminderListRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            setNoOfEmployeesSpinner();
        }

        private void setNoOfEmployeesSpinner() {
            ArrayAdapter<String> rateReminderDataAdapter = new ArrayAdapter<String>(mContext,
                    R.layout.spinner_item, mContext.getResources().getStringArray(R.array.rate_reminder_items));
            rateReminderDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rateReminderSpinner.setAdapter(rateReminderDataAdapter);

        }
    }


    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    protected void showAlertDialog(boolean setCancelable, String title, String message, String positiveBtnTxt, String negativeBtnTxt,
                                   DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setCancelable(setCancelable);

        String titleTxt = (title != null) ? title : "Warning";
        String messageTxt = (message != null) ? message : "";
        alertDialog.setTitle(titleTxt);
        alertDialog.setMessage(messageTxt);

        if (positiveBtnTxt != null) {
            DialogInterface.OnClickListener positiveClickListener =
                    (positiveListener != null) ? positiveListener : defaultDialogClickListener();
            alertDialog.setPositiveButton(positiveBtnTxt, positiveClickListener);
        }

        if (negativeBtnTxt != null) {
            DialogInterface.OnClickListener negativeClickListener =
                    (negativeListener != null) ? negativeListener : defaultDialogClickListener();
            alertDialog.setNeutralButton(negativeBtnTxt, negativeClickListener);
        }

        if (!((MainActivity)mContext).isFinishing()) alertDialog.show();
    }

    protected DialogInterface.OnClickListener defaultDialogClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        };
    }

//    private void callFragment(Fragment fragment, String tag) {
//        if (mSettingsFragment != null) mSettingsFragment.callFragment(fragment, tag, true, false);
//    }
}
