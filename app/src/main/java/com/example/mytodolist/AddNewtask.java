package  com.example.mytodolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewtask extends BottomSheetDialogFragment {
    public static final String TAG="AddNewTask";
    private TextView setDuedate;
    private EditText mTaskEdit;
    private Button msaveBtn;
    private FirebaseFirestore firestore;
    private Context context;
    private String dueDate="";
    private  String id ="";
    private  String dueDateUpdate="";


    public static AddNewtask newInstance(){
        return new AddNewtask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.add_new_task,container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        //declaring and initializing the components
        setDuedate =view.findViewById(R.id.set_duedate);
        mTaskEdit=view.findViewById(R.id.tast_et);
        msaveBtn = view.findViewById(R.id.button);

        firestore=FirebaseFirestore.getInstance();

        boolean IsUpdate= false;
        final Bundle bundle=getArguments();
        if(bundle !=null){
            IsUpdate=true;
            String task= bundle.getString("task");
            id=bundle.getString("id");
            dueDateUpdate=bundle.getString("due");

            mTaskEdit.setText(task);
            setDuedate.setText(dueDate);

            if(task.length()>0){
                msaveBtn.setEnabled(false);
                msaveBtn.setBackgroundColor(Color.GRAY);

            }

        }

        mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    msaveBtn.setEnabled(false);
                    msaveBtn.setBackgroundColor(Color.GRAY);

                }else{
                    msaveBtn.setEnabled(true);
                    msaveBtn.setBackgroundColor(getResources().getColor(R.color.purple_200));



                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setDuedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //about the date time picker
                Calendar calendar= Calendar.getInstance();
                int MONTH=calendar.get(Calendar.MONTH);
                int YEAR=calendar.get(Calendar.YEAR);
                int DAY=calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog= new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        setDuedate.setText(dayOfMonth + "/" + month+ "/" + year);

                        dueDate=dayOfMonth +"/"+month+"/"+year;



                    }
                }, YEAR,MONTH,DAY);
                datePickerDialog.show();



            }
        });
        msaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editing the task
              String task=mTaskEdit.getText().toString();
                boolean IsUpdate = false;
                if (IsUpdate){
                  firestore.collection("task").document(id).update("task",task,"due",dueDate);
                  Toast.makeText(context,"TASK UPDATED ",Toast.LENGTH_SHORT).show();


              }
              if (task.isEmpty()){
                  Toast.makeText(context,"Empty task not allowed",Toast.LENGTH_SHORT).show();

              }else{
                  //in the database
                  Map<String, Object> taskMap=new HashMap<>();
                  taskMap.put("task",task);
                  taskMap.put("due",dueDate);
                  taskMap.put("status",0);
                  taskMap.put("time", FieldValue.serverTimestamp());

                  firestore.collection("task").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                      @Override
                      public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                          Toast.makeText(context,"Task Saved",Toast.LENGTH_SHORT).show();


                        }else{
                            Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                      }
                  });



              }

              //dismiss();
            }
        });

    }
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        super.onDismiss(dialog);
        Activity activity=getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener) activity).onDialogClose(dialog);

        }
    }



}

/*package com.example.mytodolist;

import android.app.Activity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddNewtask extends Activity {
    public static final Object TAG = ;

    public static FloatingActionButton newInstance() {
        return null;
    }
}*/
