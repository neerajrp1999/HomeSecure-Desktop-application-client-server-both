package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import recognition.Recognition;

public class client {
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    JFrame f;
    public client(){
        try {
            socket=new Socket("127.0.0.1",7778);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void callFrame(){
        f=new JFrame();
        f.setLayout(null);
        f.setVisible(true);
        f.setAlwaysOnTop(true);
        JButton face_recognization=new JButton("Face Recognization");
        face_recognization.setBounds(100,400,150,40);
        face_recognization.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new Recognition().Call_Recognizer();
                    f.setVisible(false);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        f.add(face_recognization);

        JButton bell=new JButton("Bell Ring");
        bell.setBounds(260,400,100,40);
        bell.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button clicked");
                Runnable a=()->{
                    try {
                        out=new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(new String("bell"));
                        out.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                };
                new Thread(a).start();
            }
        });
        f.add(bell);

        JButton call=new JButton("Call");
        call.setBounds(370,400,100,40);
        call.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        f.add(call);

        f.setSize(new Dimension(640,560));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        //startReading();
    }
    public void startReading(){
        Runnable r1=()->{
            try {
                while (true){
                    /*
                    //ic=(ImageIcon)in.readObject();
                    in=new ObjectInputStream(socket.getInputStream());
                    byte[] imageData=(byte[])in.readObject();
                    ic=new ImageIcon(imageData);
                    cameraScreen.setIcon(ic);
                    //in.reset();
                    in.close();

                     */
                    /*
                    try{
                        ic=(ImageIcon)in.readObject();
                        System.out.println(ic);
                        cameraScreen.setIcon(ic);
                    }catch (IOException e){
                        try{
                            String a=(String)in.readObject();
                            System.out.println(a);
                            //cameraScreen.setIcon(ic);
                        }catch (IOException exception){}
                    }
                    */

                    in=new ObjectInputStream(socket.getInputStream());
                    Object i=in.readObject();
                    if (i.getClass()==String.class){
                        System.out.println(String.valueOf(i));
                        String i1=String.valueOf(i);
                        if(i1.equals("Talk")) {
                            String url1 = "http://localhost:8080/web2_war_exploded/firebase_html.html?name=Talk";
                            Desktop.getDesktop().browse(java.net.URI.create(url1));
                            String url = "http://localhost:8080/web2_war_exploded/";
                            Desktop.getDesktop().browse(java.net.URI.create(url));
                        }else if(i1.equals("Open")) {
                            f.setVisible(false);
                            JOptionPane pane = new JOptionPane("Welcome to home!\nOpening Door !!");
                            final JDialog dialog = pane.createDialog("Welcome");
                            Timer timer = new Timer(5000, new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    dialog.setVisible(false);
                                    dialog.dispose();
                                    f.setVisible(true);
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                            dialog.setVisible(true);
                        }
                    }

                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(r1).start();
    }
    public static void main(String[] args) {
        client a=new client();
        a.startReading();
        a.callFrame();
    }
}
