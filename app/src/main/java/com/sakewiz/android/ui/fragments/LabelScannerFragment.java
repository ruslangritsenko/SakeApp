package com.sakewiz.android.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakewiz.android.R;

import butterknife.ButterKnife;


public class LabelScannerFragment extends Fragment {


    public LabelScannerFragment() {
        // Required empty public constructor
    }


    public static LabelScannerFragment newInstance(String param1, String param2) {
        LabelScannerFragment fragment = new LabelScannerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_label_scanner, container, false);
        ButterKnife.bind(this, rootView);
        return  rootView;
    }
}
