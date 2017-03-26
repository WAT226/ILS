/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system.camerasensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Wataru-T
 */
public class ExecuteShellScriptandRead {
 
  public String ClientServer(String clientpath,String serverpath,long ms) {
      //サーバー側、シェルスクリプトの実行と出力結果の読み込み
      //ラズパイ側でmotion起動
      //カメラ側でmotion9 -i onコマンド打つ → 速度出力されてるのを確認したらCtrl-a k でscreen殺すこと、そうしないと動かない
    try {

      Process serverprocess = new ProcessBuilder("sh",serverpath).start(); 
      Process clientprocess = new ProcessBuilder("sh",clientpath).start();      
      String text;
      //System.out.println("server.1.serverpath:"+serverprocess.isAlive()+" clientpath:"+clientprocess.isAlive());      
      InputStream is = serverprocess.getInputStream();
      InputStreamReader isr = new InputStreamReader(is, "UTF-8");    
      BufferedReader reader = new BufferedReader(isr);
      StringBuilder builder = new StringBuilder();
      StringBuilder builder2 = new StringBuilder();
      int c;
      int i=0;
      long start = System.currentTimeMillis();
      //System.out.println("server.2.serverpath:"+serverprocess.isAlive()+" clientpath:"+clientprocess.isAlive());
      while ((c = reader.read()) != -1) {
        if(c == 10){          //c==10 のときが改行  
            builder.append((char)c);            
            builder2 = new StringBuilder();
            builder2.append(builder.toString());            
            builder = new StringBuilder();  
            //System.out.println("server.3.serverpath:"+serverprocess.isAlive()+" clientpath:"+clientprocess.isAlive());  
            //System.out.println(builder.toString());
            //if(i++ > 2) break;
            long end = System.currentTimeMillis();
            if(end - start > ms) break;
        }else{        
            builder.append((char)c);
            //System.out.print(builder.toString());
        }
      }
      //System.out.println("server.4.dasshutu"+serverprocess.isAlive()+" clientpath:"+clientprocess.isAlive());
      serverprocess.destroy();
      clientprocess.destroy();  //クライアント、サーバー終了
      
      // 実行結果を格納
      text = builder2.toString();
      int ret = serverprocess.waitFor();
      //System.out.println("server.3");
      //System.out.println(text);
      //System.out.println("  ret:"+ret);

      reader.close();
      isr.close();
      is.close();
      return text;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      String error = "E";
      return error;
    }      
  }   
  /*
    public void Client(String shellscriptpath){
        //クライアント側、シェルスクリプトの実行だけ
        try {
            Process process = new ProcessBuilder("sh",shellscriptpath).start();      
            String text;
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            //BufferedReader reader = new BufferedReader(isr);
            //StringBuilder builder = new StringBuilder();
            //StringBuilder builder2 = new StringBuilder();
            //int c;
            /*
            while ((c = reader.read()) != -1) {
                if(c == 10){                   
                    builder2 = new StringBuilder();
                    builder2.append(builder.toString());            
                    builder = new StringBuilder();  
                }
        
            //c==10 のときが改行
            builder.append((char)c);
            }
            */
            /*
            System.out.println("client.1");
            Thread.sleep(5000);
            System.out.println("client.2");
            process.destroy();
            System.out.println("client.3");
        // 実行結果を格納
        //text = builder.toString();
        //int ret = process.waitFor();

        //System.out.println(text);
        //System.out.println(ret);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(ExecuteShellScriptandRead.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }    
    
  public void Server(String shellscriptpath) {
      //サーバー側、シェルスクリプトの実行と出力結果の読み込み
    try {

      //Process process = new ProcessBuilder("sh", "/home/pi/shellScript.sh").start();
      //Process process = new ProcessBuilder("sh","/Users/Wataru-T/Desktop/Java_writedata/shellScript.sh").start();
      Process process = new ProcessBuilder("sh",shellscriptpath).start();      
      String text;
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is, "UTF-8");
      BufferedReader reader = new BufferedReader(isr);
      StringBuilder builder = new StringBuilder();
      StringBuilder builder2 = new StringBuilder();
      int c;
      System.out.println("server.1");
      Thread.sleep(5000);
      System.out.println("server.2");
      while ((c = reader.read()) != -1) {
        if(c == 10){                   
            builder2 = new StringBuilder();
            builder2.append(builder.toString());            
            builder = new StringBuilder();  
        }
        
        //c==10 のときが改行
        builder.append((char)c);
      }
      // 実行結果を格納
      text = builder.toString();
      int ret = process.waitFor();
      System.out.println("server.3");
      System.out.println(text);
      System.out.println(ret);

      reader.close();
      isr.close();
      is.close();
      process.destroy();
      System.out.println("server.4");
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }        
      */
}
