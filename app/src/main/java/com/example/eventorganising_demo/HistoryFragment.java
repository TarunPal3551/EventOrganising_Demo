package com.example.eventorganising_demo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    RecyclerView historyRecyclerView;
    HistoryAdapter adapter;
    ArrayList<Event> eventArrayList = new ArrayList<>();

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String TAG = "HistoryFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        historyRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewHistory);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        eventArrayList = new ArrayList<>();
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter(getContext(), eventArrayList);
        historyRecyclerView.setAdapter(adapter);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
//                    Event event =new Event(postSnapshot.child("name").getValue(),
//                            postSnapshot.child("date").getValue(), postSnapshot.child("time").getValue(),
//                            postSnapshot.child("reminder").getValue(Boolean.),
//                            postSnapshot.child("imageUrl").getValue())
                    Event event = postSnapshot.getValue(Event.class);
                    eventArrayList.add(event);
                }
                adapter = new HistoryAdapter(getContext(), eventArrayList);
                historyRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("events").addValueEventListener(valueEventListener);


        return view;
    }

    public void getData() {

    }
}