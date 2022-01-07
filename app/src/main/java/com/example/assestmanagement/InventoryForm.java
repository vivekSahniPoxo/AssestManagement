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
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;

import java.util.ArrayList;
import java.util.List;

public class InventoryForm extends AppCompatActivity {
    Button Back, NewBtn,StartReading;
    RadioGroup radioGroup;
    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;
    Adapter_Inventory adapter_inventory;
    List<DataModel_Inventory> ListInventory;
    IUHFService iuhfService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_form);
        recyclerView=findViewById(R.id.recyclerView);
        coordinatorLayout=findViewById(R.id.coordinator);
        Back=findViewById(R.id.Back_Button);
        NewBtn=findViewById(R.id.New_Button);
        StartReading=findViewById(R.id.Reading_btn);
        radioGroup=findViewById(R.id.RadioButtonGroup);
        radioGroup.clearCheck();
        iuhfService= UHFManager.getUHFService(this);

        //ListenerButton
        NewBtn.setOnClickListener(v -> Clear());

        //Left Swipe Delete Function
        enableSwipeToDeleteAndUndo();
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton Location = (RadioButton) group.findViewById(R.id.Location);
            RadioButton MaterialCode = (RadioButton) group.findViewById(R.id.MaterialCode);

            if (checkedId == R.id.MaterialCode) {
                Toast.makeText(InventoryForm.this, MaterialCode.getText(), Toast.LENGTH_SHORT).show();
            } else if (checkedId == R.id.Location) {
                Toast.makeText(InventoryForm.this, Location.getText(), Toast.LENGTH_SHORT).show();

            }
        });


        ListInventory=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ListInventory.add(new DataModel_Inventory("Tools ", String.valueOf(i)));
        }
        adapter_inventory=new Adapter_Inventory(ListInventory,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_inventory);


    }

    private void enableSwipeToDeleteAndUndo() {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final DataModel_Inventory item = adapter_inventory.getData().get(position);
                adapter_inventory.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> {

                    adapter_inventory.restoreItem(item, position);
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
    public void Clear() {
        ListInventory.clear();
        adapter_inventory.notifyDataSetChanged();

    }
}