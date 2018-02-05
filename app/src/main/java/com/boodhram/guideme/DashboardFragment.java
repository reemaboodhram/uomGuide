package com.boodhram.guideme;


import android.annotation.SuppressLint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jessnah on 2/2/2018.
 */


public class DashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.layout_dashboard, container,false);


        return view;
    }
}
