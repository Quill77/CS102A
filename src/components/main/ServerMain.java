package components.main;

import cn.hutool.core.io.BufferUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.socket.aio.AioServer;
import cn.hutool.socket.aio.AioSession;
import cn.hutool.socket.aio.SimpleIoAction;
import components.model.ChessColor;
import components.model.ChessPiece;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class ServerMain {

    static Map<AioSession,ChessColor> sessionMap = new HashMap<>();
    static ChessColor clientColor=ChessColor.NONE;
//    static Integer colorInt=0;
    static String json1,json2;
    static {
//        colorInt++;
//        switch (colorInt){
//            case 0->clientColor=ChessColor.WHITE;
//            case 1->clientColor=ChessColor.BLACK;
//            default -> clientColor=ChessColor.NONE;
//        }
        //联网游戏初始化棋盘
        java.util.List<ChessPiece> chessPieceList = ChessPiece.getAllChessPiece();  //所有棋子
        Map<String, Object> map = new HashMap<>();
        map.put("currentColor", ChessColor.BLACK);  //当前行棋颜色
        map.put("chessPieces", chessPieceList);  //所有棋子
        map.put("clientColor",clientColor);
        //转成json
        json1 = JSONUtil.toJsonStr(map);
    }

    public static void main(String[] args) {
        //创建服务端 async input output
        AioServer aioServer = new AioServer(8899);
        aioServer.setIoAction(new SimpleIoAction() {
            @Override
            public void accept(AioSession session) {    //每当有客户端连接时调用
                if (session.isOpen()){
//                sessionMap.put(session);   //添加会话
                    JSONObject obj = JSONUtil.parseObj(json1);
                    if (!sessionMap.containsValue(ChessColor.WHITE)) {  //黑方
                        obj.set("clientColor", ChessColor.WHITE);
                        sessionMap.put(session,ChessColor.WHITE);
                    } else if (!sessionMap.containsValue(ChessColor.BLACK)) {   //白方
                        obj.set("clientColor", ChessColor.BLACK);
                        sessionMap.put(session,ChessColor.BLACK);
                    } else {
                        obj.set("clientColor",ChessColor.NONE);
                        sessionMap.put(session,ChessColor.NONE);
                    }
                    session.write(BufferUtil.createUtf8(obj.toString())); //返回全局json信息
                }
            }
            @Override
            public void doAction(AioSession session, ByteBuffer data) {//每当有客户端write时调用
                if (session.isOpen()){
                    if (data.hasRemaining()) {
                        json1 = StrUtil.utf8Str(data);   //替换全局
                        session.read(); //读取客户端数据
                        for (AioSession s : sessionMap.keySet()) {
                            if (s != session) {
                                s.write(BufferUtil.createUtf8(json1)); //向其他客户端返回
                            }
                        }
                    }
                }else {
                    session.close();
                    sessionMap.remove(session);
                }

            }

        }).start(true);

    }

//    int i=60;
//
//    {int delay=1000;    //时间间隔，单位为毫秒
//        ActionListener taskPerformer=new ActionListener()
//        {
//            public void actionPerformed(ActionEvent evt)
//            {
//                if (sessionList.size()>=2){
//                    for (AioSession s : sessionList) {
//                        s.write(BufferUtil.createUtf8(String.valueOf(i))); //向其他客户端返回
//                        i--;
//                    }
//                }
//            }
//        };
//        new Timer(delay,taskPerformer).start();
//    }
}
