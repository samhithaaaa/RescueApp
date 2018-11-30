package com.example.android.rescueandroidapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment
{
private View groupFragmentView;
private ListView listView;
private ArrayAdapter<String> arrayAdapter;
private ArrayList<String> grouplist= new ArrayList<>();
private DatabaseReference databaseReference;



    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupFragmentView= inflater.inflate(R.layout.fragment_group, container, false);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("UserGroup");


        initializefields();
        RetrieveandDisplaygroups();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String groupname=parent.getItemAtPosition(position).toString();
                Intent i= new Intent(getContext(),CommunityActivity.class);
                i.putExtra("Groupname",groupname);
                //i.putExtra("lat",lat);
                startActivity(i);
            }
        });

        return groupFragmentView;
    }

    private void RetrieveandDisplaygroups() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String>set=new HashSet<>();
                Iterator i =dataSnapshot.getChildren().iterator();

                while (i.hasNext())
                {
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                grouplist.clear();
                grouplist.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initializefields() {
        listView =(ListView)groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,grouplist);
        listView.setAdapter(arrayAdapter);

    }

}
