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
public class CalculateRectangular extends CalculateConvexhull3D{
    
    Vector3D startpoint; //直方体の頂点のうち、x,y,z座標がどれも一番低い点
    double x_length; //横の長さ
    double y_length; //たての長さ
    double z_length; //高さ
    
    public CalculateRectangular(Vector3D s,double x,double y,double z){
        startpoint = s;
        x_length = x;        
        y_length = y;
        z_length = z;
        
        v3 = this.pointGenerate();
        g = new ArrayList<List<Boolean>>();        //衝突グラフ
        Fconflict = new ArrayList<List<Integer>>();  
        allpoints = new ArrayList<>();
        allpoints.addAll(v3);

        POINT_NUM = v3.size();        
    }
    
    private List<Vector3D> pointGenerate(){
        
        List<Vector3D> points = new ArrayList<>();
                
        points.add(startpoint);
        points.add(new Vector3D(startpoint.getX(),startpoint.getY() + y_length,startpoint.getZ()));
        points.add(new Vector3D(startpoint.getX() + x_length,startpoint.getY(),startpoint.getZ()));
        points.add(new Vector3D(startpoint.getX() + x_length,startpoint.getY() + y_length,startpoint.getZ()));
        points.add(new Vector3D(startpoint.getX(),startpoint.getY(),startpoint.getZ() + z_length));
        points.add(new Vector3D(startpoint.getX(),startpoint.getY() + y_length,startpoint.getZ() + z_length));
        points.add(new Vector3D(startpoint.getX() + x_length,startpoint.getY(),startpoint.getZ() + z_length));
        points.add(new Vector3D(startpoint.getX() + x_length,startpoint.getY() + y_length,startpoint.getZ() + z_length));        
        
        //System.out.println("points.size():"+points.size());
        //System.out.println("points:"+points);
        return points;
    }     
}
