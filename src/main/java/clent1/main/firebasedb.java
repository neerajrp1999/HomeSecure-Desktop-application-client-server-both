package clent1.main;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class firebasedb {
    public static void main(String[] args) {
        System.out.println(new firebasedb().FatchUserActivityList());
    }
    Firestore db;
    public firebasedb(){
        try {
            FileInputStream serviceAccount = new FileInputStream("src\\resources\\Key.json");
            FileInputStream serviceAccountN = new FileInputStream("src\\resources\\KeyN.json");

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
            System.out.println("id: size" + documents.size());

            for (QueryDocumentSnapshot document : documents) {
                ArrayList<String> d=new ArrayList<>();
                d.add(document.getId());
                d.add(document.getString("name"));
                d.add(document.getString("pass"));
                s.add(d);
            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
           // f.dispose();
            System.out.println(e.toString());
            JOptionPane pane = new JOptionPane("Internet Access Problem !!");
            final JDialog dialog = pane.createDialog("Error");
            Timer timer = new Timer(3000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                }
            });
            timer.setRepeats(false);
            timer.start();
            dialog.setVisible(true);
        }
        //f.dispose();
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
        //JFrame f=frameCaller();
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
        //f.dispose();
        return r;
    }

    public ArrayList<ArrayList<String>> FatchUserActivityList() {


        ArrayList<ArrayList<String>> s=new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> query = db.collection("UsersActivity").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            System.out.println("id: fetch " + documents.size());

            for (QueryDocumentSnapshot document : documents) {
                try{
                    ArrayList<String> d=new ArrayList<>();
                    long a=Long.parseLong(document.getId());
                    Date date2 = new Date(a);
                    d.add(date2.toString());
                    d.add(document.getString("type"));
                    d.add(document.getString("link"));
                    s.add(d);
                }catch (Exception e){}
            }
        } catch (ExecutionException e) {
            Internet_Access_Problem();
            //throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Internet_Access_Problem();
            //throw new RuntimeException(e);
        }
        return s;
    }

    public int getNewID() {
        //JFrame f=frameCaller();
        int newid=0;
        try {
            ApiFuture<QuerySnapshot> query = db.collection("UsersList").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            System.out.println("id: newid" + documents.size());
            for (QueryDocumentSnapshot document : documents) {
                int id=Integer.parseInt(document.getId());
                if(id>=newid){
                    newid=id;
                }
            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
            System.out.println(e.toString());
            Internet_Access_Problem();
        }
        //f.dispose();
        return newid+1;
    }

    public boolean checkIdNo(int id) {
        //JFrame f=frameCaller();
        try {
            ApiFuture<QuerySnapshot> query = db.collection("UsersList").get();

            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            System.out.println("id: userlist" + documents.size());

            for (QueryDocumentSnapshot document : documents) {
                if(id==Integer.parseInt(document.getId())){
                 //   f.dispose();
                    return true;
                }
            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
            Internet_Access_Problem();
        }
        //f.dispose();
        return false;
    }

    public long checkUpdateInModel() {
        //JFrame f=frameCaller();
        try {
            ApiFuture<DocumentSnapshot> query = db.collection("UserDirection").document("update_in_model").get();
            DocumentSnapshot documentSnapshot = query.get();
            long data=2;
            if(documentSnapshot.exists()){
                data=(long) documentSnapshot.get("is");
            }
           // f.dispose();
            return data;
        } catch (Exception e) {
            System.out.println(e.toString());
            //Internet_Access_Problem();
            //f.dispose();

        }
        return 2;
    }

    public ArrayList<String> readDataOfUserByID(int id){
        //JFrame f = frameCaller();
        ArrayList<String> s=new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> query = db.collection("UsersList").get();

            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            System.out.println("id: hell" + documents.size());

            for (QueryDocumentSnapshot document : documents) {

                if(Integer.parseInt(document.getId())==id){
                    s.add(document.getString("name"));
                    s.add(document.getString("pass"));
                }

            }
            //f.setVisible(false);
        } catch (Exception e) {
            //throw new RuntimeException(e);
            //f.dispose();
            System.out.println(e.toString());
            JOptionPane pane = new JOptionPane("Internet Access Problem !!");
            final JDialog dialog = pane.createDialog("Error");
            Timer timer = new Timer(3000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                }
            });
            timer.setRepeats(false);
            timer.start();
            dialog.setVisible(true);
        }
        //f.setVisible(false);
        //f.dispose();
        return s;
    }
public static JFrame frameCaller(){
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

    return f;
}

public String fetModelURL() {
        //JFrame f=frameCaller();
        String data=null;
        try {
            ApiFuture<DocumentSnapshot> query = db.collection("UserDirection").document("update_in_model").get();
            DocumentSnapshot documentSnapshot = query.get();
            if(documentSnapshot.exists()){
                data=(String) documentSnapshot.get("link");
            }

        } catch (Exception e) {
            //f.dispose();
            Internet_Access_Problem();
        }
        //f.dispose();
        return data;
    }

    public static void Internet_Access_Problem(){
        JOptionPane pane = new JOptionPane("Internet Access Problem !!");
        final JDialog dialog = pane.createDialog("Error");
        Timer timer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }

    public void UpdateInModelStatus(int i) {
        HashMap<String,Object> h=new HashMap<String, Object>(1);
        h.put("is",i);
        try {
            db.collection("UserDirection").document("update_in_model").update(h);
        } catch (Exception e) {
            System.out.println(e.toString());

        }
    }
}