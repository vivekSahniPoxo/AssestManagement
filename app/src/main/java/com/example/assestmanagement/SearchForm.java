package com.example.assestmanagement;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.HashMap;
import java.util.List;

public class SearchForm extends AppCompatActivity {
    RecyclerView recyclerView;
    RadioGroup radioGroup;
    Button StartBtn, Retry, New_Btn;
    Adapter_list adapter_list;
    List<Data_Model_Search> ListSearch;
    CoordinatorLayout coordinatorLayout;
    IUHFService iuhfService;
    String result;
    LooperDemo looperDemo;
    ProgressDialog dialog;
    AutoCompleteTextView SearchKey;
    Button SearchData;
    String paravalues;
    List<String> suggest;
    private int mStatusCode = 0;

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
        SearchKey = findViewById(R.id.SearchKey);
        SearchData = findViewById(R.id.Search_Data);
        radioGroup.clearCheck();
        dialog = new ProgressDialog(this);

        //SuggestionList
        SuggestList();
        suggest = new ArrayList<>();

        coordinatorLayout = findViewById(R.id.coordinator);
        iuhfService = UHFManager.getUHFService(this);
        looperDemo = new LooperDemo();
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton MaterialId = (RadioButton) group.findViewById(R.id.MaterialID);
            RadioButton MaterialCode = (RadioButton) group.findViewById(R.id.MaterialCode);

            if (checkedId == R.id.MaterialCode) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (this, android.R.layout.simple_list_item_1, suggest);
                SearchKey.setAdapter(adapter);
                String values = String.valueOf(MaterialCode.getText());
                paravalues = values;
                SearchKey.setEnabled(true);
//                Toast.makeText(SearchForm.this, MaterialCode.getText(), Toast.LENGTH_SHORT).show();
                SearchKey.setHint("Enter Material Code");
            } else if (checkedId == R.id.MaterialID) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (this, android.R.layout.simple_list_item_1, suggest);
                SearchKey.setAdapter(adapter);
                SearchKey.setEnabled(true);
                paravalues = (String) MaterialId.getText();
//                Toast.makeText(SearchForm.this, MaterialId.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        Handler handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                adapter_list.getFilter(msg.obj);
            }
        };

        StartBtn.setOnClickListener(v -> {
            Button b = (Button) v;
            String buttonText = b.getText().toString();
            if (buttonText.matches("Start")) {
                iuhfService.openDev();
                iuhfService.selectCard(1, "", false);
                iuhfService.inventoryStart();
                StartBtn.setText("STOP");
                iuhfService.setOnInventoryListener(var1 -> {

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
                StartBtn.setText("Start");
            }


        });


        New_Btn.setOnClickListener(v -> Clear());
        //Left Swipe Delete
        enableSwipeToDeleteAndUndo();
        //List Intiailized
        ListSearch = new ArrayList<>();
        SearchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String value = String.valueOf(SearchKey.getText());

                    if (value.length() == 0) {
                        SearchKey.setError("Please Enter Value...");
                    } else {
                        dialog.show();
                        dialog.setMessage(getString(R.string.Dialog_Text));
                        dialog.setCancelable(false);
                        FetchData(paravalues, value);
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

    //Method for Search Data From Server using Accession Number
    private void FetchData(String parameter, String value) throws JSONException {

        String url = "http://164.52.223.163:4501/api/storematerial/searchmaterial";
        JSONObject obj = new JSONObject();
//        obj.put("AccessNo", "B1228");
        obj.put(parameter, value);
//        obj.put("Material Code","8411630145" );
        RequestQueue queue = Volley.newRequestQueue(this);


        final String requestBody = obj.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {

                JSONArray array = new JSONArray(response);
                dialog.dismiss();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    String Material_Name = object.getString("Material_Name");
                    String Material_Model = object.getString("Material_ID");
                    String Material_Department = object.getString("Material_Department");
                    String Location = object.getString("Location");
                    String TagId= object.getString("TagID");

                    ListSearch.add(new Data_Model_Search(Material_Name, Material_Model, Location, Material_Department,TagId));
//
//                    TempList_Inventory.add(RFIDNO);
                }
                adapter_list = new Adapter_list(ListSearch, getApplicationContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter_list);
                adapter_list.notifyDataSetChanged();
StartBtn.setEnabled(true);

                dialog.dismiss();
//                    Toast.makeText(Inventory_form.this, name, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("VOLLEY", response);
//            dialog.dismiss();
        }, error -> {
            Log.e("VOLLEY Negative", String.valueOf(error.networkResponse.statusCode));
            try {
                if (error.networkResponse.statusCode == 404) {
                    Toast.makeText(SearchForm.this, "No Result Found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchForm.this, "Network Error", Toast.LENGTH_SHORT).show();

                }
                String responseBody = new String(error.networkResponse.data, "utf-8");
                JSONObject data = new JSONObject(responseBody);

                String message = data.getString("message");
//                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
            } catch (UnsupportedEncodingException errorr) {
            }
            dialog.dismiss();
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

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;

                return super.parseNetworkResponse(response);
            }
        };

        queue.add(stringRequest);
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