package components.main;

import components.view.ChessGameFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame {
    public MainFrame(int mode) {
        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame;
            try {
                mainFrame = new ChessGameFrame(1000, 760,mode);
                mainFrame.setMinimumSize(new Dimension (500,400));
                mainFrame.setVisible(true);
            } catch (IOException ignored) {}
        });
    }
}
