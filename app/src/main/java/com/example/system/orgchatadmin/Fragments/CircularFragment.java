package com.example.system.orgchatadmin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.system.orgchatadmin.Activities.NewCircular;
import com.example.system.orgchatadmin.R;


public class CircularFragment extends Fragment {

    public CircularFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_circular, container, false);

        Button new_circular = (Button)root.findViewById(R.id.new_circular);
        ListView circular_list = (ListView)root.findViewById(R.id.circular_list);

        new_circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),NewCircular.class));
            }
        });

        return root;

    }

}
