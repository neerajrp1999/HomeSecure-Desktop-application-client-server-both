package clent1.main;

import clent1.swing.EventOpenTheDoor;
import server.main.Socket_Server;
import server.main.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Socket_Client {
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    Boolean canCall=false,canCallStopByAdmin=false,bellCalled=false;
    boolean called=false;

    Socket_Client(){
        try {
            this.socket = new Socket("127.0.0.1",7780);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void startReading(){
        Runnable stop=()->{
            canCallStopByAdmin=true;
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            canCallStopByAdmin=false;
                            canCall=false;
                        }
                    },
                    5000
            );
        };
        Runnable r1=()->{
            try {
                while (true){
                    in=new ObjectInputStream(socket.getInputStream());
                    Object i=in.readObject();
                    if (i.getClass()==String.class){
                        System.out.println(String.valueOf(i));
                        String i1=String.valueOf(i);
                        if(i1.equals("Talk")) {
                            //String url1 = "http://localhost:8080/web2_war_exploded/firebase_html.html?name=Talk";
                            String url1 = "http://localhost:8080/ClientSideWeb/uploadImage.html?name=Talk";
                            Desktop.getDesktop().browse(java.net.URI.create(url1));
                            String url = "http://localhost:8080/ClientSideWeb/call.html";
                            Desktop.getDesktop().browse(java.net.URI.create(url));
                            canCall=false;
                            canCallStopByAdmin=true;
                            BellRingButton2.setVisible(false);
                            BellRingButton1.setVisible(true);
                            stop.run();

                            //open the gui

                        }
                        else if(i1.equals("Open")) {
                            openDoor();
                        }
                    }
                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(r1).start();
    }
    public void openDoor(){eventOpenTheDoor.OpenDoor();}

    clent1.swing.Button CallUserButton;
    clent1.swing.Button BellRingButton1;
    clent1.swing.Button BellRingButton2;
    public void setButtonCallUser(clent1.swing.Button CallUserButton){
        this.CallUserButton=CallUserButton;
    }

    public void ring_the_bell(){

        Runnable a=()->{
            try {
                out=new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(new String("bell"));
                out.flush();
                new Sound().main1();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };

        Thread ta=new Thread(a);
        ta.start();
    }


    public void ring_the_bell(clent1.swing.Button BellRingButton1,clent1.swing.Button BellRingButton2){
        this.BellRingButton1=BellRingButton1;
        this.BellRingButton2=BellRingButton2;

        Runnable a=()->{
            try {
                out=new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(new String("bell"));
                out.flush();
                String url = "http://localhost:8080/ClientSideWeb/uploadImage.html?name=Ring_the_Bell";
                try {
                    Desktop.getDesktop().browse(java.net.URI.create(url));

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };

        Thread ta=new Thread(a);
        ta.start();

        Runnable b=()->{
            long endTime = System.currentTimeMillis() + 30000;
            while (System.currentTimeMillis() < endTime) {
                if(canCallStopByAdmin){
                    CallUserButton.setText("Call Disable");
                    canCall=false;
                    bellCalled=false;
                    BellRingButton2.setVisible(false);
                    BellRingButton1.setVisible(true);
                    break;
                }
                CallUserButton.setText("Call ("+(endTime-System.currentTimeMillis())/1000+" seconds left)");
            }
            if(System.currentTimeMillis() <= endTime && !canCallStopByAdmin){
                CallUserButton.setText("Call");
                canCall=true;
            }
            endTime = System.currentTimeMillis() + 100000;
            while (System.currentTimeMillis() < endTime) {
                CallUserButton.setText("make a call ("+(endTime-System.currentTimeMillis())/1000+" seconds left)");
                if(canCallStopByAdmin){
                    CallUserButton.setText("Call Disable");
                    BellRingButton2.setVisible(false);
                    BellRingButton1.setVisible(true);
                    canCall=false;
                    bellCalled=false;
                    break;
                }
            }

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            canCallStopByAdmin=false;
                            CallUserButton.setText("Call Disable");
                            canCall=false;
                            bellCalled=false;
                            BellRingButton2.setVisible(false);
                            BellRingButton1.setVisible(true);
                        }
                    },
                    8000
            );

        };

        Thread tb=new Thread(b);
        try{
            if(! tb.isAlive()){
                tb.start();
            }
        }catch (Exception e1){
            System.out.println(e1.getMessage());
        }
        bellCalled=false;
    }


    public static Socket_Client socket_client=null;
    public static Socket_Client getInstance()
    {
        if (socket_client == null)
            socket_client = new Socket_Client();
        return socket_client;
    }
    private EventOpenTheDoor eventOpenTheDoor;
    public void setEventOpenTheDoor(EventOpenTheDoor eventOpenTheDoor) {
        this.eventOpenTheDoor = eventOpenTheDoor;
    }

    public void call_User() {
        if(canCall){
            System.out.println("Call admin");

            String url = "http://127.0.0.1:7001/apicall/noti_push/";
            //String url1 = "http://localhost:8080/ClientSideWeb/call.html";
            try {
                Desktop.getDesktop().browse(java.net.URI.create(url));
                if(!called){
              //      Desktop.getDesktop().browse(java.net.URI.create(url1));
                    called=true;
                }
                Runnable time=()->{
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    called=false;
                                }
                            },
                            5000
                    );
                };
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
