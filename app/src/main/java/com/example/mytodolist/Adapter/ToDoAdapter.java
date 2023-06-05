package com.example.mytodolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.AddNewtask;
import com.example.mytodolist.MainActivity;
import com.example.mytodolist.Model.ToDoModel;
import com.example.mytodolist.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<ToDoModel> todoList;
    private MainActivity activity;
    private FirebaseFirestore firestore;
    public ToDoAdapter(MainActivity mainActivity, List<ToDoModel> todoList){
        this.todoList=todoList;
        activity=mainActivity;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task, parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);


    }

    public void deleteTask(int position) {
       // if (position != RecyclerView.NO_POSITION) {
            ToDoModel toDoModel = todoList.get(position);
            firestore.collection("task").document(toDoModel.TaskId).delete();
            todoList.remove(position);
            notifyItemRemoved(position);

        }
    //}





  /*  public void deleteTask(int position) {
       // if (position != RecyclerView.NO_POSITION) {
            ToDoModel toDoModel = todoList.get(position);
            firestore.collection("task").document(toDoModel.TaskId).delete();
            todoList.remove(position);
            notifyItemRemoved(position);

        }
        */
    public Context getContext(){
        return activity;
    }
    //}





    public void editTask(int position){
      ToDoModel toDoModel=todoList.get(position);
        Bundle bundle= new Bundle();
        bundle.putString("task",toDoModel.getTask());
        bundle.putString("due",toDoModel.getDue());
        bundle.putString("id",toDoModel.TaskId);

        AddNewtask addNewtask=new AddNewtask();
        addNewtask.setArguments(bundle);
        addNewtask.show(activity.getSupportFragmentManager(),addNewtask.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position){
       ToDoModel toDoModel=todoList.get(position);
       holder .mCheckBox.setText(toDoModel.getTask());
       holder.mDuedateTv.setText("Due on"+ toDoModel.getDue());
       holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));

       holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if(isChecked){
                 firestore.collection("tasks").document(toDoModel.TaskId).update("status",1);


             } else {
                firestore.collection("tasks").document(toDoModel.TaskId).update("status",0);

             }
           }
       });


    }
private boolean toBoolean(int status){
        return status !=0;

}

    public int getItemCount(){
        return todoList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDuedateTv;
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            mDuedateTv=itemView.findViewById(R.id.due_date);
            mCheckBox=itemView.findViewById(R.id.materialcheckbox);


        }
    }

}
