package com.boodhram.guideme;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boodhram.guideme.Chat.Chat;
import com.boodhram.guideme.Utils.ConnectivityHelper;
import com.boodhram.guideme.Utils.Utils;

/**
 * Created by Jessnah on 2/2/2018.
 */


public class DashboardFragment extends Fragment {

    LinearLayout linearUomWebsite,linearPostgraduate,linearUndergraduate,linearCalendar;
    LinearLayout linear_places,linear_meeting,linear_friends, linear_chat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.layout_dashboard, container,false);
        findviewById(view);
        return view;
    }

    private void findviewById(View view) {
        linearUomWebsite = view.findViewById(R.id.linearUomWebsite);
        linearPostgraduate = view.findViewById(R.id.linearPostgraduate);
        linearUndergraduate = view.findViewById(R.id.linearUndergraduate);
        linearCalendar = view.findViewById(R.id.linearCalendar);

        linear_places = view.findViewById(R.id.linear_places);
        linear_meeting = view.findViewById(R.id.linear_meeting);
        linear_friends = view.findViewById(R.id.linear_friends);
        linear_chat = view.findViewById(R.id.linearchat);

        linearUomWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("http://www.uom.ac.mu");
            }
        });

        linearPostgraduate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("http://www.uom.ac.mu/index.php/study-at-uom/programmes/postgraduate");

            }
        });

        linearUndergraduate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("http://www.uom.ac.mu/index.php/study-at-uom/programmes/undergraduate-programmes");
            }
        });

        linearCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("http://www.uom.ac.mu/index.php/study-at-uom/academic-calendar");
            }
        });

        linear_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showPopup(getString(R.string.title_place_of_interest),getString(R.string.detail_uom_places),getActivity(),new Intent(getActivity(),UomPlacesActivity.class));
            }
        });
        linear_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showPopup(getString(R.string.title_meeting_point),getString(R.string.detail_meeting_point),getActivity(), new Intent(getActivity(), MeetingPointActivity.class));
            }
        });

        linear_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showPopup(getString(R.string.title_spot_myfriends),getString(R.string.detail_spot_friends),getActivity(), new Intent(getActivity(), SpotFriendsActivity.class));
            }
        });
        linear_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showPopup(getString(R.string.title_chat),getString(R.string.detail_chat),getActivity(), new Intent(getActivity(), Chat.class));
            }
        });


    }

    private void openUrl(String url){
        if(ConnectivityHelper.isConnected(getActivity())){
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }else {
            Toast.makeText(getActivity(),"Not connected to the internet", Toast.LENGTH_SHORT).show();
        }

    }
}
