package components.view;


import cn.hutool.core.io.BufferUtil;
import cn.hutool.json.JSONUtil;
import components.controller.ClickController;
import components.model.*;
import components.myOptionPane.MyLabel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent {
    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */
    private static final int CHESSBOARD_SIZE = 8;

    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentPlayer = ChessColor.BLACK;
    //all chessComponents in this chessboard are shared only one components.model components.controller
    private final ClickController clickController = new ClickController(this);
    private int chessSize;

    private boolean end=false;
    private ChessColor winner;
    private final ChessGameFrame chessGameFrame;

    public Chessboard(ChessGameFrame chessGameFrame, int width, int height) {
        this.chessGameFrame = chessGameFrame;   //放入游戏框架
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        chessSize = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, chessSize);

        initiateEmptyChessboard();


        //initChessPiece();
    }

    /**
     * 初始化棋子
     */
    public void initChessPiece() {
        List<ChessPiece> allChessPiece = ChessPiece.getAllChessPiece();
        for (ChessPiece piece : allChessPiece) {
            initComponentOnBoard(piece.getKind(), piece.getRow(), piece.getCol(), piece.getColor());
        }
    }//shuru

    /**
     * 重置棋盘
     */
    int i=300;
    public void resetChessboard() {
        Color[]temp=chessComponents[0][0].getBackgroundColor();
        currentPlayer = ChessColor.WHITE;
        MyLabel.getPlayerInfoLabel().setText("current player: " + currentPlayer);
//        chessGameFrame.setTimeLabelText("time remaining: " + i);
        initiateEmptyChessboard();
        initChessPiece();
        for (ChessComponent[] chessComponents1:chessComponents){
            for (ChessComponent component:chessComponents1){
                component.setBackgroundColors(temp);
            }
        }
        repaint();
        sendServer();
    }
    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ChessColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().x(), col = chessComponent.getChessboardPoint().y();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.


        //吃棋
        if (!(chess2 instanceof EmptySlotComponent)) {
            //获胜条件：吃掉对方的王
            if(chess2 instanceof KingChessComponent){
                end=true;
                winner= currentPlayer;
            }
            Color[]temp=chess2.getBackgroundColor();
            remove(chess2);

            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, chessSize,this));
            chess2.setBackgroundColors(temp);
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().x(), col1 = chess1.getChessboardPoint().y();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().x(), col2 = chess2.getChessboardPoint().y();
        chessComponents[row2][col2] = chess2;

        chess1.repaint();
        chess2.repaint();

        if (end){
            //模态框
            JDialog dialog = new JDialog(chessGameFrame, "游戏结束", true);
            dialog.setSize(300, 100);
            dialog.setLocationRelativeTo(null); // Center the window.
            dialog.setLayout(new FlowLayout()); //流式布局
            JLabel label = new JLabel("赢家是：" + winner);
            JButton exitBtn = new JButton("退出关闭");
            exitBtn.addActionListener((e) -> System.exit(0));
            JButton resetBtn = new JButton("重置棋盘");
            resetBtn.addActionListener((e) -> {dialog.dispose();resetChessboard();end=false;});
            dialog.add(label);
            dialog.add(exitBtn);
            dialog.add(resetBtn);
            dialog.setVisible(true);    //使用模态方式这个一定要在最后，否则添加的组件不显示
        }
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, chessSize,this));
            }
        }
    }

    public void swapColor() {
        currentPlayer = currentPlayer == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
        MyLabel.getPlayerInfoLabel().setText("current player: " + currentPlayer); //Task1-1：显示黑白方回合状态
    }

    public void initComponentOnBoard(int kind,int row, int col, ChessColor color) {
        ChessComponent chessComponent = switch (kind) {
            case 1 ->
                    new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, chessSize,this);
            case 2 ->
                    new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, chessSize,this);
            case 3 ->
                    new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, chessSize,this);
            case 4 ->
                    new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, chessSize,this);
            case 5 ->
                    new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, chessSize,this);
            case 6 ->
                    new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, chessSize,this);
            default ->
                    new EmptySlotComponent(new ChessboardPoint(row,col), calculatePoint(row, col), clickController, chessSize,this);
        };
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }


    private Point calculatePoint(int row, int col) {
        return new Point(col * chessSize, row * chessSize);
    }

    public void loadGame(List<String> chessData) {
        chessData.forEach(System.out::println);
    }

    public void resizedAfter() {
        chessSize = getSize().width / 8; //棋子大小

        //遍历设置所有棋子位置和大小
        Component[] components = getComponents();
        for (Component item : components) {
            ChessComponent c = (ChessComponent) item;
            ChessboardPoint p = c.getChessboardPoint();
            c.setLocation(calculatePoint(p.x(), p.y()));
            c.setSize(chessSize,chessSize);
        }
    }

    public ChessGameFrame getChessGameFrame() {
        return chessGameFrame;
    }

    /**
     * 修改棋子
     */
    public void modifyPiece(List<ChessPiece> chessPieces) {
        Color[] presentColor=chessComponents[0][0].getBackgroundColor();
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                int finalI = i;
                int finalJ = j;
                //查找当前坐标有没有棋子
                ChessPiece piece = chessPieces.stream()
                        .filter(item -> item.getRow() == finalI && item.getCol() == finalJ)
                        .findFirst()
                        .orElse(null);
                if (piece != null) {    //有就放棋子
                    initComponentOnBoard(piece.getKind(), piece.getRow(), piece.getCol(), piece.getColor());
                    //回显选中
                    Boolean selected = piece.getSelected();
                    if (selected != null) {
                        chessComponents[piece.getRow()][piece.getCol()].setSelected(selected);
                    }
                } else {    //没有就放空格
                    putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, chessSize,this));
                }
                chessComponents[i][j].setBackgroundColors(presentColor);
            }
        }
        repaint();
    }

    public void setBackgroundColor(Color[] backgroundColor) {
        for(int i=0;i<8;i++){
            for (int j=0;j<8;j++) {
                chessComponents[i][j].setBackgroundColors(backgroundColor);
            }
        }
        this.repaint();
    }


    public void sendServer() {
        java.util.List<ChessPiece> chessPieceList = ChessPiece.getListByComponent(getChessComponents()); //封装list
        Map<String, Object> map = new HashMap<>();
        map.put("currentColor", getCurrentPlayer());  //当前行棋颜色
        map.put("chessPieces", chessPieceList);  //所有棋子
        //转成json
        String json = JSONUtil.toJsonStr(map);
        //写入服务端
        getChessGameFrame().getClient().write(BufferUtil.createUtf8(json));
    }
}
