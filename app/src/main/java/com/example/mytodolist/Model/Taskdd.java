package com.example.mytodolist.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class Taskdd {
    @Exclude
    public String TaskId;

    public <T extends Taskdd> T withId(@NonNull final String id ){
      this.TaskId=id;
      return (T) this;

    }

}

