package com.example.eventorganising_demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
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
    TextView emptyTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        historyRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewHistory);
        MaterialToolbar toolbar = (MaterialToolbar) view.findViewById(R.id.toolbar);
        emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);

        emptyTextView.setText("Loading....");
        emptyTextView.setVisibility(View.VISIBLE);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                return true;
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        eventArrayList = new ArrayList<>();
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter(getContext(), eventArrayList);
        historyRecyclerView.setAdapter(adapter);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventArrayList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
//                    Event event =new Event(postSnapshot.child("name").getValue(),
//                            postSnapshot.child("date").getValue(), postSnapshot.child("time").getValue(),
//                            postSnapshot.child("reminder").getValue(Boolean.),
//                            postSnapshot.child("imageUrl").getValue())
                    Event event = postSnapshot.getValue(Event.class);
                    eventArrayList.add(event);
                }
                if (eventArrayList.size() > 0) {
                    emptyTextView.setVisibility(View.GONE);
                    adapter = new HistoryAdapter(getContext(), eventArrayList);
                    historyRecyclerView.setAdapter(adapter);
                } else {
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText("No Event Found....Press + button for creating new event");

                }

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