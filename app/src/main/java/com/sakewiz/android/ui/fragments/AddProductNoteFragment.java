package com.sakewiz.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.sakewiz.android.R;
import com.sakewiz.android.ui.activities.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilshan_e on 6/24/17.
 */
public class AddProductNoteFragment extends BaseFragment {
    final String TAG = AddProductNoteFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "SearchBreweriesFragment";
    }

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    public static AddProductNoteFragment newInstance() {
        AddProductNoteFragment fragment = new AddProductNoteFragment();
        Bundle args = new Bundle();
//        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap());
        fragment.setArguments(args);
        return fragment;
    }


    private SearchView searchView;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mItem = Parcels.unwrap(getArguments().getParcelable(BUNDLE_EXTRA));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_add_product_note, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
        } catch (Exception ex) {
            Log.e(TAG, "onCreateView: " + ex.toString());
        }

        return rootView;
    }

    @Override
    protected void setUpUI() {
    }

    @Override
    public void initializePresenter() {
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_main, null);
        searchView = ((SearchView) mCustomView.findViewById(R.id.searchView));
        mTitle = (TextView) mCustomView.findViewById(R.id.title);
        TextView mNotificationCount = (TextView) mCustomView.findViewById(R.id.text_notification_count);
        mToolBar.addView(mCustomView);

        mCustomView.findViewById(R.id.imgVback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).toggleNavigationDrawer();
            }
        });
        mTitle.setText(R.string.my_sake_notes);
        mTitle.setAllCaps(true);
        mNotificationCount.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setUpSearchBar(searchView, null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btn_details)
    public void onClickDetails(View view) {
        getFragmentManager().popBackStack();
    }


}
