package com.example.assestmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    EditText SearchKey;

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
        SearchKey=findViewById(R.id.SearchKEy);
        radioGroup.clearCheck();
        coordinatorLayout = findViewById(R.id.coordinator);
        iuhfService = UHFManager.getUHFService(this);
        looperDemo = new LooperDemo();
        try {
            FetchData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Handler handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

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

                    runOnUiThread(() -> Toast.makeText(SearchForm.this, result, Toast.LENGTH_SHORT).show());
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
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton MaterialId = (RadioButton) group.findViewById(R.id.MaterialID);
            RadioButton MaterialCode = (RadioButton) group.findViewById(R.id.MaterialCode);

            if (checkedId == R.id.MaterialCode) {
                Toast.makeText(SearchForm.this, MaterialCode.getText(), Toast.LENGTH_SHORT).show();
                SearchKey.setHint("Enter Material Code");
            } else if (checkedId == R.id.MaterialID) {
                Toast.makeText(SearchForm.this, MaterialId.getText(), Toast.LENGTH_SHORT).show();
                SearchKey.setHint("Enter Material ID");

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

    //Method for Search Data From Server using Accession Number
    private void FetchData() throws JSONException {
        String url = "https://library.poxorfid.com/api/BooksInfo/FetchBookByAccessNo";
        JSONObject obj = new JSONObject();
//
        obj.put("AccessNo", "B1228");
//        obj.put("AccessNo", accession_no.getText().toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                response -> {
                    try {
//                        String access_details = response.getString("AccessNo");
//                        String Title = response.getString("Title");
//                        String publisher = response.getString("Publisher");
//                        String Author = response.getString("Author");
//                        String subject = response.getString("SubjectTitle");
//                        String language = response.getString("Language");
//                        String edition = response.getString("Edition");
//                        String rfid = response.getString("RFIDNo");

//                        tempList.add(rfid);
//                        list_data_Recyclerview.add(new Data_Model_Search(subject, language, edition, publisher, access_details, Author, Title, rfid));
//
//                        adapter_list = new Adapter_list(list_data_Recyclerview, getApplicationContext());
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                        recyclerView.setAdapter(adapter_list);
//                        if (list_data_Recyclerview.size() > 0) {
//                            Search_btn.setEnabled(true);
//                        }
//                        dialog.dismiss();
//                           System.out.println("Search Response "+response.toString());
                        Toast.makeText(SearchForm.this, response+"", Toast.LENGTH_SHORT).show();
                        Log.e("response Search", response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
//                    dialog.dismiss();
                    System.out.println("Negative Response" + error.getMessage());
                });


        queue.add(jsObjRequest);

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