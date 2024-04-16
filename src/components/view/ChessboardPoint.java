package components.view;

public record ChessboardPoint(int x, int y) {
    public ChessboardPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ") on the chessboard is clicked!";
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }
}
