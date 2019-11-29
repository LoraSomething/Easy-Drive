package com.example.easydriveapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class DataAccesFirestore {

    //final static String UserId = FirebaseAuth.getInstance().getUid();
    //final static String UserId = "darkme.cl@gmail.com";


    public static void CreateFile(final Action<Boolean> action, final Userfile u, final Uri uri) {

        final String UserId = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Userfiles").document(UserId).collection("files").add(u)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(final DocumentReference documentReference) {

                        Userfile uf = u;

                        uf.set_id(documentReference.getId());
                        DataAccesFirestore.UpdateOneFile(new Action<Boolean>() {
                            @Override
                            public void execute(Boolean param) {

                            }
                        }, documentReference.getId(), uf);

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference riversRef = storageRef.child(UserId + "/" + documentReference.getId());
                        UploadTask uploadTask = riversRef.putFile(uri);

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                DataAccesFirestore.DeleteOneFileFromDB(new Action<Boolean>() {
                                    @Override
                                    public void execute(Boolean param) {
                                    }
                                }, documentReference.getId());
                                action.execute(false);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                action.execute(true);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("", "Error adding document", e);
                    }
                });
    }


    public static void GetSingleFile(final Action<Userfile> u, String id) {

        final String UserId = FirebaseAuth.getInstance().getUid();

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


    public static void GetAllFiles(final Action<ArrayList<Userfile>> u) {

        final String UserId = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Userfiles").document(UserId).collection("files")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Userfile> userfiles = new ArrayList<>();
                            for (int i = 0; i < task.getResult().size(); i++) {
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

    public static void DeleteOneFile(final Action<Boolean> action, final String id) {

        final String UserId = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Userfiles").document(UserId).collection("files").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully deleted!");
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference riversRef = storageRef.child(UserId + "/" + id);
                        riversRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                action.execute(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                action.execute(false);
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error deleting document", e);
                    }
                });

    }

    private static void DeleteOneFileFromDB(final Action<Boolean> action, final String id) {

        final String UserId = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Userfiles").document(UserId).collection("files").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       action.execute(true);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error deleting document", e);
                    }
                });

    }


    public static void UpdateOneFile(final Action<Boolean> action, String id, Userfile u) {

        final String UserId = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Userfiles").document(UserId).collection("files").document(id).set(u)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        action.execute(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(MainActivity.this, "Failed Firebase", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static void DownloadOneFile(final Action<Boolean> action, final Userfile u) {

        final String UserId = FirebaseAuth.getInstance().getUid();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child(UserId + "/" + u.get_id());

        File fileNameOnDevice = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"");
        Log.e("--Download Filename----", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + u.getName());

        final File localFile = new File(fileNameOnDevice,u.getName()+"."+u.getFileExtension());

        riversRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("-------firebase------ ",";local tem file created  created " +localFile.toString());
                Log.e("---Bytes Transferred---",Long.toString(taskSnapshot.getBytesTransferred()));
                action.execute(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public static void SetChangeListener(final Action<Boolean> action){
        final String UserId = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Userfiles").document(UserId).collection("files");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("", "Listen failed.", e);
                    return;
                }

                if (queryDocumentSnapshots != null) {
                    action.execute(true);
                } else {
                    Log.d("", "Current data: null");
                    action.execute(true);
                }
            }
        });


    }

}


