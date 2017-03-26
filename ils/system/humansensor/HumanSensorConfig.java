/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system.humansensor;

/**
 *
 * @author Wataru-T
 */
public class HumanSensorConfig {//人感センサの角度情報に関するクラス
    
    public double height;//角錐の高さ
    public double top_angle; //角錐の頂点から底面の中心を通る直線と、角錐の母線との角度
    public int bottom_shape; //角錐の底面は何角形？
    
    public double x_angle;   //x軸方向を基準にして回転させる角度,,x軸を軸として左回りに回転
    public double y_angle;   //y軸方向を基準にして回転させる角度,,y軸を軸として右回りに回転
    
    public HumanSensorConfig(double h,double ta,int bs,double xa,double ya){
        height = h;
        top_angle = ta;
        bottom_shape = bs;
        x_angle = xa;
        y_angle = ya;
    }
    
    public HumanSensorConfig(){
        
    }
    
}
