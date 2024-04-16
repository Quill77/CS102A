package components.myOptionPane;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import components.main.MainFrame;
import components.model.ChessColor;
import components.model.ChessComponent;
import components.model.ChessPiece;
import components.model.ChessboardColor;
import assets.music.Bgm;
import components.view.Chessboard;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyButton {
    /**
     * 重置按钮
     */
    public static void addResetButton(Chessboard chessboard, JFrame frame) {
        JButton button = new JButton("Reset Chessboard");
//        button.setLocation(HEIGTH - 40, HEIGTH / 10 + 250);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> chessboard.resetChessboard());
        frame.add(button);
    }

    /**
     * 改变bgm
     */
    public static void addChangeBgmButton(JFrame frame) {
        final int[] bgm = {1};
        JButton button = new JButton("ChangeBgm");
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        frame.add(button);
        button.addActionListener(e -> {
            Bgm.stop();
            bgm[0]++;
            if (bgm[0] >6){
                bgm[0] -=6;
            }
            Bgm.play(String.valueOf(bgm[0]));
        });
    }

    /**
     * 改变棋盘颜色
     */
    public static void addChangeChessboardColorButton(Chessboard chessboard, JFrame frame) {
        JButton button = new JButton("change board color");
//        button.setLocation(HEIGTH - 40, HEIGTH / 10 + 150);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.setVisible(true);
        frame.add(button);
        button.addActionListener(e->{
            JDialog dialog = new JDialog(frame, "change chessboard color", true);
            dialog.setSize(500, 600);
            dialog.setLocationRelativeTo(null); // Center the window.
            dialog.setLayout(null); //流式布局

            JButton[] buttons = new JButton[4];
            for (int i=0;i<4;i++) {
                buttons[i]=new JButton("choose this color");
                buttons[i].setBackground(ChessboardColor.getColor(i)[1]);
                buttons[i].setLocation(100, 100+100*i);
                buttons[i].setSize(300, 70);
                buttons[i].setFont(new Font("Rockwell", Font.BOLD, 20));
                int finalI = i;
                buttons[i].addActionListener(e1 -> {
                    chessboard.setBackgroundColor(ChessboardColor.getColor(finalI));
                    for (ChessComponent[] chessComponents:chessboard.getChessComponents()){
                        for (ChessComponent chessComponent:chessComponents){
                             chessComponent.setBackgroundColors(ChessboardColor.getColor(finalI));
                        }
                    }
                });
                buttons[i].setVisible(true);
                dialog.add(buttons[i]);

            }
            dialog.setVisible(true);    //使用模态方式这个一定要在最后，否则添加的组件不显示
        });


//        button.addActionListener((e) -> {

//            }


//            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            jFrame.setVisible(true);
//        });
//        add(button);
    }
//
//    public static JLabel BGPLabel;//背景图片容器
//    private static ImageIcon imageIcon;//背景图片转icon
//    /**
//     * 添加背景图片
//     */
//    public static void addBGP(JFrame frame) throws IOException {
//        imageIcon = BackgroundPicture.getImageIcon(3);
//        BGPLabel=new JLabel(imageIcon);
//        BGPLabel.setBounds(0,0,frame.getWidth(),frame.getHeight());
//        BGPLabel.setVisible(true);
//        frame.add(BGPLabel);
//    }

    /**
     * 写入存档按钮
     */
    public static void addWriteButton(Chessboard chessboard,JFrame frame) {
        JButton button = new JButton("write archive");
//        button.setLocation(HEIGTH - 40, HEIGTH / 10 + 375);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            //文件选择
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("国际象棋存档.json")); //选择的文件
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);    //保存类型
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || StrUtil.equals("json", FileUtil.getSuffix(f));
                }

                @Override
                public String getDescription() {
                    return "json";
                }
            });
            chooser.setCurrentDirectory(new File("C:\\Users\\29266\\IdeaProjects\\ChessDemo v1.2\\国际象棋存档"));
            chooser.setSelectedFile(new File("国际象棋存档.json"));
            int res = chooser.showSaveDialog(frame);
            if (res == JFileChooser.APPROVE_OPTION) {   //点击了保存
                File file = chooser.getSelectedFile();  //选择的文件
                //处理数据
                ChessComponent[][] chessComponents = chessboard.getChessComponents();   //所有组件
                List<ChessPiece> chessPieceList = ChessPiece.getListByComponent(chessComponents); //封装list
                Map<String, Object> map = new HashMap<>();
                map.put("currentColor", chessboard.getCurrentPlayer());  //当前行棋颜色
                map.put("chessPieces", chessPieceList);  //所有棋子
                //转成json
                String json = JSONUtil.toJsonStr(map);
                //保存文件
                try {
                    IoUtil.writeUtf8(new FileOutputStream(file), true, json);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "保存存档失败，错误信息：" + ex.getMessage());
                    return;
                }
                JOptionPane.showMessageDialog(frame, "保存存档成功，文件路径：" + file.getAbsolutePath());
            }
        });
        frame.add(button);
    }

    /**
     * 读取存档
     */
    public static void addReadButton(Chessboard chessboard,JFrame frame) {
        JButton button = new JButton("read archive");
//        button.setLocation(HEIGTH - 40, HEIGTH / 10 + 500);
        button.setSize(160, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            //文件选择
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("C:\\Users\\29266\\IdeaProjects\\ChessDemo v1.2\\国际象棋存档"));
            chooser.setSelectedFile(new File("国际象棋存档.json"));
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return StrUtil.equals("json", FileUtil.getSuffix(f));
                }

                @Override
                public String getDescription() {
                    return "国际象棋存档";
                }
            });
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);    //打开类型
            int res = chooser.showOpenDialog(frame);
            if (res == JFileChooser.APPROVE_OPTION) {   //选择了文件
                File file = chooser.getSelectedFile();
                //(4)	文件格式错误(例如规定是 txt，导入的时候是json)
                if (!StrUtil.equals("json", FileUtil.getSuffix(file))) {
                    JOptionPane.showMessageDialog(frame, "文件格式错误");
                } else {
                    try {
                        //读取解析json
                        String json = IoUtil.read(new FileReader(file));
                        JSONObject obj = JSONUtil.parseObj(json);
                        List<ChessPiece> chessPieces = ((JSONArray) obj.get("chessPieces")).toList(ChessPiece.class);
                        String currentColor = (String) obj.get("currentColor");
                        //(1)	棋盘并非 8*8
                        boolean present = chessPieces.stream()
                                .anyMatch(item -> item.getRow() < 0 || item.getRow() >= 8 ||
                                        item.getCol() < 0 || item.getCol() >= 8);
                        if (present) {
                            JOptionPane.showMessageDialog(frame, "读取存档失败，错误信息：棋盘并非 8*8");
                            return;
                        }
                        //(2)	棋子并非六种之一，棋子并非黑白棋子
                        present = chessPieces.stream()
                                .anyMatch(item -> (item.getKind() < 1 || item.getKind() > 6) ||
                                        (item.getColor() != ChessColor.BLACK && item.getColor() != ChessColor.WHITE));
                        if (present) {
                            JOptionPane.showMessageDialog(frame, "读取存档失败，错误信息：棋子并非六种之一，棋子并非黑白棋子");
                            return;
                        }
                        //(3)	缺少下一步行棋方
                        if (StrUtil.isBlank(currentColor)) {
                            JOptionPane.showMessageDialog(frame, "读取存档失败，错误信息：缺少下一步行棋方");
                            return;
                        }
                        //渲染表盘
                        chessboard.initiateEmptyChessboard();
                        for (ChessPiece piece : chessPieces) {
                            chessboard.initComponentOnBoard(piece.getKind(), piece.getRow(), piece.getCol(), piece.getColor());
                        }
                        chessboard.repaint();
                        //设置行棋方
                        if (!StrUtil.equalsIgnoreCase(currentColor, chessboard.getCurrentPlayer().getName())) {
                            chessboard.swapColor();
                        }
                        chessboard.sendServer();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "读取存档失败，错误信息：" + ex.getMessage());
                    }
                }
            }
        });
        frame.add(button);
    }


    /**
     * 联网游戏
     */
    public static void addOnlineGameButton(JFrame frame){
        JButton button = new JButton();
        frame.add(button);
        button.setSize(300,100);

        button.setText("Online Game");

        Font f=new Font("Rockwell", Font.BOLD, 20);

        button.setFont(f);
        button.setVisible(true);
        button.addActionListener(e -> {
            frame.dispose();
            new MainFrame(1);
        });
    }


    public static void addOutlineGameButton(JFrame frame){
        JButton button = new JButton();
        frame.add(button);
        button.setText("Outline Game");

        Font f=new Font("Rockwell", Font.BOLD, 20);

        button.setFont(f);
        button.setVisible(true);
        button.addActionListener(e -> {
            frame.dispose();
            new MainFrame(2);
        });
    }


    public static void addLeaveButton(JFrame frame){
        JButton button = new JButton();
        frame.add(button);
        button.setText("Leave Game");
        Font f=new Font("Rockwell", Font.BOLD, 20);

        button.setFont(f);
        button.setSize(300,100);
        button.setVisible(true);
        button.addActionListener(e -> {
            frame.dispose();
        });
    }
}
