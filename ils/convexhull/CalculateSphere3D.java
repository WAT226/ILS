/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.convexhull;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class CalculateSphere3D extends CalculateConvexhull3D{
    //球(Sphere)のConvexHull3Dを計算するクラス
    
    Vector3D center;    //球の中心の座標
    double radius;      //球の半径
    int angle_diff;     //球の表面は何度間隔で点を取るか,90以下が望ましい
    
    public CalculateSphere3D(Vector3D c,double r,int inner_angle) {
        center = c;
        radius = r;
        angle_diff = inner_angle;
        
        v3 = this.pointGenerate();
        g = new ArrayList<List<Boolean>>();        //衝突グラフ
        Fconflict = new ArrayList<List<Integer>>();  
        allpoints = new ArrayList<>();
        allpoints.addAll(v3);

        POINT_NUM = v3.size();
    }
    
    private List<Vector3D> pointGenerate(){
        
        List<Vector3D> points = new ArrayList<>();
        //int angle_diff = 10; //90 以下が望ましい
        
        double center_x = this.center.getX();
        double center_y = this.center.getY();
        double center_z = this.center.getZ();         
        
        for(int theta_a = 0;theta_a <= 180;theta_a += angle_diff){
            for(int theta_b = 0;theta_b<360;theta_b+=angle_diff){
                //System.out.println("シータa:"+theta_a+" シータb:"+theta_b+"angle_diff:"+angle_diff);
                Vector3D point = new Vector3D(center_x + (this.radius * Math.sin(Math.toRadians(theta_a)) * Math.cos(Math.toRadians(theta_b))),
                                              center_y + (this.radius * Math.sin(Math.toRadians(theta_a)) * Math.sin(Math.toRadians(theta_b))),
                                              center_z + (this.radius * Math.cos(Math.toRadians(theta_a))));
                
                if(!(points.contains(point))){
                    points.add(point);
                    if(theta_a == 0 || theta_a == 180){
                        break;
                    }
                }
            }
        }

        List<Vector3D> adjusted_points = new ArrayList<>();
        adjusted_points.addAll(AdjustingMinusZero(points));        
        //System.out.println("points.size():"+points.size());
        //System.out.println("points:"+points);
        
        //return points;
        return adjusted_points;
    }
    
    private List<Vector3D> AdjustingMinusZero(List<Vector3D> v){
        List<Vector3D> adjusted_v = new ArrayList<>();
        for(int h=0;h<v.size();h++){
            //要素に-0.0を持つものがある場合がある。この-0.0を0.0に正す動作を行う
            Vector3D vh = v.get(h);
            
            double vh_x = vh.getX();
            vh_x += 1.0;
            vh_x -= 1.0;
            
            double vh_y = vh.getY();
            vh_y += 1.0;
            vh_y -= 1.0;

            double vh_z = vh.getZ();
            vh_z += 1.0;
            vh_z -= 1.0;
            
            Vector3D vh_adjusted = new Vector3D(vh_x,vh_y,vh_z);
            
            if(!adjusted_v.contains(vh_adjusted)){
                adjusted_v.add(vh_adjusted);
            }
        }        
        return adjusted_v;        
    }    
}
