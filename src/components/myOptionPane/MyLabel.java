package components.myOptionPane;

import components.picture.BackgroundPicture;
import components.view.Chessboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyLabel {
    /**
     * 在游戏面板中添加标签
     */
    private static final JLabel playerInfoLabel = new JLabel();
    public static void addPlayerInfoLabel(Chessboard chessboard, JFrame frame) {
        playerInfoLabel.setText("current player: " + chessboard.getCurrentPlayer());
//        playerInfoLabel.setLocation(HEIGTH - 40, HEIGTH / 10);
        playerInfoLabel.setSize(200, 40);
        Font f=new Font("Rockwell", Font.BOLD, 20);

        playerInfoLabel.setFont(f);
//        playerInfoLabel.setForeground(Color.WHITE);
        frame.add(playerInfoLabel);
    }
    public static JLabel getPlayerInfoLabel() {
        return playerInfoLabel;
    }


    public static JLabel BGPLabel;//背景图片容器
    private static ImageIcon imageIcon;//背景图片转icon
    /**
     * 添加背景图片
     */
    public static void addBGP(JFrame frame) throws IOException {
        imageIcon = BackgroundPicture.getImageIcon(3);
        BGPLabel=new JLabel(imageIcon);
        BGPLabel.setBounds(0,0,frame.getWidth(),frame.getHeight());
        BGPLabel.setVisible(true);
        frame.add(BGPLabel);
    }


    public static void addPictureLabel(JFrame frame) throws IOException {
        ImageIcon i=new ImageIcon(ImageIO.read(new File("src/assets/images/bgp1.png")));
        JLabel label=new JLabel(i);
        frame.add(label);
        label.setSize(frame.getSize());
        label.setLocation(0,0);
    }

    public static void addTitleLabel(JFrame frame){
        JLabel label=new JLabel();

        frame.add(label);
        Font f=new Font("Rockwell", Font.BOLD, 60);
        label.setFont(f);
        label.setForeground(Color.WHITE.darker());
        label.setText("CS102A Chess");
        label.setLocation(frame.getX()/2,80);
        label.setVisible(true);
    }
}
