package com.friendngo.friendngo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityListFragment extends Fragment {
    public ListView listView;
    private static ActivityListAdapter adapter;
    private static final int POLLING_PERIOD = 5;
    private String checkOnlineToast = null;
    public ActivityListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_list, container, false);
        adapter = new ActivityListAdapter(getApplicationContext());
        initXmlView(view);
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        if(ValidationClass.checkOnline(getApplicationContext())) {
            if (listView == null) {
                Log.w("LIST VIEW ERROR", "List view is null!");
            } else {

                listView.setAdapter(adapter);
            }
        }else {
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();
        }
        return view;
    }
    public void initXmlView(View view){
        listView = (ListView) view.findViewById(R.id.activity_list);
    }
}
