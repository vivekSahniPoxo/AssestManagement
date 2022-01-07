package com.example.assestmanagement;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_Inventory extends RecyclerView.Adapter<Adapter_Inventory.MyViewholder> {
    List<DataModel_Inventory> list;
    List<String> templist;
    Context c;
//    SharedPreferences pref;

    public Adapter_Inventory(List<DataModel_Inventory> list, Context c) {
        this.list = list;
        this.c = c;
//        this.templist = tempList_Inventory;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.list, parent, false);
        return new MyViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        //Initialize model
        DataModel_Inventory dataModel_inventory = list.get(position);

        //binding data with  components
        holder.Title.setText(dataModel_inventory.getLocation());
        holder.publisher.setText(dataModel_inventory.getToolsName());
//        holder.Subject.setText(dataModel_inventory.getSubjectTitle());
//        holder.author.setText(dataModel_inventory.getAuthor());
//        holder.edition.setText(dataModel_inventory.getEdition());
//        holder.language.setText(dataModel_inventory.getLanguage());
//        holder.access_No.setText(dataModel_inventory.getAccessNo());
//        holder.head_title.setText(dataModel_inventory.getSubjectTitle());
//        holder.head_subject.setText(dataModel_inventory.getAccessNo());
//        holder.expand.setTextColor(Color.BLACK);
//        holder.expand.getCompoundDrawables()[0].setTint(Color.BLACK);
//        holder.expand.setTextColor(Color.parseColor("#000000"));
//        holder.ListLayout.setBackgroundColor(Color.parseColor("#E5E6E6"));

//        holder.expand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.card_details.setVisibility(View.VISIBLE);
//                holder.expand.setVisibility(View.GONE);
//                holder.minimize.setVisibility(View.VISIBLE);
//
//
////                holder.cardView.setVisibility(View.GONE);
//            }
//        });
//        holder.minimize.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.card_details.setVisibility(View.GONE);
//                holder.cardView.setVisibility(View.VISIBLE);
//                holder.minimize.setVisibility(View.GONE);
//                holder.expand.setVisibility(View.VISIBLE);
//            }
//        });

//       Change color if Search Found
//        if (dataModel_inventory.getColor() == "Green") {
//            holder.head_subject.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.minimize.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.publisher.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.language.setTextColor(Color.parseColor("#FFFFFF"));
//
//            holder.ListLayout.setBackgroundColor(Color.rgb(46, 139, 87));
//            holder.head_title.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.expand.setOnClickListener(v -> {
//                holder.card_details.setVisibility(View.VISIBLE);
//                holder.expand.setVisibility(View.GONE);
//                holder.minimize.setVisibility(View.VISIBLE);
//
//
////                holder.cardView.setVisibility(View.GONE);
//            });
            //            holder.Title_Details.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.Author_Details.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.RFid_details.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.Access_detail.setTextColor(Color.parseColor("#FFFFFF"));

//        } else {
//            holder.ListLayout.setBackgroundColor(Color.parseColor("#C6CFCF"));
//            holder.head_subject.setTextColor(Color.parseColor("#000000"));
//            holder.head_title.setTextColor(Color.parseColor("#000000"));
//            holder.publisher.setTextColor(Color.parseColor("#000000"));
//            holder.language.setTextColor(Color.parseColor("#000000"));
//
//
//        }
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

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
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

//    public int getFilter(Object search_value) {
//        if (templist.contains(search_value)) {
//            for (DataModel_Inventory row : list) {
//
//                if (row.getrFIDNo().equals(search_value)) {
//                    row.setColor("Green");
//                    row.setStatus("True");
//                    bookFooundCount = bookFooundCount + 1;
//                    notifyDataSetChanged();
//                    break;
//                }
//            }
//            templist.remove(search_value);
//        }
//
//
//        return bookFooundCount;
//    }

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
