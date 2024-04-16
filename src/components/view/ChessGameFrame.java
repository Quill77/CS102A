package components.view;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.socket.aio.AioClient;
import cn.hutool.socket.aio.AioSession;
import cn.hutool.socket.aio.SimpleIoAction;

import components.model.ChessColor;
import components.model.ChessPiece;
import assets.music.Bgm;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static components.myOptionPane.MyButton.*;
import static components.myOptionPane.MyLabel.*;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private int WIDTH;
    private int HEIGTH;
    public int chessboardSize;


    public Chessboard getChessboard() {
        return chessboard;
    }

    private Chessboard chessboard;
//    private JLabel timeLabel;

    private AioClient client;   //异步io客户端
    private ChessColor clientColor; //客户端颜色：黑方，白方，null就是观战者
    private final Integer gameMode;


    public ChessGameFrame(int width, int height, int mode) throws IOException {
        setTitle("2022 CS102A Project Chess Demo"); //设置标题

        this.WIDTH = width;
        this.HEIGTH = height;
        this.chessboardSize = HEIGTH * 4 / 5;
        this.gameMode =mode;


        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        addChessboard();

//        addTimeLabel();
        addPlayerInfoLabel(chessboard,this);
        addResetButton(chessboard,this);  //重置按钮
        addChangeBgmButton(this);
        addChangeChessboardColorButton(chessboard,this);
        addWriteButton(chessboard ,this);   //写入存档
        addReadButton(chessboard,this);    //读取存档
        addBGP(this);

        if (gameMode ==1){
            connectServer();    //连接服务端
        }
        if (gameMode ==2){
            chessboard.initiateEmptyChessboard();
            chessboard.initChessPiece();
        }
        addComponentListener(new ComponentAdapter() {//缩放窗口时重新渲染
            @Override
            public void componentResized(ComponentEvent e) {
                resizedAfter();
            }
        });

        Bgm.play(String.valueOf(1)); //播放bgm


    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboard = new Chessboard(this, chessboardSize, chessboardSize);
//        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        add(chessboard);
    }

    public Integer getGameMode() {
        return gameMode;
    }

    /**
     * 缩放窗口重新渲染
     */
    public void resizedAfter() {
        Dimension size = getSize();
        HEIGTH = size.height;
        WIDTH = size.width;

        chessboardSize = HEIGTH * 4 / 5;

        //设置棋盘大小
        chessboard.setLocation((int) (WIDTH/2.6-chessboardSize/2),HEIGTH/2-chessboardSize/2);
        chessboard.setSize(chessboardSize,chessboardSize);
        chessboard.resizedAfter();

        //遍历设置标签和按钮位置
        Component[] components = getContentPane().getComponents();
        List<Component> componentList = Arrays.stream(components)
                .filter(item -> !(item instanceof Chessboard))
                .toList();
        BGPLabel.setBounds(0,0,WIDTH,HEIGTH);

        for (int i = 0; i < componentList.size()-1; i++) {
            Component c = componentList.get(i);
            c.setSize((int) (WIDTH/5.5), HEIGTH/15);
            c.setLocation((int) (WIDTH/1.4), (int) (HEIGTH/8.3*(i+0.75)));
        }
    }
    public AioClient getClient() {
        return client;
    }
    public ChessColor getClientColor() {
        return clientColor;
    }
    public void setClientColor(ChessColor clientColor) {
        this.clientColor = clientColor;
    }
    /**
     * 连接服务端
     */
    private void connectServer() {
        this.client = new AioClient(new InetSocketAddress("127.0.0.1", 8899), new SimpleIoAction() {
            @Override
            public void doAction(AioSession session, ByteBuffer data) {
                if(data.hasRemaining()) {
                    String json = StrUtil.utf8Str(data);
                    JSONObject obj = JSONUtil.parseObj(json);
                    List<ChessPiece> chessPieces = ((JSONArray) obj.get("chessPieces")).toList(ChessPiece.class);
                    String currentColor = (String) obj.get("currentColor");
                    String clientColor = (String) obj.get("clientColor");
                    String time = (String) obj.get("time");
                    //设置档案客户端颜色
                    if (clientColor != null) {
                        setClientColor(ChessColor.valueOf(clientColor));
                        System.err.println("客户端颜色：" + getClientColor());
                    }
                    //修改棋子
                    if (chessboard!=null){
                        chessboard.modifyPiece(chessPieces);
                    }
                    if (currentColor!=null){
                        //设置当前行棋方
                        if (!StrUtil.equalsIgnoreCase(currentColor, chessboard.getCurrentPlayer().getName())) {
                            chessboard.swapColor();
                        }
                    }
                    session.read();
                }
            }
        }).read();
    }

}
