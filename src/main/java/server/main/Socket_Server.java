package server.main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;

public class Socket_Server {

    ServerSocket server;
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket_Server(){
        try {
            server=new ServerSocket(7780);
            socket=server.accept();
            System.out.println("connection done");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void startReadingServer(){
        Runnable r1=()->{
            try {
                while (true){
                    in=new ObjectInputStream(socket.getInputStream());
                    Object i=in.readObject();
                    if (i.getClass()==String.class){
                        System.out.println(String.valueOf(i));
                        String i1=String.valueOf(i);
                        if(i1.equals("bell")) {
                            System.out.println("bell");
                            new Sound().main1();
                        }
                    }else if(i.getClass()== ImageIcon.class){
                        //cameraScreen.setIcon((ImageIcon)i);
                    }else{

                    }

                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(r1).start();
    }


    public void openTheDoor(){
        Runnable a=()->{
            try {
                out=new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(new String("Open"));
                out.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };
        new Thread(a).start();
    }
    public static Socket_Server socket_server=null;
    public static Socket_Server getInstance()
    {
        if (socket_server == null)
            socket_server = new Socket_Server();
        return socket_server;
    }

    public void talk_to_person() {
        Runnable a=()->{
            try {
                out=new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(new String("Talk"));
                out.flush();
                String url1="http://localhost:8080/ClientSideWeb/index_server.html";
                Desktop.getDesktop().browse(URI.create(url1));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };
        new Thread(a).start();
    }
}
