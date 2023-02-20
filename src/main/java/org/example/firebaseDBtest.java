package org.example;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.cloud.firestore.*;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class firebaseDBtest {
    private Firestore db;
    Bucket bucket;
    public firebaseDBtest(){
        /*
        FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://home-55fa7-default-rtdb.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
        */


        FirestoreOptions firestoreOptions = null;
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src\\resources\\key.json");
            firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                    .setProjectId("home-55fa7")
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("gs://home-55fa7.appspot.com")
                    .build();
            FirebaseApp.initializeApp(options);

            Bucket bucket = StorageClient.getInstance().bucket();
            this.bucket=bucket;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Firestore db = firestoreOptions.getService();
        this.db=db;
    }

    public void addFireStore(){
        DocumentReference docRef = db.collection("users").document("alovelace");
        // addFireStore document data  with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("first", "Ada");
        data.put("last", "Lovelace");
        data.put("born", 1815);
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        // ...
        // result.get() blocks on response

        try {
            System.out.println("Update time : " + result.get().getUpdateTime());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    void retrieveAllDocumentsFireStore() throws Exception {
        // [START firestore_setup_dataset_read]
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = db.collection("users").get();
        // ...
        // query.get() blocks on response
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
            System.out.println("First: " + document.getString("first"));
            System.out.println("Last: " + document.getString("last"));
            System.out.println("Born: " + document.getLong("born"));
        }
        // [END firestore_setup_dataset_read]
    }

    public static void main(String[] args) {
        try {
            new firebaseDBtest().Storage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void Storage(){
        System.out.println(bucket.asBucketInfo());
    }

}