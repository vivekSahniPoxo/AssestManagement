package com.example.assestmanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_Inventory extends RecyclerView.Adapter<Adapter_Inventory.MyViewholder> {
    List<DataModel_Inventory> list;
    List<String> templist;


    Context c;
//    SharedPreferences pref;

    public Adapter_Inventory(List<DataModel_Inventory> list, Context c, List<String> tempList_Inventory) {
        this.list = list;
        this.c = c;
        this.templist = tempList_Inventory;

    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.list, parent, false);
        return new MyViewholder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        //Initialize model
        DataModel_Inventory dataModel_inventory = list.get(position);

        //binding data with  components
        holder.head_subject.setText(dataModel_inventory.getMaterialName());
        holder.head_title.setText(dataModel_inventory.getMaterialID());
        holder.publisher.setText(dataModel_inventory.getLocation());
        holder.language.setText(dataModel_inventory.getMaterialDepartment());
        holder.checkBox.setSelected(dataModel_inventory.getSelected());

        for (DataModel_Inventory row : list) {
            if (row.getCheckList() == "True") {
                row.setSelected(true);
                row.setCheckList("True");
            } else if (row.getCheckList() == "false") {
                row.setSelected(false);
                row.setCheckList("false");
            }

        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataModel_inventory.setSelected(true);
                    dataModel_inventory.setCheckList("True");

                } else {
                    dataModel_inventory.setSelected(false);
                }
            }
        });
        holder.checkBox.setChecked(dataModel_inventory.getSelected());
        holder.cardView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
            View dailogbox = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.customdailog, null);
            TextView t1 = dailogbox.findViewById(R.id.t1);
            TextView t2 = dailogbox.findViewById(R.id.t2);
            TextView t3 = dailogbox.findViewById(R.id.t3);
            TextView t4 = dailogbox.findViewById(R.id.t4);
            TextView t5 = dailogbox.findViewById(R.id.t5);
            TextView t6 = dailogbox.findViewById(R.id.t6);
            TextView t7 = dailogbox.findViewById(R.id.t7);


            t1.setText(dataModel_inventory.getMaterialName());
            t2.setText(dataModel_inventory.getMaterialModel());
            t3.setText(dataModel_inventory.getLocation());
            t4.setText(dataModel_inventory.getMaterialDepartment());
            t5.setText(dataModel_inventory.getAssignedToEmpID());
            t6.setText(dataModel_inventory.getCost());
            t7.setText(dataModel_inventory.getTagID());

            builder.setView(dailogbox);
            builder.setCancelable(true);
            builder.show();

        });

//       Change color if Search Found
        if (dataModel_inventory.getColor() == "Green") {
            holder.head_subject.setTextColor(Color.parseColor("#FFFFFF"));
            holder.minimize.setTextColor(Color.parseColor("#FFFFFF"));
            holder.publisher.setTextColor(Color.parseColor("#FFFFFF"));
            holder.language.setTextColor(Color.parseColor("#FFFFFF"));
            holder.ListLayout.setBackgroundColor(Color.rgb(46, 139, 87));
            holder.head_title.setTextColor(Color.parseColor("#FFFFFF"));

        } else {
            holder.ListLayout.setBackgroundColor(Color.parseColor("#C6CFCF"));
            holder.head_subject.setTextColor(Color.parseColor("#000000"));
            holder.head_title.setTextColor(Color.parseColor("#000000"));
            holder.publisher.setTextColor(Color.parseColor("#000000"));
            holder.language.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        TextView Subject, Title, publisher, author, edition, language, access_No, head_subject, head_title, expand, minimize;
        LinearLayout list_layout;
        CardView cardView, card_details;
        ConstraintLayout ListLayout;
        CheckBox checkBox;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            //Binding components
            checkBox = itemView.findViewById(R.id.checkItem);

            ListLayout = itemView.findViewById(R.id.LayoutList);
            Subject = itemView.findViewById(R.id.Subject);
            Title = itemView.findViewById(R.id.Booktitle);
            expand = itemView.findViewById(R.id.expand);
            minimize = itemView.findViewById(R.id.minimize);
            list_layout = itemView.findViewById(R.id.list_layout);
            cardView = itemView.findViewById(R.id.cardView);
            card_details = itemView.findViewById(R.id.cardView_Details);
            publisher = itemView.findViewById(R.id.Publisher);
            author = itemView.findViewById(R.id.Authorname);
            edition = itemView.findViewById(R.id.Edition);
            language = itemView.findViewById(R.id.Language);
            access_No = itemView.findViewById(R.id.Access_No);
            head_subject = itemView.findViewById(R.id.Head_subject);
            head_title = itemView.findViewById(R.id.Head_Tilte);


        }
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(DataModel_Inventory item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public List<DataModel_Inventory> getData() {
        return list;
    }

    //   method for search data
    int bookFooundCount = 0;

    public int getFilter(Object search_value) {
        try {
            if (templist.contains(search_value)) {
                for (DataModel_Inventory row : list) {
                    if (row.getCheckList().equals("True")) {
                        if (row.getTagID().equals(search_value)) {
                            row.setColor("Green");
                            row.setStatus("True");
                            bookFooundCount = bookFooundCount + 1;
                            notifyDataSetChanged();
                            break;
                        }
                        notifyDataSetChanged();
                    }
                }
                templist.remove(search_value);
            }

        } catch (Exception e) {
            Toast.makeText(c, "SelectItem to be Search...", Toast.LENGTH_SHORT).show();
        }
        return bookFooundCount;
    }

//    public void RetrySearch() {
//        for (DataModel_Inventory row : list) {
//            try {
//                if (row.getColor().matches("Green")) {
//                    row.setColor("White");
//                    notifyDataSetChanged();
//                }
//                else {
//                    System.out.println("Searching ");
//                }
//
//            }catch (Exception e)
//            {
//                System.out.print(e.getMessage());
//            }
//
//        }
//    }
}
