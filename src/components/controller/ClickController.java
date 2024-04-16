package components.controller;


import components.model.ChessComponent;
import assets.music.Bgm;
import components.view.Chessboard;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public void onClick(ChessComponent chessComponent) {

        if (first == null) {
            if (handleFirst(chessComponent)) {
                for (ChessComponent[] components:chessboard.getChessComponents()){
                    for (ChessComponent component:components){
                        if(chessComponent.canMoveTo(chessboard.getChessComponents(),component.getChessboardPoint())){
                            component.setBackgroundColor();
                            component.repaint();
                        }
                    }
                }

                
                chessComponent.setSelected(true);
                first = chessComponent;
                first.repaint();
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取

                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
                for (ChessComponent[] components:chessboard.getChessComponents()){
                    for (ChessComponent component:components){
                        component.deleteBackgroundColor();
                        component.repaint();
                    }
                }
            } else if (handleSecond(chessComponent)) {

                Bgm.hit();
                //repaint in swap chess method.
                chessboard.swapColor();
                chessboard.swapChessComponents(first, chessComponent);
                for (ChessComponent[] chessComponents:chessboard.getChessComponents()){
                    for (ChessComponent Component:chessComponents){
                        Component.setBackgroundColors(first.getTemp());
                    }
                }
                chessboard.repaint();
                first.setSelected(false);
                first = null;
                for (ChessComponent[] components:chessboard.getChessComponents()){
                    for (ChessComponent component:components){
                        component.deleteBackgroundColor();
                        component.repaint();
                    }
                }
            }else if (chessComponent.getChessColor()==chessboard.getCurrentPlayer()){
                chessComponent.setSelected(true);
                ChessComponent recordFirst = first;
                first=chessComponent;
                first.repaint();
                recordFirst.setSelected(false);
                recordFirst.repaint();
                for (ChessComponent[] components:chessboard.getChessComponents()){
                    for (ChessComponent component:components){
                        component.deleteBackgroundColor();
                        if(chessComponent.canMoveTo(chessboard.getChessComponents(),component.getChessboardPoint())){
                            component.setBackgroundColor();
                        }
                        component.repaint();
                    }
                }
            }

        }
        if (chessboard.getChessGameFrame().getGameMode()==1){
            chessboard.sendServer();
        }

    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentPlayer()&&
                (chessboard.getChessGameFrame().getGameMode()!=1||
                chessComponent.getChessColor()!=chessboard.getChessGameFrame().getClientColor());   //加个判断是否客户端颜色正确黑方白方;
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        return chessComponent.getChessColor() != chessboard.getCurrentPlayer() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint());
    }


}
