package com.sakewiz.android.ui.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.sakewiz.android.R;
import com.sakewiz.android.dto.FilterSakeTypeItem;

/**
 * Created by dilshan_e on 6/10/17.
 */
@SuppressLint("UseSparseArrays")
public class ExpListViewAdapterWithCheckbox extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;
    private HashMap<String, List<FilterSakeTypeItem>> mListDataChild;
    private ArrayList<String> mListDataGroup;
    private HashMap<Integer, boolean[]> mChildCheckStates;

    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;

    private String groupText;
    private String childText;

    public ExpListViewAdapterWithCheckbox(Context context,
                                          ArrayList<String> listDataGroup, HashMap<String, List<FilterSakeTypeItem>> listDataChild) {

        mContext = context;
        mListDataGroup = listDataGroup;
        mListDataChild = listDataChild;

        // Initialize our hashmap containing our check states here
        mChildCheckStates = new HashMap<Integer, boolean[]>();
    }

    @Override
    public int getGroupCount() {
        return mListDataGroup.size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return mListDataGroup.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        groupText = getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.filter_sake_type_header, null);

            groupViewHolder = new GroupViewHolder();

            groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.header);

            convertView.setTag(groupViewHolder);
        } else {

            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.mGroupText.setText(groupText);

        if (isExpanded) {
            groupViewHolder.mGroupText.setTypeface(null, Typeface.BOLD);
            groupViewHolder.mGroupText.setBackgroundColor(mContext.getResources().getColor(R.color.dark_text_color));
        } else {
            groupViewHolder.mGroupText.setTypeface(null, Typeface.NORMAL);
            groupViewHolder.mGroupText.setBackgroundColor(mContext.getResources().getColor(R.color.filter_header_bg_default));
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).size();
    }

    @Override
    public FilterSakeTypeItem getChild(int groupPosition, int childPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;

        childText = getChild(mGroupPosition, mChildPosition).getSakeType();

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.filter_sake_type_child, null);

            childViewHolder = new ChildViewHolder();

            childViewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.child_check_box);

            convertView.setTag(R.layout.filter_sake_type_child, childViewHolder);

        } else {

            childViewHolder = (ChildViewHolder) convertView
                    .getTag(R.layout.filter_sake_type_child);
        }

        childViewHolder.mCheckBox.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);

            childViewHolder.mCheckBox.setChecked(getChecked[mChildPosition]);
            childViewHolder.mCheckBox.setText(childText);

        } else {
            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];

            mChildCheckStates.put(mGroupPosition, getChecked);

            childViewHolder.mCheckBox.setChecked(false);
            childViewHolder.mCheckBox.setText(childText);
        }

        childViewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);

                    List<FilterSakeTypeItem> temp = mListDataChild.get(mListDataGroup.get(mGroupPosition));
                    temp.get(mChildPosition).setSelected(isChecked);
                    mListDataChild.put(mListDataGroup.get(mGroupPosition),temp);

                } else {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);

                    List<FilterSakeTypeItem> temp = mListDataChild.get(mListDataGroup.get(mGroupPosition));
                    temp.get(mChildPosition).setSelected(isChecked);
                    mListDataChild.put(mListDataGroup.get(mGroupPosition),temp);
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public final class GroupViewHolder {

        TextView mGroupText;
    }

    public final class ChildViewHolder {

        CheckBox mCheckBox;
    }
}
