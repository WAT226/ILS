/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system.camerasensor;

import ils.system.humansensor.*;
import ils.system.Properties;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wataru-T
 */
public class CameraSensorClient {//ILS(Mac)側で実行する、サーバー（ラズパイ）に向けて通信し
    
    String res = new String();
    
    //public String Client(String raspi_ip) {//ラズパイのIPアドレス
    public void main(String args[]){//ラズパイのIPアドレス
        res = new String();
        
        try {
            //Properties properties = new Properties();
            
            //String server = "150.65.231.104";//args[0];->ラズパイのIPアドレス
            //String server = properties.get_raspi_ip();
            String server = args[0];
            
            int port = Integer.parseInt("7777");//args[1]); //サーバー側のポート番号
            Socket s = new Socket(server, port);                
                
            // サーバーに数値を送信
            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(Integer.parseInt(args[1]));//args[2]));//サーバーに何かしら送る            
                
            // 演算結果を受信
            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            res = dis.readUTF();
            //System.out.println(res);
            
            // ストリームを閉じる
            dis.close();
            dos.close();
            
        }
        catch (Exception e) {
            System.out.println("CameraSensorClient:Exception: " + e);
        }
    }  
    
    public String getResponse(){
        
        return res;
    }
}
