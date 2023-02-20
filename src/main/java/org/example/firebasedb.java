package org.example;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class firebasedb {
    Firestore db;
    public firebasedb(){
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src\\resources\\Key.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://home-55fa7-default-rtdb.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
            this.db = FirestoreClient.getFirestore();
            System.out.println("sasdasdsdsdds");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static firebasedb firebase_db=null;
    public static firebasedb getInstance()
    {
        if (firebase_db == null)
            firebase_db = new firebasedb();
        return firebase_db;
    }
    public ArrayList<ArrayList<String>> readData(){

        ArrayList<ArrayList<String>> s=new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> query = db.collection("UsersList").get();

            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            System.out.println("id: " + documents.size());

            for (QueryDocumentSnapshot document : documents) {
                ArrayList<String> d=new ArrayList<>();
                d.add(document.getId());
                d.add(document.getString("name"));
                d.add(document.getString("pass"));
                s.add(d);
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return s;
    }
    public Boolean addData(String id, String name, String pass) {

        DocumentReference docRef = db.collection("UsersList").document(id);

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("pass", pass);

        ApiFuture<WriteResult> result = docRef.set(data);

        Boolean r;
        while(true){
            if(result.state()== Future.State.CANCELLED || result.state()== Future.State.FAILED ){
                r=false;
                break;
            }else if(result.state()== Future.State.SUCCESS){
                r=true;
                break;
            }
        }
        return r;
    }
    public boolean updatePassword(String pass,String id){
        Map<String, Object> data = new HashMap<>();
        data.put("pass", pass);
        DocumentReference documentReference=db.collection("UsersList").document(id);
        ApiFuture<WriteResult> result = documentReference.update(data);

        Boolean r;
        while(true){
            if(result.state()== Future.State.CANCELLED || result.state()== Future.State.FAILED ){
                r=false;
                break;
            }else if(result.state()== Future.State.SUCCESS){
                r=true;
                break;
            }
        }
        return r;
    }

    public boolean deleteUser(String id) {

        DocumentReference documentReference=db.collection("UsersList").document(id);

        ApiFuture<WriteResult> result = documentReference.delete();

        Boolean r;
        while(true){
            if(result.state()== Future.State.CANCELLED || result.state()== Future.State.FAILED ){
                r=false;
                break;
            }else if(result.state()== Future.State.SUCCESS){
                r=true;
                break;
            }
        }
        return r;
    }

    public ArrayList<ArrayList<String>> FatchUserActivityList() {

        JFrame f = new JFrame();
        JPanel p = new JPanel();
        JProgressBar jb=new JProgressBar(0,2000);
        jb.setBounds(40,40,160,30);

        JLabel t=new JLabel("Wait for response");
        t.setBounds(15,35,160,30);
        f.add(t);

        jb.setStringPainted(true);
        f.add(jb);
        f.setSize(250,150);
        jb.setIndeterminate(true);
        p.add(jb);
        f.add(p);
        f.setSize(200, 120);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setVisible(true);

        ArrayList<ArrayList<String>> s=new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> query = db.collection("UsersActivity").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            System.out.println("id: " + documents.size());

            for (QueryDocumentSnapshot document : documents) {
                ArrayList<String> d=new ArrayList<>();
                d.add(document.getId());
                d.add(document.getString("link"));
                d.add(document.getString("type"));
                s.add(d);
            }
        } catch (ExecutionException e) {
            f.dispose();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            f.dispose();
            throw new RuntimeException(e);
        }
        f.dispose();
        return s;
    }

    public int getNewID() {
        try {
            ApiFuture<QuerySnapshot> query = db.collection("UsersList").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            System.out.println("id: " + documents.size());
            return documents.size();
        } catch (Exception e) {
            return 0;
        }
    }
}