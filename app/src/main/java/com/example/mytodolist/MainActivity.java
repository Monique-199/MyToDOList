package com.example.mytodolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.mytodolist.Adapter.ToDoAdapter;
import com.example.mytodolist.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.WatchChange;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {
    private RecyclerView recyclerView;
    private FloatingActionButton mFab;
    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private List<ToDoModel> mList;
    private Query query;
    private ListenerRegistration listenerRegistration;





    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);
        mFab=findViewById(R.id.floatingactionButton);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        firestore = FirebaseFirestore.getInstance();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewtask.newInstance().show(getSupportFragmentManager(),AddNewtask.TAG);

            }
        });
        mList =new ArrayList<>();
        adapter=new ToDoAdapter(MainActivity.this,mList);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new TouchAdapter(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);



       showData();
       recyclerView.setAdapter(adapter);
      //  recyclerView.setAdapter(adapter);


        }

public void showData(){
      query =  firestore.collection("task").orderBy("time",Query.Direction.DESCENDING );
     listenerRegistration =query.addSnapshotListener(new EventListener<QuerySnapshot>() {
           // @SuppressLint("NotifyDataSetChanged")
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               // if (value != null) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);

                        mList.add(toDoModel);
                        adapter.notifyDataSetChanged();


                    }
                    // }
                }

                //if (listenerRegistration != null) {
                //Collections.reverse(mList);
                    listenerRegistration.remove();

                }
            //}



     });

}
//@SuppressLint("NotifyDataSetChanged")
@Override
    public void onDialogClose(DialogInterface dialogInterface){
    mList.clear();
    showData();
    adapter.notifyDataSetChanged();

}


    }
