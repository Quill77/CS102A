package components.main;

import components.myOptionPane.MyButton;
import components.myOptionPane.MyLabel;
import components.view.Chessboard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String uid = args[0];
        JFrame mainFrame = new JFrame();

        mainFrame.setLayout(null);
        mainFrame.setSize(1000, 760);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MyLabel.addTitleLabel(mainFrame);
        MyButton.addOutlineGameButton(mainFrame);
        MyButton.addOnlineGameButton(mainFrame);
        MyButton.addLeaveButton(mainFrame);
        MyLabel.addPictureLabel(mainFrame);
        Component[] components = mainFrame.getContentPane().getComponents();
        List<Component> componentList = Arrays.stream(components)
                .filter(item -> !(item instanceof Chessboard))
                .toList();

        componentList.get(0).setSize(500, 300);
        componentList.get(0).setLocation(300, 0);
        componentList.get(0).setVisible(true);
        for (int i = 1; i < componentList.size() - 1; i++) {
            Component c = componentList.get(i);
            c.setSize(200, 70);
            c.setLocation(-200 + 300 * i, 600);
            c.setVisible(true);
            c.setFocusable(true);
        }
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }
}
