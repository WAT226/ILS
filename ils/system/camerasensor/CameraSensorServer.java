/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system.camerasensor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Wataru-T
 */
//参考：ttps://www.qoosky.io/techs/f8c35bb5d7
public class CameraSensorServer {//ラズパイ側に置いて、前もって実行させておく
    public static void main(String args[]) {//args[0]:ポート番号        
        try {
            int port = Integer.parseInt("7777");//args[0]); //サーバ側の待受ポート番号
            ServerSocket ss = new ServerSocket(port);

            while(true) {
                Socket s = ss.accept(); //クライアントからの通信開始要求が来るまで待機

                // 以下、クライアントからの要求発生後
                InputStream is = s.getInputStream(); //クライアントから数値を受信
                DataInputStream dis = new DataInputStream(is);
                int req = dis.readInt();
                
                //String s_in = fileToString(new File("/sys/class/gpio/gpio"+req+"/value"));//人感センサの結果が格納されているファイル   

                ExecuteShellScriptandRead shell = new ExecuteShellScriptandRead();
                String serverpath = "/home/pi/ils/serverpath.sh"; //nc -l 5555
                String clientpath = "/home/pi/ils/clientpath.sh"; //cat /dev/ttyUSB0 | nc <MacのIPアドレス> 5555
        
                String cameraoutput = shell.ClientServer(clientpath,serverpath,req);               
                
                
                OutputStream os = s.getOutputStream(); //結果を送信
                DataOutputStream dos = new DataOutputStream(os);
                //dos.writeInt(req*req);
                dos.writeUTF(cameraoutput);

                // ストリームを閉じる
                dos.close();
                dis.close();
            }
        }
        catch(Exception e) {
            System.out.println("Exception: " + e);
        }
    }   
    
  public static String fileToString(File file) throws IOException {
    BufferedReader br = null;
    try {
      // ファイルを読み込むバッファドリーダを作成します。
      br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      // 読み込んだ文字列を保持するストリングバッファを用意します。
      StringBuffer sb = new StringBuffer();
      // ファイルから読み込んだ一文字を保存する変数です。
      int c;
      // ファイルから１文字ずつ読み込み、バッファへ追加します。
      while ((c = br.read()) != -1) {
        sb.append((char) c);
      }
      // バッファの内容を文字列化して返します。
      return sb.toString();
    } finally {
      // リーダを閉じます。
      br.close();
    }
  }    
}
