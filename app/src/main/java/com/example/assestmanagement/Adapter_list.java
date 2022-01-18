package com.example.assestmanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class Adapter_list extends RecyclerView.Adapter<Adapter_list.myviewholder> {
    List<Data_Model_Search> list;
    Context context;
    Dialog dialog;
//    List<String> tempList;

    public Adapter_list(List<Data_Model_Search> list, Context context) {
        this.list = list;
        this.context = context;

    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
//Initial Data model
        Data_Model_Search model_search = list.get(position);

        //Binding Data with components
        holder.head_subject.setText(model_search.getMaterialName());
        holder.head_title.setText(model_search.getMaterialID());
        holder.publisher.setText(model_search.getLocation());
        holder.language.setText(model_search.getMaterialDepartment());
        holder.checkBox.setSelected(model_search.getSelected());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    model_search.setSelected(true);
                } else {
                    model_search.setSelected(false);
                }
            }
        });
        holder.checkBox.setChecked(model_search.getSelected());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dailogbox = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.customdailog, null);
                TextView t1 = dailogbox.findViewById(R.id.t1);
                TextView t2 = dailogbox.findViewById(R.id.t2);
                TextView t3 = dailogbox.findViewById(R.id.t3);
                TextView t4 = dailogbox.findViewById(R.id.t4);
                TextView t5 = dailogbox.findViewById(R.id.t5);
                TextView t6 = dailogbox.findViewById(R.id.t6);
                TextView t7 = dailogbox.findViewById(R.id.t7);


                t1.setText(model_search.getMaterialName());
                t2.setText(model_search.getMaterialModel());
                t3.setText(model_search.getLocation());
                t4.setText(model_search.getMaterialDepartment());
                t5.setText(model_search.getAssignedToEmpID());
                t6.setText(model_search.getCost());
                t7.setText(model_search.getTagID());

                builder.setView(dailogbox);
                builder.setCancelable(true);
                builder.show();

            }
        });
//        holder.
//        holder.Subject.setText(model_search.getSubjectTitle());
//        holder.author.setText(model_search.getAuthor());
//        holder.edition.setText(model_search.getEdition());
//        holder.language.setText(model_search.getLanguage());
//        holder.access_No.setText(model_search.getAccessNo());
//        holder.head_title.setText(model_search.getAccessNo());
//        holder.head_subject.setText(model_search.getTitle());

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
//        //Listener
//        holder.minimize.setOnClickListener(v -> {
//            holder.card_details.setVisibility(View.GONE);
//            holder.cardView.setVisibility(View.VISIBLE);
//            holder.minimize.setVisibility(View.GONE);
//            holder.expand.setVisibility(View.VISIBLE);
//        });

//Change color if data found
        if (model_search.getColor() == "Green") {
//            holder.cardView.setCardBackgroundColor(Color.rgb(46, 139, 87));
            holder.ListLayout.setBackgroundColor(Color.rgb(46, 139, 87));
            holder.head_subject.setTextColor(Color.parseColor("#FFFFFF"));
            holder.head_title.setTextColor(Color.parseColor("#FFFFFF"));
            holder.publisher.setTextColor(Color.parseColor("#FFFFFF"));
            holder.language.setTextColor(Color.parseColor("#FFFFFF"));

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


    public class myviewholder extends RecyclerView.ViewHolder {
        TextView Subject, Title, publisher, author, edition, language, access_No, head_subject, head_title, expand, minimize;
        LinearLayout list_layout;
        CardView cardView, card_details;
        ConstraintLayout ListLayout;
        CheckBox checkBox;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            //Binding components
            checkBox = itemView.findViewById(R.id.checkItem);
            ListLayout = itemView.findViewById(R.id.LayoutList);
            Subject = itemView.findViewById(R.id.Subject);
            expand = itemView.findViewById(R.id.expand);
            minimize = itemView.findViewById(R.id.minimize);
            Title = itemView.findViewById(R.id.Booktitle);
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

    public void restoreItem(Data_Model_Search item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public List<Data_Model_Search> getData() {
        return list;
    }

    //Method for Search
    public void getFilter(@NonNull Object search_value) {
        for (Data_Model_Search row : list) {

            if (row.getTagID().equals(search_value)) {
                row.setColor("Green");
                notifyDataSetChanged();
                break;
            }
//                else {
//                    Toast.makeText(context.getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
//                    break;
//                }
        }

    }

//    public void RetrySearch() {
//        for (Data_Model_Search row : list) {
//            if (row.getColor().equals("Green"))
//                row.setColor("White");
//            notifyDataSetChanged();
//
//        }
//    }

}
