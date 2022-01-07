package com.example.assestmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SearchForm extends AppCompatActivity {
    RecyclerView recyclerView;
    RadioGroup radioGroup;
    Button StartBtn, Retry, New_Btn;
    Adapter_list adapter_list;
    List<Data_Model_Search> ListSearch;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_form);
        //Binding Components
        recyclerView = findViewById(R.id.recyclerView_Accession);
        radioGroup = findViewById(R.id.RadioButtonGroup);
        StartBtn = findViewById(R.id.Reading_btn);
        Retry = findViewById(R.id.Retry);
        New_Btn = findViewById(R.id.New_Button);
        radioGroup.clearCheck();
        coordinatorLayout = findViewById(R.id.coordinator);

        New_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton MaterialId = (RadioButton) group.findViewById(R.id.MaterialID);
                RadioButton MaterialCode = (RadioButton) group.findViewById(R.id.MaterialCode);

                if (checkedId == R.id.MaterialCode) {
                    Toast.makeText(SearchForm.this, MaterialCode.getText(), Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.MaterialID) {
                    Toast.makeText(SearchForm.this, MaterialId.getText(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        //Left Swipe Delete
        enableSwipeToDeleteAndUndo();
    //List Intiailized
        ListSearch = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ListSearch.add(new Data_Model_Search("Tools ", String.valueOf(i)));
        }
        adapter_list = new Adapter_list(ListSearch, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter_list);

    }

    private void enableSwipeToDeleteAndUndo() {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Data_Model_Search item = adapter_list.getData().get(position);
                adapter_list.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> {

                    adapter_list.restoreItem(item, position);
                    recyclerView.scrollToPosition(position);
                });
//
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
    //Method for Clear Data from Components
    public void Clear() {
        ListSearch.clear();
        adapter_list.notifyDataSetChanged();

    }
}