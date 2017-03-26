/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import ils.system.humansensor.HumanSensorConfig;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wataru-T
 */
public class Properties {
   
    //public static final int RASPI_NUM = 2;      //ラズパイの個数
    //public static final int HUMANSENSOR_NUM = 4;    //使用する人感センサの個数
            
    short[] enableportnumlist = {1,2};  //Speedwayで使用するアンテナのポート番号のリスト
    
    String convexfilepath; //作ったConvexHullの.plyファイルを置くパス
    
    List<Raspberry_pi> raspi = new ArrayList<>();
    //List<HumanSensorConfig> sensorconfig = new ArrayList<>();
    //List<HumanSensorConfig> cameraconfig = new ArrayList<>();    
    
    public Properties(){
        
        convexfilepath = "/Users/Wataru-T/Desktop/Java_writedata/Experiment/";
        
        //ラズパイの設定、ラズパイのIPアドレス、使用しているセンサが出力するGPIO番号を入力
        int[] gpionum1 = {18,27};
        raspi.add(new Raspberry_pi("150.65.204.71",gpionum1));
        int[] gpionum2 = {18,27};
        //int[] gpionum2 = {18};        
        raspi.add(new Raspberry_pi("150.65.206.235",gpionum2)); 
        
        int[] gpionum3 = {18,27};
        //int[] gpionum3 = {18};        
        raspi.add(new Raspberry_pi("150.65.124.103",gpionum3));         
        int[] gpionum4 = {18,27};
        //int[] gpionum4 = {18};        
        raspi.add(new Raspberry_pi("150.65.124.92",gpionum4));         
        
        //sensorconfig.add(new HumanSensorConfig(700,60,10,315,90));//人感センサの角度情報、本当はjson内に記述させるべきだけど、、、
        //sensorconfig.add(new HumanSensorConfig(700,60,10,315,270));
        //cameraconfig.add(new HumanSensorConfig(700,60,10,90,0));        
    }
    
    public String[] getTwoSensorsValue(int raspi_ind){
        
        
        if(raspi.get(raspi_ind).GPIONUM_OF_SENSOR.length == 2){
            String[] arg = {raspi.get(raspi_ind).IP_ADDRESS,
                        Integer.toString(raspi.get(raspi_ind).GPIONUM_OF_SENSOR[0]),
                        Integer.toString(raspi.get(raspi_ind).GPIONUM_OF_SENSOR[1])};
            
            return arg;
        }else if(raspi.get(raspi_ind).GPIONUM_OF_SENSOR.length == 1){
            String[] arg = {raspi.get(raspi_ind).IP_ADDRESS,
                        Integer.toString(raspi.get(raspi_ind).GPIONUM_OF_SENSOR[0])};            
            
            return arg;
        }
        String arg[] = {raspi.get(raspi_ind).IP_ADDRESS,
                        Integer.toString(raspi.get(raspi_ind).GPIONUM_OF_SENSOR[0])};
        return arg;
    }    
    
    public String getRaspi_ip(int raspi_ind){
        String ip = raspi.get(raspi_ind).IP_ADDRESS;
        
        return ip;
    }
}

class Raspberry_pi{
    
    String IP_ADDRESS;//ラズパイのIPアドレス
    int[] GPIONUM_OF_SENSOR;//ラズパイが持つ人感センサの出力GPIO番号
    
    Raspberry_pi(String ip,int[] gpionum){
        IP_ADDRESS = ip;
        GPIONUM_OF_SENSOR = gpionum;
    }
    
}
/*
class HumanSensorConfig{//人感センサの角度情報に関するクラス
    
    double height;//角錐の高さ
    double top_angle; //角錐の頂点から底面の中心を通る直線と、角錐の母線との角度
    int bottom_shape; //角錐の底面は何角形？
    
    double x_angle;   //x軸方向を基準にして回転させる角度
    double y_angle;   //y軸方向を基準にして回転させる角度    
    
    HumanSensorConfig(double h,double ta,int bs,double xa,double ya){
        height = h;
        top_angle = ta;
        bottom_shape = bs;
        x_angle = xa;
        y_angle = ya;
    }
    
}
*/