package org.example;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import recognition.Recognition;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class server {
    ServerSocket server;
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    JLabel cameraScreen;
    JFrame f;
    static firebasedb db=firebasedb.getInstance();

    public void callJFrame_server(){
        f=new JFrame();
        f.setLayout(null);

        JButton face_recognization=new JButton("Admin Login");
        face_recognization.setBounds(100,400,150,40);
        face_recognization.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new Recognition().Call_Recognizer();
                try {
                    new Recognition().Call_Admin_Recognizer(server.this);
                    f.setVisible(false);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        f.add(face_recognization);

        JButton bell=new JButton("See/Talk out side");
        bell.setBounds(260,400,100,40);
        bell.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runnable a=()->{
                    try {
                        out=new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(new String("Talk"));
                        out.flush();
                        String url = "http://localhost:8080/web2_war_exploded/";
                        Desktop.getDesktop().browse(URI.create(url));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                };
                new Thread(a).start();
            }
        });
        f.add(bell);

        JButton call=new JButton("Open Door");
        call.setBounds(370,400,100,40);
        call.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });
        f.add(call);
        f.setSize(new Dimension(640,560));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    public void startReadingServer(){

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
                        if(i1.equals("bell")) {
                            System.out.println("bell");
                            new Sound().main1();
                        }
                    }else if(i.getClass()==ImageIcon.class){
                        cameraScreen.setIcon((ImageIcon)i);
                    }

                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(r1).start();
    }

    public static void main(String[] args) {
        server s= new server();
        s.connectSocket();
        s.startReadingServer();
        s.callJFrame_server();
    }

    private void connectSocket() {
        try {
            //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            server=new ServerSocket(7778);
            socket=server.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void callJFrame_server_page2(){

        ArrayList<ArrayList<String>> data=db.readData();
        ArrayList<String> arrayList= new ArrayList<>(data.size());
        for(int i=0;i<data.size();i++){
            ArrayList<String> array=data.get(i);
            arrayList.add("ID:  "+array.get(0)+"    |   Name:   "+array.get(1));
        }

        JFrame f=new JFrame();
        f.setLayout(null);

        JPanel panel = new JPanel(new BorderLayout());
        final JList<String> jlist = new JList<>(arrayList.toArray(new String[arrayList.size()]));
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlist.setFont(new Font("Serif", Font.ITALIC, 14));
        JScrollPane listScroller = new JScrollPane();
        listScroller.setViewportView(jlist);
        jlist.setLayoutOrientation(JList.VERTICAL);
        jlist.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    int position = list.locationToIndex(evt.getPoint()) + 1;
                    f.dispose();
                    showUser(position,data.get(position-1));
                }
            }
        });

        panel.add(listScroller);

        panel.setBounds(150,70,300,300);
        f.add(panel);

        JButton add_user=new JButton("Add New User");
        add_user.setBounds(100,400,150,40);
        add_user.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddNewUser().showUser();
                f.dispose();
            }
        });
        f.add(add_user);

        JButton activity=new JButton("Activities List");
        activity.setBounds(260,400,100,40);
        activity.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<ArrayList<String>> data=db.FatchUserActivityList();
                ArrayList<String> showdata=new ArrayList<>(data.size());
                ArrayList<String> link=new ArrayList<>(data.size());
                for(int i=0;i<data.size();i++){
                    ArrayList<String> temp=data.get(i);
                    Date currentDate = new Date(Long.parseLong(temp.get(0)));
                    showdata.add(currentDate.toString()+"   |   "+temp.get(2));
                    link.add(temp.get(1));
                }
                f.dispose();
                UserActivityList(showdata,link);

            }
        });
        f.add(activity);

        JButton back=new JButton("Back");
        back.setBounds(370,400,100,40);
        back.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                //call main
                callJFrame_server();

            }
        });
        f.add(back);

        f.setSize(new Dimension(640,560));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setVisible(true);
    }
    public static BufferedImage getImage(int id)throws IOException{
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        String file = "src\\resources\\TempPhotos\\"+String.valueOf(id)+".jpg";
        Mat image = Imgcodecs.imread(file);

        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, matOfByte);

        byte[] byteArray = matOfByte.toArray();
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage b= ImageIO.read(in);

        BufferedImage resizedImg = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(b, 0, 0, 200, 200, null);
        g2.dispose();
        return resizedImg;
    }
    public void showUser(int id,ArrayList<String> data) {
        BufferedImage bufImage = null;
        try {
            bufImage = getImage(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame("User");
        frame.setLayout(null);
        frame.setSize(550,500);
        JLabel l=new JLabel(new ImageIcon(bufImage));
        l.setBounds(10,125,200,200);
        frame.add(l);

        JLabel l2=new JLabel("ID:\t"+"\t"+id);
        l2.setBounds(220,130,100,20);
        frame.add(l2);

        JLabel l3=new JLabel("Name:\t"+"\t"+data.get(1));
        l3.setBounds(220,187,100,20);
        frame.add(l3);

        JLabel l4=new JLabel("Password:");
        l4.setBounds(220,245,70,20);
        frame.add(l4);

        JTextField t=new JFormattedTextField();
        t.setBounds(290,245,70,20);
        t.setEditable(false);
        t.setText(data.get(2));
        frame.add(t);

        JButton update=new JButton("Update");
        update.setBounds(430,245,100,20);
        update.setVisible(false);
        frame.add(update);
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pass=t.getText().toString().trim();
                if(pass.length()<=0){
                    JOptionPane pane = new JOptionPane("Error !!");
                    JOptionPane.showMessageDialog(pane, "Password size is not vallid.","Error",JOptionPane.ERROR_MESSAGE);
                } else{
                    int result = JOptionPane.showConfirmDialog(null,"Sure? You want to update password?", "Update",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        JOptionPane pane = new JOptionPane("Info !!");
                        Timer timer = new Timer(5000, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                pane.setVisible(false);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                        if(db.updatePassword(pass,String.valueOf(id))){
                            JOptionPane.showMessageDialog(pane, "Update Done");
                        }else {
                            JOptionPane.showMessageDialog(pane, "Something Wrong Update not done!!");
                        }
                    /*
                    JOptionPane pane = new JOptionPane("Yes !!");
                    JOptionPane.showMessageDialog(pane, "My Goodness, this is so concise");
                     */

                    }
                }
            }
        });

        JButton b=new JButton("Edit");
        b.setBounds(360,245,70,20);
        frame.add(b);
        b.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(t.isEditable()){
                    t.setEditable(false);
                    b.setText("Edit");
                    t.setText("pass");
                    update.setVisible(false);
                }else{
                    t.setEditable(true);
                    b.setText("Undo");
                    update.setVisible(true);
                }
            }
        });

        JButton delete=new JButton("Delete User");
        delete.setBounds(220,300,100,20);
        frame.add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null,"Sure? You Want to delete this user?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    JOptionPane pane = new JOptionPane("Info !!");
                    Timer timer = new Timer(5000, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            pane.setVisible(false);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                    if(db.deleteUser(String.valueOf(id))){
                        JOptionPane.showMessageDialog(pane, "Delete Done !!!");
                        frame.dispose();
                        callJFrame_server_page2();
                    }else {
                        JOptionPane.showMessageDialog(pane, "Something Wrong Delete not done!!");
                    }
                    /*
                    JOptionPane pane = new JOptionPane("Yes !!");
                    JOptionPane.showMessageDialog(pane, "My Goodness, this is so concise");
                     */
                }
            }
        });

        JButton back=new JButton("Back");
        back.setBounds(340,300,100,20);
        frame.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                callJFrame_server_page2();
            }
        });

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    public void UserActivityList(ArrayList<String> data,ArrayList<String> link){

        JFrame frame = new JFrame("User");
        frame.setLayout(null);
        frame.setSize(550,500);

        JLabel image_label=new JLabel();
        image_label.setBounds(10,125,200,200);
        frame.add(image_label);


        JPanel panel = new JPanel(new BorderLayout());
        final JList<String> jlist = new JList<>(data.toArray(new String[data.size()]));
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlist.setFont(new Font("Serif", Font.ITALIC, 14));
        JScrollPane listScroller = new JScrollPane();
        listScroller.setViewportView(jlist);
        jlist.setLayoutOrientation(JList.VERTICAL);
        jlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Image image = null;
                try {
                    //String link="https://firebasestorage.googleapis.com/v0/b/home-55fa7.appspot.com/o/Sun%20Jan%2029%202023%2015%3A34%3A48%20GMT%2B0530%20(India%20Standard%20Time)-base64?alt=media&token=a1906684-b1a0-4a04-a7aa-1f07fbd9a487";
                    String linki=link.get(e.getFirstIndex());
                    URL url = new URL(linki);
                    image = ImageIO.read(url);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                image_label.setIcon(new ImageIcon(image));
            }
        });

        /*
        jlist.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    int position = list.locationToIndex(evt.getPoint()) + 1;
                    frame.dispose();
                    showUser(position);
                }
            }
        });
        */

        panel.add(listScroller);
        panel.setBounds(250,70,250,300);
        frame.add(panel);

        JButton back=new JButton("Back");
        back.setBounds(100,400,100,20);
        frame.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                callJFrame_server_page2();
            }
        });

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        if(!link.isEmpty()){
            Image image = null;
            try {
                String linki=link.get(0);
                URL url = new URL(linki);
                image = ImageIO.read(url);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            image_label.setIcon(new ImageIcon(image));
        }
    }
}