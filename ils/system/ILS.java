/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ils.system.humansensor.HumanSensorConfig;
import ils.system.thread.CameraSensorThread;
import ils.system.thread.HumanSensorThread;
import ils.system.thread.SpeedwayThread;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class ILS {
    //システム実行用クラス
    
    //始める前にチェック！！
    //・RoomConfig.jsonを作成しておく、このilsプロジェクトはラズパイではなくMacから動かすのでラズパイに置かなくて良い
    //・プロジェクトのプロパティ＞実行＞VMオプションからSpeedwayのホスト名またはIPアドレスを入力しておく   
    //・ラズパイ側でサーバープログラムを実行させておくこと(ils/humansensor/HumanSensorServer.java)GPIOは18と27
    //・ラズパイの電源を切るとgpio/valueは全て消える事が発覚、再度作っておくこと
        // echo xx > /sys/class/gpio/export
        // cd /sys/class/gpio/gpioxx
        // echo in > direction
        // cat valueで出力数値確認できる
    //・ラズパイでセンサ出力GPIO番号に対しexport、出力結果をvalueにやるよう設定しているか確認
    //・カメラにおいて、ラズパイ内のils/clientpath.sh内に記述されているIPアドレスをラズパイのIPアドレスにしておく（接続場所により変わるので注意） 
    //・Properties.javaでラズパイのアドレスを設定すること
    //・Properties.javaでSpeedwayの使用するアンテナポートを設定
    //・カメラのラズパイのIPアドレスも設定すること
    //・SMKカメラセンサを起動、手順は
        //1.ラズパイでmotionを動かす（カメラが繋がっている状況で）
        //2.ラズパイからscreenでカメラにつなぐ
        //3.motion9 -i on
        //4.screenを殺す、(Ctrl-a k)    
        //5.ils/CameraSensorServer.javaを起動
    
    //ちょっと直したほうがいい？
    //・RoomConfig.jsonのHumanSensorの数とPropertiesのセンサの数は同じである必要がある、、、めんどくないか
    //・Api_lsで読み取ったセンサと実際にセンサのデータを読むところ、この2つで見るセンサはどちらも同じでなければならない
    //・Speedwayのホスト名（プロパティ＞実行＞VMオプション）に注意、ホスト名の最後に改行が入ってると動作しない
    
    public static final int ACCLEVEL_OF_APP = 3; // 提供先アプリケーションが求める位置情報精度レベル、1~6. 
    public static final int CALCULATE_NUM =150;  //LSによる計測回数
    public static final int MEASUREING_TIME = 1000;//[ms]
    public static final int MAX_WAITING_TIME = 1500;//[ms]ネットワーク通信の支障などによりスレッドが終了しない→システム全体が作動しないのを防ぐために、スレッド終了まで待てる時間を設ける
    
    House house = new House();
    
    public void makeHouse(){
        //部屋の設定
        
        //このクラス、メソッド内で書き換え、指定して定義させる形にするか
        //それとも、設定用のテキストファイル（jsonファイル？）を用意して、それを読み込ませるような形にするか  
        //(参考:ttp://blog.kymmt.com/entry/jackson)
        
        //読み込むjsonファイルのパス、名前(ラズパイで実行するならラズパイでのパス)
        String json = "/Users/Wataru-T/Desktop/WTfiles/maintheme/java/ils/src/ils/system/RoomConfig.json"; //Macでの絶対パス
        //String json = "/home/pi/ils/RoomConfig.json";//ラズパイでの絶対パス
        
        //javaでjsonファイルを読み込むライブラリ"Jackson"を使用
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(new File(json));
            
            for(JsonNode n : root.get("Room")){
                int roomid = n.get("Room_id").asInt();
                int floor = n.get("floor").asInt();
                int width = n.get("width").asInt();                
                int depth = n.get("depth").asInt();
                int height= n.get("height").asInt();
                List<LSData> lslist = new ArrayList<>();
                
                for(JsonNode m : n.get("LS")){
                    String name = m.get("name").asText();
                    int ls_id = m.get("LS_id").asInt();
                    int time_acc = m.get("LS_time_acc").asInt();
                    int space_acc = m.get("LS_space_acc").asInt();
                    int ls_acc = m.get("LS_acc").asInt();
                    List<Vector3D> coordinate_LS = new ArrayList<>();
                    
                    List<HumanSensorConfig> config_Sensor = new ArrayList<>();
                    
                    for(JsonNode l : m.get("coordinate")){
                        int x = l.get("x").asInt();
                        int y = l.get("y").asInt();
                        int z = l.get("z").asInt();  
                        Vector3D coordinate = new Vector3D(x,y,z);                        
                        coordinate_LS.add(coordinate);
                        
                        if(l.has("height")){
                            int h = l.get("height").asInt();
                            int ta = l.get("top_angle").asInt();
                            int xa = l.get("x_angle").asInt();  
                            int ya = l.get("y_angle").asInt(); 
                            HumanSensorConfig config;
                            
                            if(name.equals("Camera")){
                                config = new HumanSensorConfig(h,ta,10,xa,ya);                        
                            }else{
                                config = new HumanSensorConfig(h,ta,10,xa,ya);                                 
                            }
                            config_Sensor.add(config);
                        }
                    }

                    for(int i=0;i<coordinate_LS.size();i++){
                    
                        LSData lsdata;
                        if(config_Sensor.size() > 0){
                            //lsdata = new LSData(name,ls_id,time_acc,space_acc,ls_acc,coordinate_LS,config_Sensor);
                            lsdata = new LSData(name,ls_id,time_acc,space_acc,ls_acc,coordinate_LS.get(i),config_Sensor.get(i));
                        }else{
                            //lsdata = new LSData(name,ls_id,time_acc,space_acc,ls_acc,coordinate_LS);                            
                            lsdata = new LSData(name,ls_id,time_acc,space_acc,ls_acc,coordinate_LS.get(i));
                        }
                    
                        lslist.add(lsdata);
                    
                    }
                }
                Room r = new Room(roomid,floor,depth,width,height,lslist);                
                house.rooms.add(r);
            }            
        } catch (IOException ex) {
            Logger.getLogger(ILS.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    public void Execute(){
        //システム実行用メソッド
        Properties properties = new Properties();
        
        //識別できたユーザのリスト
        List<Integer> useridlist = new ArrayList<>();
        
        //LS起動・終了用クラス
        LSTurn lsturn = new LSTurn();
        List<LocationData> datum = new ArrayList<>();
        
        //データ統合用クラス
        Integrate integrate = new Integrate(useridlist);
        
        //部屋IDリスト制作
        List<Integer> roomlist = new ArrayList<>();
        for(int i=0;i<house.rooms.size();i++){
            roomlist.add(house.rooms.get(i).roomNo);
        }

        
        //データ出力用のAPI2                 
        Api_service api2 = new Api_service(ACCLEVEL_OF_APP,roomlist);                
        
        //jsonから読み込んだ結果をもとにApi_lsを作成
        //Api_lsのリストに格納させる
        List<Api_ls> Speedway = new ArrayList<>();
        List<Api_ls> Humansensor = new ArrayList<>();
        List<Api_ls> Smkcam = new ArrayList<>();
        
        for(int i=0;i<house.rooms.size();i++){
            for(int j=0;j<house.rooms.get(i).ls_list.size();j++){
                //System.out.println(house.rooms.get(i).ls_list.get(j).ls_name);
                if(house.rooms.get(i).ls_list.get(j).ls_name.equals("Speedway")){                       //使うSpeedway１個あたりにSpeedway用Api_lsを１個追加
                    Speedway.add(new Api_ls(house.rooms.get(i),house.rooms.get(i).ls_list.get(j)));     //(Speedway１個にアンテナが複数個ある)
                }else if(house.rooms.get(i).ls_list.get(j).ls_name.equals("HumanSensor")){              //使う人感センサ１個あたりにHumanSensor用Api_lsを１個追加
                    //for(int k=0;k<house.rooms.get(i).ls_list.get(j).coordinate.size();k++){
                    Humansensor.add(new Api_ls(house.rooms.get(i),house.rooms.get(i).ls_list.get(j)));  //                
                    //}
                }else if(house.rooms.get(i).ls_list.get(j).ls_name.equals("Camera")){
                    //for(int k=0;k<house.rooms.get(i).ls_list.get(j).coordinate.size();k++){                    
                    Smkcam.add(new Api_ls(house.rooms.get(i),house.rooms.get(i).ls_list.get(j)));
                    //}
                }
            }
        }
        System.out.println("Api_ls, Speedway:"+Speedway.size()+", Humansensor:"+Humansensor.size()+", Smkcam:"+Smkcam.size());
        
        //全LS(speedway)との接続確立 
        //SMKカメラは手動での接続、シリアル接続によるコマンド入力での起動になるので、、ILSクラスを動作させる前にmotion9 -i onまでやっておく事にする？
        lsturn.allStartup(Humansensor.size());   
        

        
        try{
            Thread.sleep(1000);       //読み取り開始から読み取り終了まで少し停止
        }catch(InterruptedException e){              
        }       
        
        for(int i=0;i<CALCULATE_NUM;i++){
        
            Calendar calendar = Calendar.getInstance();       
            Time nowtime = new Time(calendar.get(calendar.HOUR),calendar.get(calendar.MINUTE),calendar.get(calendar.SECOND),calendar.get(calendar.MILLISECOND)); //現在時刻
        
            //speedwayに(引数)ミリ秒間、データ読ませる
            SpeedwayThread thread_speedway = new SpeedwayThread(lsturn,MEASUREING_TIME);            
            thread_speedway.start();

            //人感センサにデータ読ませる
            HumanSensorThread thread_humansensor = new HumanSensorThread(lsturn,MEASUREING_TIME);
            thread_humansensor.start();//
            
            //カメラ型センサからのデータを読む
            CameraSensorThread thread_camera = new CameraSensorThread(lsturn,MEASUREING_TIME);
            thread_camera.start();
            
            try{
                //Speedway,人感センサ、カメラセンサの各スレッド終了待機                
                thread_speedway.join(MAX_WAITING_TIME);
                thread_humansensor.join(MAX_WAITING_TIME);
                thread_camera.join(MAX_WAITING_TIME); //カメラはデータを採らない場合が何回かあった。なので最高待機時間を設ける
                
                //全スレッド終了後にデータ格納
                for(int j=0;j<Speedway.size();j++){ //(Api_ls)Speedwayはある一つのspeedwayにつながっている全てのアンテナひっくるめて一つにしている      
                    ////datum.addAll(Speedway.get(j).ReceiveDataofSpeedway(lsturn.speedway_db,nowtime)); 
                    datum.add(Speedway.get(j).ReceiveData(lsturn.speedway_buf.get(j),nowtime,new HumanSensorConfig()));
                }
                
                for(int j=0;j<Humansensor.size();j++){
                    ////datum.add(Humansensor.get(j).ReceiveDataofHumanSensor(lsturn.humansensor_db.get(j),nowtime,j,Humansensor.get(j).lsd.sensorconfig));//.get(j)));//properties.sensorconfig.get(j)));
                    datum.add(Humansensor.get(j).ReceiveData(lsturn.humansensor_buf.get(j),nowtime,Humansensor.get(j).lsd.sensorconfig));//.get(j)));//properties.sensorconfig.get(j)));                                         
                }
                for(int j=0;j<Smkcam.size();j++){
                    ////datum.add(Smkcam.get(j).ReceiveDataofCameraSensor(lsturn.camerasensor_db,nowtime,j,Smkcam.get(j).lsd.sensorconfig));//.get(j)));//properties.cameraconfig.get(j)));
                    datum.add(Smkcam.get(j).ReceiveData(lsturn.camerasensor_buf,nowtime,Smkcam.get(j).lsd.sensorconfig));//.get(j)));//properties.cameraconfig.get(j)));                     
                }
                
            }catch(InterruptedException e){
                e.printStackTrace();
            }   
            
            //for(int j=0;j<datum.size();j++){
            //    System.out.println("datum["+j+"]:"+datum.get(j).room_id+","+datum.get(j).user_id+","+datum.get(j).subtract_flag);
            //}
                                    
            System.out.println("ILS:データ取得完了,");
            java.awt.Toolkit.getDefaultToolkit().beep();            //データ取得した時点でアラーム鳴らす
            //System.out.println("SpeedwayThread?:"+thread_speedway.isAlive()+" HumanSensorThread?:"+thread_humansensor.isAlive()+" CameraSensorThread?:"+thread_camera.isAlive());
            
            //integrate,ルームナンバーはとりあえず最初の部屋
            //LocationData outputdata = integrate.IntegrateArea(house.rooms.get(0),datum,i,nowtime);  //Speedway,SensorPod
            List<LocationData> outputdata = integrate.IntegrateAreaEachUser(house.rooms.get(0),datum,i,nowtime);  //Speedway,SensorPod            
            
            //System.out.println("OUTPUT_ACCURACY_LEVEL:"+ACCLEVEL_OF_APP);
            
            System.out.println("---------------------------");           
            //for(int j=0;j<outputdata.size();j++){
                //System.out.print("User "+outputdata.get(j).user_id+" :");
                //api2.DoAll(outputdata.get(j),i, ACCLEVEL_OF_APP);                                
            //}
            if(useridlist.size() == 0 && outputdata.size() > 0){
                    api2.EachUserDataReturning(-1,outputdata,i);                 
            }else{
                
                //for(int j=0;j<useridlist.size();j++){
                //    api2.EachUserDataReturning(useridlist.get(j),outputdata,i); 
                //}
                api2.EachUserDataReturning(useridlist, outputdata, i);
            }
            System.out.println("---------------------------");
            
            datum.clear();
        }
        //全LS(speedway)との接続を切る
        lsturn.allEnd();            
    }
    
    public static void main(String args[]){
   
        ILS ils = new ILS();
        
        ils.makeHouse();
        ils.Execute();
        
        System.out.println("ILS Ended.");
        
    }    
}
