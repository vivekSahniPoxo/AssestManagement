package com.example.assestmanagement;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;

import java.util.ArrayList;
import java.util.List;

public class InventoryForm extends AppCompatActivity {
    Button Back, NewBtn, StartReading;
    RadioGroup radioGroup;
    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;
    Adapter_Inventory adapter_inventory;
    List<DataModel_Inventory> ListInventory;
    IUHFService iuhfService;
    TextView Total, Found, NotFound;
    LooperDemo looperDemo;
    String result;
    EditText SearchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_form);
        recyclerView = findViewById(R.id.recyclerView);
        coordinatorLayout = findViewById(R.id.coordinator);
        Back = findViewById(R.id.Back_Button);
        SearchKey = findViewById(R.id.SearchKey);
        NewBtn = findViewById(R.id.New_Button);
        StartReading = findViewById(R.id.Reading_btn);
        radioGroup = findViewById(R.id.RadioButtonGroup);
        Total = findViewById(R.id.Total);
        Found = findViewById(R.id.Found);
        NotFound = findViewById(R.id.not_found);
        radioGroup.clearCheck();
        iuhfService = UHFManager.getUHFService(this);
        iuhfService.setAntennaPower(20);
        looperDemo = new LooperDemo();
        Handler handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };
        //ListenerButton
        NewBtn.setOnClickListener(v -> Clear());
        Back.setOnClickListener(v -> startActivity(new Intent(InventoryForm.this, MainActivity.class)));
        StartReading.setOnClickListener(v -> {
            Button b = (Button) v;
            String buttonText = b.getText().toString();
            if (buttonText.matches("Start")) {
                iuhfService.openDev();
                iuhfService.selectCard(1, "", false);
                iuhfService.inventoryStart();
                StartReading.setText("STOP");
                iuhfService.setOnInventoryListener(var1 -> {

                    runOnUiThread(() -> Toast.makeText(InventoryForm.this, result, Toast.LENGTH_SHORT).show());
                    result = var1.getEpc();
                    looperDemo.execute(() -> {
                        Message message = Message.obtain();
                        message.obj = result;
                        handler.sendMessage(message);
                    });
//                    if (!tempList.contains(result))
//                    {
//                    tempList.add(result);}
//                    Log.d("UHFService", "Callback");
                });

            } else {
                iuhfService.inventoryStop();
                iuhfService.closeDev();
                StartReading.setText("Start");
            }

        });

        //Left Swipe Delete Function
        enableSwipeToDeleteAndUndo();
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton Location = (RadioButton) group.findViewById(R.id.Location);
            RadioButton MaterialCode = (RadioButton) group.findViewById(R.id.MaterialCode);

            if (checkedId == R.id.MaterialCode) {
                Toast.makeText(InventoryForm.this, MaterialCode.getText(), Toast.LENGTH_SHORT).show();
                SearchKey.setHint("Enter Material Code");
            } else if (checkedId == R.id.Location) {
                Toast.makeText(InventoryForm.this, Location.getText(), Toast.LENGTH_SHORT).show();
                SearchKey.setHint("Enter Location");

            }
        });


        ListInventory = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ListInventory.add(new DataModel_Inventory("Tools ", String.valueOf(i)));
        }
        adapter_inventory = new Adapter_Inventory(ListInventory, this);
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