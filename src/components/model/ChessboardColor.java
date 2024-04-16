package components.model;

import java.awt.*;

public class ChessboardColor {
    private static final Color[][] backGroundColors=new Color[][]{
            new Color[]{Color.BLACK,Color.WHITE},
            new Color[]{Color.getHSBColor(0.25f,0.45f,0.85f),Color.getHSBColor(0.36f,0.45f,0.8f)},
            new Color[]{Color.getHSBColor(0.60f,0.45f,1f),Color.getHSBColor(0.66f,0.45f,1f)},
            new Color[]{Color.getHSBColor(0.2f,0.45f,1f),Color.ORANGE}};

    public static Color[] getColor(int i){
        return i>=0?i<4?backGroundColors[i]:backGroundColors[0] : backGroundColors[0];
    }
}
