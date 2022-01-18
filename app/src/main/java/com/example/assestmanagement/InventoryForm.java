package com.example.assestmanagement;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class InventoryForm extends AppCompatActivity {
    Button Back, NewBtn, StartReading, Searchbtn;
    RadioGroup radioGroup;
    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;
    Adapter_Inventory adapter_inventory;
    ProgressDialog dialog;
    List<DataModel_Inventory> ListInventory;
    IUHFService iuhfService;
    String result;
    TextView Total, Found, NotFound;
    LooperDemo looperDemo;
    AutoCompleteTextView SearchKey;
    String Parameter;
    List<String> suggest;
    int counter = 0, len, not_founded;
    List<String> TempList_Inventory;

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
        Searchbtn = findViewById(R.id.Search);
        radioGroup.clearCheck();
        dialog = new ProgressDialog(this);
        TempList_Inventory = new ArrayList<>();
        iuhfService = UHFManager.getUHFService(this);
//        iuhfService.setAntennaPower(20);
        looperDemo = new LooperDemo();
        Handler handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int fnd = adapter_inventory.getFilter(msg.obj);
//                counter = counter+fnd;
//                iuhfService.inventoryStop();
//                iuhfService.closeDev();
                not_founded = len - fnd;
                Total.setText(String.valueOf(len));
                Found.setText(String.valueOf(fnd));
                NotFound.setText(String.valueOf(not_founded));
            }
        };
        //SuggestionList
        SuggestList();
        suggest = new ArrayList<>();

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

//                    runOnUiThread(() -> Toast.makeText(InventoryForm.this, result, Toast.LENGTH_SHORT).show());
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

//        Left Swipe Delete Function
        enableSwipeToDeleteAndUndo();
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton Location = (RadioButton) group.findViewById(R.id.Location);
            RadioButton MaterialCode = (RadioButton) group.findViewById(R.id.MaterialCode);

            if (checkedId == R.id.MaterialCode) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (this, android.R.layout.simple_list_item_1, suggest);
                SearchKey.setAdapter(adapter);
                Parameter = MaterialCode.getText().toString();
                SearchKey.setEnabled(true);
//                Toast.makeText(InventoryForm.this, MaterialCode.getText(), Toast.LENGTH_SHORT).show();
                SearchKey.setHint("Enter Material Code");
            } else if (checkedId == R.id.Location) {

                SearchKey.setHint("Enter Location");
                SearchKey.setEnabled(true);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (this, android.R.layout.simple_list_item_1, suggest);
                SearchKey.setAdapter(adapter);
                Parameter = Location.getText().toString();
            }
        });


        ListInventory = new ArrayList<>();

        Searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = SearchKey.getText().toString();

                try {
                    if (value.length() == 0) {
                        SearchKey.setError("Enter Value..");
                    } else {
                        FetchData(Parameter, value);
                        dialog.show();
                        dialog.setMessage(getString(R.string.Dialog_Text));
                        dialog.setCancelable(false);
                        SearchKey.setText("");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void SuggestList() {
        String url = "http://164.52.223.163:4501/api/storematerial/distinctlocation";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        System.out.println("Location " + array.getString(i));
                        suggest.add(array.getString(i));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //adding the request to volley
        Volley.newRequestQueue(this).add(request);

    }

    private void FetchData(String parameter, String value) throws JSONException {

        String url = "http://164.52.223.163:4501/api/storematerial/storeinventory";
        JSONObject obj = new JSONObject();
//        obj.put("AccessNo", "B1228");
        obj.put(parameter, value);
//        obj.put("Material Code","8411630145" );
        RequestQueue queue = Volley.newRequestQueue(this);


        final String requestBody = obj.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            ListInventory.clear();

            try {
                JSONArray array = new JSONArray(response);
                len = array.length();
                Total.setText(String.valueOf(len));
                NotFound.setText(String.valueOf(len));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String Material_Name = object.getString("Material_Name");
                    String Material_Model = object.getString("Material_ID");
                    String Material_Department = object.getString("Material_Department");
                    String Location = object.getString("Location");
                    String TagId = object.getString("TagID");

                    ListInventory.add(new DataModel_Inventory(Material_Name, Material_Model, Location, Material_Department, TagId));
//
                    TempList_Inventory.add(TagId);
                }
                StartReading.setEnabled(true);
                recyclerView.setHasFixedSize(true);
                adapter_inventory = new Adapter_Inventory(ListInventory, getApplicationContext(), TempList_Inventory);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter_inventory);
                dialog.dismiss();


//                    Toast.makeText(Inventory_form.this, name, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("VOLLEY", response);
            dialog.dismiss();
        }, error -> {
            Log.e("VOLLEY Negative", error.toString());
            dialog.dismiss();
            if (error.networkResponse.statusCode == 404) {
                Toast.makeText(InventoryForm.this, "No Result Found", Toast.LENGTH_SHORT).show();
            } else if (error.networkResponse.statusCode == 400) {
                Toast.makeText(InventoryForm.this, "Bad Request", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(InventoryForm.this, "Unable to process the request", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        queue.add(stringRequest);
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