package components.model;

import components.view.Chessboard;
import components.view.ChessboardPoint;
import components.controller.ClickController;

import java.awt.*;

/**
 * 这个类表示棋盘上的空位置
 */
public class EmptySlotComponent extends ChessComponent {

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ClickController listener, int size, Chessboard chessboard) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size,chessboard);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    @Override
    public void loadResource() {
        //No resource!
    }

}
