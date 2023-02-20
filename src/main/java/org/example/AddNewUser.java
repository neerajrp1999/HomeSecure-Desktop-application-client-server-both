package org.example;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import recognition.CaptureProfileImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddNewUser {
    firebasedb db=firebasedb.getInstance();;
    JLabel l;
    int id;
    boolean face=false;

    public void imageSet(){
        BufferedImage bufImage = null;
        try {
            bufImage = getImage1();
            face=true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        l.setIcon(new ImageIcon(bufImage));
    }
    JTextField tname,tpass;

    public void showUser() {
        id=db.getNewID()+1;
        //id=1;
        JFrame frame = new JFrame("User");
        frame.setLayout(null);
        frame.setSize(550,500);
        BufferedImage bufImage = null;
        try {
            bufImage = getImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        l=new JLabel(new ImageIcon(bufImage));
        l.setBounds(10,125,200,200);
        frame.add(l);
        l.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    System.out.println("double clicked");
                    new CaptureProfileImage().call(AddNewUser.this,id);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JLabel l2=new JLabel("ID    :   "+id);
        l2.setBounds(220,130,100,20);
        frame.add(l2);

        JLabel l3=new JLabel("Name  :   ");
        l3.setBounds(220,187,100,20);
        frame.add(l3);

        tname=new JFormattedTextField();
        tname.setBounds(290,187,70,20);
        frame.add(tname);

        JLabel l4=new JLabel("Password:");
        l4.setBounds(220,245,70,20);
        frame.add(l4);

        tpass=new JFormattedTextField();
        tpass.setBounds(290,245,70,20);
        frame.add(tpass);

        JLabel err=new JLabel();
        err.setBounds(220,360,200,20);
        frame.add(err);

        JButton add=new JButton("Start Scan ");
        add.setBounds(220,340,100,20);
        frame.add(add);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name_s=tname.getText().toString().trim();
                String pass_s=tpass.getText().toString().trim();
                if(name_s.length()<=0){
                    err.setText("Write a proper name");
                    return;
                }
                if(pass_s.length()<=0){
                    err.setText("Write a proper password");
                    return;
                }
                if(!face){
                    err.setText("Upload Profile Pic first.....");
                    return;
                }
                int result = JOptionPane.showConfirmDialog(null,"Sure? You Want to Add this user?", "ADD", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    frame.dispose();
                    new addNewFaceInData(id,name_s,pass_s).mainCaller();

                    /*
                    JOptionPane pane = new JOptionPane("Yes !!");
                    JOptionPane.showMessageDialog(pane, "My Goodness, this is so concise");
                     */
                }
            }
        });

        JButton back=new JButton("Back");
        back.setBounds(340,340,100,20);
        frame.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new server().callJFrame_server_page2();
            }
        });

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    public static BufferedImage getImage1() throws IOException{
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        String file = "src\\resources\\TempPhotos\\temp.jpg";
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
    public static BufferedImage getImage() throws IOException{
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        String file = "src\\resources\\TempPhotos\\person.jpg";
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

    public static void main(String[] args) {
        new AddNewUser().showUser();
    }
}