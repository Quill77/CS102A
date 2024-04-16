package assets.music;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Bgm {
    private static Clip bgm;//背景乐
    private static Clip hit;//音效
    private static AudioInputStream ais;
    public static void play(String s){
        try {
            bgm= AudioSystem.getClip();
            InputStream is=Bgm.class.getResourceAsStream("bgm"+s+".wav");
            //getclassLoader得到当前类的加载器.getResourceAsStream加载资源，只能加载wav的音乐格式

            if (is != null) {
                ais=AudioSystem.getAudioInputStream(is);//获取输入流
            }
            bgm.open(ais);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        bgm.start();//开始播放
        bgm.loop(Clip.LOOP_CONTINUOUSLY);//循环播放
    }
    public static void stop(){
        if(ais!=null)
            bgm.close();
    }

    public static void hit(){
        try {
            hit= AudioSystem.getClip();
            InputStream is=Bgm.class.getResourceAsStream("hit.wav");
            //getclassLoader得到当前类的加载器.getResourceAsStream加载资源，只能加载wav的音乐格式

            if (is != null) {
                ais=AudioSystem.getAudioInputStream(is);//获取输入流
            }
            hit.open(ais);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        hit.start();//开始播放
    }
}
