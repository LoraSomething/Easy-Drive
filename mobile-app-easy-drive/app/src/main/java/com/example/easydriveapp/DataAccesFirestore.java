package com.example.easydriveapp;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DataAccesFirestore {

    private static String UserId = "darkme.cl@gmail.com";

    public static void CreateFile(Userfile u){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Userfiles").document(UserId).collection("files").document().set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(MainActivity.this, "Succes FIREBASE", Toast.LENGTH_SHORT).show();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(MainActivity.this, "Failed Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static  void GetSingleFile(final Action<Userfile> u, String id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Userfiles").document(UserId).collection("files").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("-------------", "DocumentSnapshot data: " + document.getData());
                        Userfile uf = new Userfile();
                        uf = document.toObject(Userfile.class);
                        u.execute(uf);
                    } else {
                        Log.d("--------------", "No such document");
                    }
                } else {
                    Log.d("--------------", "get failed with ", task.getException());
                }
            }
        });
    }


    public static  void GetAllFiles(final Action<ArrayList<Userfile>> u){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Userfiles").document(UserId).collection("files")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Userfile> userfiles = new ArrayList<>();
                            for (int i = 0; i < task.getResult().size();i++){
                                DocumentSnapshot document = task.getResult().getDocuments().get(i);
                                userfiles.add(document.toObject(Userfile.class));
                                userfiles.get(i).set_id(document.getId());
                            }
                            u.execute(userfiles);
                        } else {
                            //Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
