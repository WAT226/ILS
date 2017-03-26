/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.convexhull;

import ils.system.humansensor.HumanSensorConfig;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class CalculateCorn extends CalculateConvexhull3D{
    Vector3D top; //角錐の頂点の座標
    double height;//角錐の高さ
    double top_angle; //角錐の頂点から底面の中心を通る直線と、角錐の母線との角度
    int bottom_shape; //角錐の底面は何角形？
    
    double x_angle;   //x軸方向を基準にして回転させる角度
    double y_angle;   //y軸方向を基準にして回転させる角度
    
    public CalculateCorn(Vector3D t,double h,double ta,int bs,double xa,double ya){
        top = t;
        height = h;
        top_angle = ta;
        bottom_shape = bs;
        
        x_angle = xa;
        y_angle = ya;
        
        v3 = this.pointGenerate();
        g = new ArrayList<List<Boolean>>();        //衝突グラフ
        Fconflict = new ArrayList<List<Integer>>();  
        allpoints = new ArrayList<>();
        allpoints.addAll(v3);

        POINT_NUM = v3.size();        
    }
    
    public CalculateCorn(Vector3D t,HumanSensorConfig hsc){
        top = t;
        height = hsc.height;
        top_angle = hsc.top_angle;
        bottom_shape = hsc.bottom_shape;
        
        x_angle = hsc.x_angle;
        y_angle = hsc.y_angle;
        
        v3 = this.pointGenerate();
        g = new ArrayList<List<Boolean>>();        //衝突グラフ
        Fconflict = new ArrayList<List<Integer>>();  
        allpoints = new ArrayList<>();
        allpoints.addAll(v3);

        POINT_NUM = v3.size();        
    }
    
    private List<Vector3D> pointGenerate(){
        
        List<Vector3D> points = new ArrayList<>();
                
        points.add(top);
        
        double l = height / Math.cos(Math.toRadians(top_angle)); //角錐の母線の長さ
             
        for(double i=0;i<360;i += (double)360/bottom_shape){//底面の座標
            points.add(new Vector3D(top.getX() + (height * Math.tan(Math.toRadians(top_angle)) * Math.cos(Math.toRadians(i))),
                                    top.getY() + (height * Math.tan(Math.toRadians(top_angle)) * Math.sin(Math.toRadians(i))),
                                    top.getZ() - height ));
        }      
        //System.out.println("x_angle:"+x_angle+" y_angle:"+y_angle);
        //System.out.println("points:"+points);
        
        //角錐の頂点を通るx軸に平行な直線を軸にx_angle度だけ回転させる作業
        for(int i=1;i<points.size();i++){
            Vector3D v1 = new Vector3D(points.get(i).getX(),
                                        points.get(i).getY() - top.getY(),
                                        points.get(i).getZ() - top.getZ());
            
            Vector3D v2 = new Vector3D(v1.getX(),
                                        (v1.getY() * Math.cos(Math.toRadians(x_angle))) - (v1.getZ() * Math.sin(Math.toRadians(x_angle))),
                                        (v1.getY() * Math.sin(Math.toRadians(x_angle))) + (v1.getZ() * Math.cos(Math.toRadians(x_angle))));
            
            Vector3D v3 = new Vector3D(v2.getX(),
                                        v2.getY() + top.getY(),
                                        v2.getZ() + top.getZ());
            
            points.set(i,v3);
        }             
        
        //角錐の頂点を通るy軸に平行な直線を軸にy_angle度だけ回転させる作業
        for(int i=1;i<points.size();i++){
            Vector3D v1 = new Vector3D(points.get(i).getX() - top.getX(),
                                        points.get(i).getY(),
                                        points.get(i).getZ() - top.getZ());
            
            Vector3D v2 = new Vector3D((v1.getX() * Math.cos(Math.toRadians(y_angle))) + (v1.getZ() * Math.sin(Math.toRadians(y_angle))),
                                        v1.getY(),
                                       (-1 * v1.getX() * Math.sin(Math.toRadians(y_angle))) + (v1.getZ() * Math.cos(Math.toRadians(y_angle))));
            
            Vector3D v3 = new Vector3D(v2.getX() + top.getX(),
                                        v2.getY(),
                                        v2.getZ() + top.getZ());
            
            points.set(i,v3);
        }         
        //参考：ttp://www.cg.info.hiroshima-cu.ac.jp/~miyazaki/knowledge/tech07.html
        
        //System.out.println("points.size():"+points.size());
        //System.out.println("points:"+points);
        return points;
    }        
}
