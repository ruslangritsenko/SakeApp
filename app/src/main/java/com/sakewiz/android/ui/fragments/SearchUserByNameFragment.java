package com.sakewiz.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakewiz.android.R;

import butterknife.ButterKnife;

/**
 * Created by dilshan_e on 7/20/2017.
 */
public class SearchUserByNameFragment extends BaseFragment {

    final String TAG = SearchUserByNameFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "SavedSakesFragment";
    }

    public static SearchUserByNameFragment newInstance() {
        return new SearchUserByNameFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_search_user, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }
        return rootView;
    }

    @Override
    public void initializePresenter() {

    }

    @Override
    protected void setUpUI() {
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_with_back, null);
        TextView toolBarTitle = (TextView) mCustomView.findViewById(R.id.title_text);
        toolBarTitle.setText(getString(R.string.menu_item_search));
        mCustomView.findViewById(R.id.imgVmain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        mToolBar.addView(mCustomView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
