package com.sakewiz.android.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.sakewiz.android.R;
import com.sakewiz.android.dto.FilterSakeTypeItem;
import com.sakewiz.android.ui.adapters.ExpListViewAdapterWithCheckbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilshan_e on 6/14/17.
 */
public class FragmentSakeType extends BaseFragment {

    final String TAG = FragmentSakeType.this.getClass().getSimpleName();

    public static String getTAG() {
        return "FragmentSakeType";
    }

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";
    private static int REP_DELAY = 50;
    private static String CODES = "codes";
    public static FragmentSakeType fragmentSakeType;
    private ExpListViewAdapterWithCheckbox adapter;

    public static FragmentSakeType newInstance(String fragmentFrom) {
        FragmentSakeType fragment = new FragmentSakeType();
        Bundle args = new Bundle();
        args.putString(BUNDLE_EXTRA, fragmentFrom);
        fragment.setArguments(args);
        return fragment;
    }

    private Fragment mFragment;
    private Activity mActivity;
    private String mFragmentFrom;

    @Bind(R.id.simple_expandable_listview) ExpandableListView expandableListView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             mFragmentFrom = getArguments().getString(BUNDLE_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            // Fragment screen orientation normal both portait and landscape
            rootView = inflater.inflate(R.layout.fragment_sake_type, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            this.fragmentSakeType = this;
        } catch (Exception ex) {
            Log.e(TAG, "onCreateView: " + ex.toString());
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    protected void setUpUI() {
        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);
        setItems();
        setListener();
    }

    @Override
    public void initializePresenter() {
    }

    @Override
    protected void setUpToolBar() {}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentSakeType = null;
    }

    // Setting headers and childs to expandable listview
    void setItems() {

        // Array list for header
        ArrayList<String> header = new ArrayList<String>();

        // Hash map for both header and child
        HashMap<String, List<FilterSakeTypeItem>> hashMap = new HashMap<String, List<FilterSakeTypeItem>>();

        // Adding headers to list
        String [] headers = getResources().getStringArray(R.array.sake_filter_headers);
        for (int i = 0; i < headers.length; i++) {
            header.add(headers[i]);
        }

        // Adding header and childs to hash map
        hashMap.put(header.get(0), addDtaToChildList(getResources().getStringArray(R.array.sake_type)));
        hashMap.put(header.get(1), addDtaToChildList(getResources().getStringArray(R.array.sake_filterWaters)));
        hashMap.put(header.get(2), addDtaToChildList(getResources().getStringArray(R.array.sake_yeasts)));
        hashMap.put(header.get(3), addDtaToChildList(getResources().getStringArray(R.array.sake_press_and_squeezes)));

        adapter = new ExpListViewAdapterWithCheckbox(getActivity(), header, hashMap);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(adapter);
    }

    public List<FilterSakeTypeItem> addDtaToChildList(String [] strings){
        List<FilterSakeTypeItem> child = new ArrayList<FilterSakeTypeItem>();
        for (int i = 0; i < strings.length; i++) {
            FilterSakeTypeItem a = new FilterSakeTypeItem();
            a.setSakeType(strings[i]);
            child.add(a);
        }
        return child;
    }

    // Setting different listeners to expandablelistview
    void setListener() {

        // This listener will show toast on group click
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView listview, View view,
                                        int group_pos, long id) {
                return false;
            }
        });

        // This listener will expand one group at one time
        // You can remove this listener for expanding all groups
        expandableListView
                .setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    // Default position
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup)

                            // Collapse the expanded group
                            expandableListView.collapseGroup(previousGroup);
                        previousGroup = groupPosition;
                    }

                });

        // This listener will show toast on child click
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView listview, View view,
                                        int groupPos, int childPos, long id) {
                return false;
            }
        });
    }

    @OnClick(R.id.apply_btn_sake_type)
    public void onApply(View view){
        switch (mFragmentFrom){
            case  "SearchSakeFragment"      :
                if (SearchSakeFragment.searchSakeFragment != null) {
                    SearchSakeFragment.searchSakeFragment.setSakeTypeTag(getSlectedItems(0),
                            getSlectedItems(1), getSlectedItems(2), getSlectedItems(3));
                    SearchSakeFragment.searchSakeFragment.updateTagView();
                }
                break;
            case  "SearchPlacesFragment"    :
                if (SearchPlacesFragment.searchPlacesFragment != null) {
                    SearchPlacesFragment.searchPlacesFragment.setSakeTypeTag(getSlectedItems(0),
                            getSlectedItems(1), getSlectedItems(2), getSlectedItems(3));
                    SearchPlacesFragment.searchPlacesFragment.updateTagView();
                }
                break;
            case  "SearchBreweriesFragment" :
                break;
            default                         :
                break;

        }
       if (FilterMainFragment.mFilterMainFragment != null)
           FilterMainFragment.mFilterMainFragment.getFragmentManager().popBackStack();
    }

    public List<String> getSlectedItems(int groupId){
        List<String> list = new ArrayList<>();
            for (int i = 0; i < adapter.getChildrenCount(groupId); i++){
               FilterSakeTypeItem temp = ((FilterSakeTypeItem)adapter.getChild(groupId,i));
                if (temp.isSelected()) list.add(temp.getSakeType());
            }
        return list;
    }
}
