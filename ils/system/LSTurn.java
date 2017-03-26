/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import ils.system.camerasensor.CameraOutputAnalyzer;
import ils.system.camerasensor.CameraSensorClient;
import ils.system.speedway.*;
import ils.system.humansensor.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wataru-T
 */
public class LSTurn {
    //各LSを起動させてデータを取らせるためのクラス
    Properties properties = new Properties();

    ////List<DataBufferofSpeedway> speedway_db = new ArrayList<>();    
    ////List<DataBufferofSensor> humansensor_db = new ArrayList<>();    
    ////DataBufferofSensor camerasensor_db = new DataBufferofSensor();  
    
    List<Databuf> speedway_buf = new ArrayList<>();
    List<Databuf> humansensor_buf = new ArrayList<>();
    Databuf camerasensor_buf = new Databuf();
    
    ReadTagReport rtr;
    //HumanSensorMain hsm;
    HumanSensorClient hsc;
    CameraSensorClient csc;
            
    public void startupSpeedway(){
        //Speedwayを起動するメソッド
        //short[] enableportnumlist = {1,2,3,4}; //Speedwayで使用するアンテナのポート番号のリスト
        //Properties properties = new Properties();
        short[] enableportnumlist = properties.enableportnumlist;
        
        for(int i=0;i<enableportnumlist.length;i++){
            
            ////speedway_db.add(new DataBufferofSpeedway()); //使うアンテナの数だけDataBufferofSpeedwayを追加
            
            speedway_buf.add(new Databuf());
        }
        
        rtr = new ReadTagReport();
        ////rtr.Setting(speedway_db,enableportnumlist);    //speedwayとの接続確立、読み取りを準備させる
        
        rtr.Setting(speedway_buf,enableportnumlist);    //speedwayとの接続確立、読み取りを準備させる
    }

    public void startupHumanSensor(int humansensor_num){
        //HumanSensor(人感センサ)の起動の準備をするメソッド
        
        for(int i=0;i<humansensor_num;i++){
            ////humansensor_db.add(new DataBufferofSensor());
            humansensor_buf.add(new Databuf());            
        }
        
        //先にサーバー側でServer.javaを実行しておく、、
        hsc = new HumanSensorClient();
        
    }
    
    public void startupCameraSensor(){
        //CameraSensor(カメラセンサ)の起動準備をするメソッド
        csc = new CameraSensorClient();
        
        //1.ラズパイでmotionを動かす（カメラが繋がっている状況で）
        //2.ラズパイからscreenでカメラにつなぐ
        //3.motion9 -i on
        //4.screenを殺す、(Ctrl-a k)
        //5.mkfifo filename  (Mac上で)
        //6.nc -l 5555(ポート番号) > filename （Mac上で）
        //7.cat filename  (Mac上で、5.とは別ターミナルで)
        //8.cat /dev/ttyUSB0 | nc MacのIPアドレス 5555(ポート番号)  (ラズパイ上で)
        
        //↑の1.~4.までやってこのプロジェクト実行すれば良い
        
    }            
    
    public void allStartup(int humansensor_num){
        //すべての機器において読み取りの準備        
        
        startupSpeedway(); //Speedwayの起動準備
        
        startupHumanSensor(humansensor_num); //人感センサの起動準備
        
        startupCameraSensor();//カメラセンサの起動準備
                
        //SMKの起動準備        
        
    }    
     
    //LSにデータ読み取らせる
    //Speedway
    public void readingSpeedway(long ms){
        //(引数[ミリ秒])間、SpeedWayによりデータを計測する  
        
        //System.out.print("speedway_db_mae:");
        //for(int i=0;i<speedway_db.size();i++){
        //    System.out.print(speedway_db.get(i).eachdistance);
        //}
        //System.out.println("");
        
        rtr.Reading(ms);
        
        //System.out.print("speedway_db_ato:");
        //for(int i=0;i<speedway_db.size();i++){
        //    System.out.print(speedway_db.get(i).eachdistance);
        //}
        //System.out.println("");        
    }
    
    //人感センサ
    public void readingHumanSensor(long ms){
        //(引数[ミリ秒])間、人感センサにより計測
        //hsm.Reading(humansensor_db,ms);   
        List<Boolean> SensorResult = new ArrayList<>();
        
        //System.out.print("HumanSensor_db_mae:");
        //for(int i=0;i<humansensor_db.size();i++){
        //    System.out.print(humansensor_db.get(i).detect);
        //}
        //System.out.println("");          
        
        for(int h=0;h<properties.raspi.size();h++){
            //String[] arg = {"150.65.231.104","27","18"};//ラズパイのiPアドレス、人感センサの出力GPIO番号
            String[] args = properties.getTwoSensorsValue(h);
            hsc.main(args);
        
            SensorResult.addAll(hsc.getResponse());                
            System.out.println("HumanSensorResult:"+SensorResult);        
        }
        
        for(int i=0;i<SensorResult.size();i++){
            ////DataBufferofSensor dbs = new DataBufferofSensor();
            ////dbs.SetData(SensorResult.get(i));
            ////humansensor_db.set(i, dbs);
            
            Databuf dbs = new Databuf();
            dbs.SetData(Integer.parseInt("001",2),-1,0.0,SensorResult.get(i),1);
            humansensor_buf.set(i, dbs);            
        }        
        
        //System.out.print("HumanSensor_db_ato:");
        //for(int i=0;i<humansensor_db.size();i++){
        //    System.out.print(humansensor_db.get(i).detect);
        //}
        //System.out.println("");            
    }
    
    //カメラ型センサ
    public void readingCameraSensor(long ms){

        //String[] arg = {properties.getRaspi_ip(0),String.valueOf(ms)};
        String[] arg = {"150.65.204.252",String.valueOf(ms)};        
        csc.main(arg);
        
        String cameraoutput = csc.getResponse();
        
        System.out.println("        Camera,output:"+cameraoutput);
        CameraOutputAnalyzer coa = new CameraOutputAnalyzer();
        boolean detect = coa.recognizer(coa.outputanalyze(cameraoutput));

        ////camerasensor_db.SetData(detect);   
        camerasensor_buf.SetData(Integer.parseInt("001",2),-1,0.0,detect,1);         
    }
    
    public void endSpeedway(){    
        rtr.Disconnecting();
    }
    
    public void allEnd(){
        //すべての機器の接続を切る
        
        endSpeedway();  //Speedwayとの接続を切る
        
        //SMKの接続切る
    }                        
}
