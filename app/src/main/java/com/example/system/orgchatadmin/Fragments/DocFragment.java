package com.example.system.orgchatadmin.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.system.orgchatadmin.Adapters.DocListAdapter;
import com.example.system.orgchatadmin.R;

import java.util.ArrayList;

public class DocFragment extends Fragment {

    public DocFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_doc, container, false);

        ListView list = (ListView)root.findViewById(R.id.list);

        ArrayList<Integer> images = new ArrayList<Integer>();

        images.add(R.drawable.esi1);
        images.add(R.drawable.esi2);
        images.add(R.drawable.esi3);
        images.add(R.drawable.esi4);

        DocListAdapter adapter = new DocListAdapter(getContext(),images);

        list.setAdapter(adapter);

        return root;
    }

}
