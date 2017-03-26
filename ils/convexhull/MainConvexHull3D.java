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
public class MainConvexHull3D {
    
    public static final int POINT_NUM = 10;  //点の個数
    public static final int POINT_IND = 500; //点のとる範囲    
    
    public static void main(String args[]){
        
        List<Vector3D> colorlist = new ArrayList<>();   //点のカラー成分、左から赤、緑、青
        List<ConvexHull3D> ch3ds = new ArrayList<>();
        List<CalculateConvexhull3D> calculator = new ArrayList<>();
        
        ConvexHull3D intersectioned = new ConvexHull3D();
        ConvexHull3D unioned = new ConvexHull3D();
        ConvexHull3D subtracted = new ConvexHull3D();
        
        calculator.add(new CalculateConvexhull3D(POINT_NUM,POINT_IND));
        colorlist.add(new Vector3D(255,0,0));        
        
        calculator.add(new CalculateConvexhull3D(POINT_NUM,POINT_IND));
        colorlist.add(new Vector3D(0,0,255));           
        /*
        Vector3D pointofRoom = new Vector3D(0,0,0);    
        calculator.add(new CalculateRectangular(pointofRoom,10000,10000,10000));
        colorlist.add(new Vector3D(255,0,0));

        Vector3D pointofRoom2 = new Vector3D(2500,2500,2500);    
        calculator.add(new CalculateRectangular(pointofRoom2,5000,5000,5000));
        colorlist.add(new Vector3D(0,255,0));
        
        Vector3D pointofRoom3 = new Vector3D(5000,0,0);    
        calculator.add(new CalculateRectangular(pointofRoom3,10000,10000,10000));
        colorlist.add(new Vector3D(0,0,255));
        */
        
        for(int i=0;i<colorlist.size();i++){

            //CalculateConvexhull3D con3d = new CalculateConvexhull3D(POINT_NUM,POINT_IND);
                        
            //CalculateSphere3D con3d = new CalculateSphere3D(centerofsphere.get(i),500);
            //CalculateRectangular con3d = new CalculateRectangular(pointofrect.get(i),2000,2000,2000);
            //CalculateCorn con3d = new CalculateCorn(pointofcorn.get(i),2000,30,8,90*i,90*i);
            
            CalculateConvexhull3D con3d = calculator.get(i);
        
            ConvexHull3D ch3d = new ConvexHull3D();

            con3d.MakeConvexHull3D(ch3d);

            //System.out.println("ch3d.facet_v.size()"+ch3d.facet_v.size());
        
            String plyfilename = "/Users/Wataru-T/Desktop/Java_writedata/maintheme_figure/Convexhull3D_Inter_beforemihon"+i+".ply"; //作った.plyファイルを置くパス。適宜直してください
            //String plyfilepath = "/Users/ply/";                                   //作った.plyファイルを置くパス。適宜直してください        
            //String plyfilename = "Test.ply";                                      //書き込む.plyのファイル名  

            con3d.MakeplyFile(plyfilename,colorlist.get(i));    
            ch3ds.add(ch3d);
            System.out.println("");
                   
            /*
            for(int j=0;j<3;j++){
                java.awt.Toolkit.getDefaultToolkit().beep();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainConvexHull3D.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            */
            
            String plyintersectionname = "/Users/Wataru-T/Desktop/Java_writedata/maintheme_figure/Convexhull3D_Inter_aftermihon"+i+".ply";
            String plyunionname = "/Users/Wataru-T/Desktop/Java_writedata/Test_"+i+"union.ply";      
            String plysubtractionname = "/Users/Wataru-T/Desktop/Java_writedata/maintheme_figure/Convexhull3D_Sub_aftermihon"+i+".ply";            
            CalculateIntersectionofConvexHull3D isc = new CalculateIntersectionofConvexHull3D();  
            CalculateUnionConvexHull3D uc = new CalculateUnionConvexHull3D();
            CalculateSubtractofConvexHull3D sc = new CalculateSubtractofConvexHull3D();              
                                    
            if(i==1){
                intersectioned = isc.Intersectioning(ch3ds.get(0), ch3ds.get(1)); 
                isc.MakeplyFile(plyintersectionname, intersectioned, new Vector3D(255,0,255));
                //unioned = uc.Unioning(ch3ds.get(0), ch3ds.get(1));              
                subtracted = sc.Subtracting(ch3ds.get(0), ch3ds.get(1));
                sc.MakeplyFile(plysubtractionname, subtracted, new Vector3D(255,0,255));
            }else if(i > 1){
                ConvexHull3D is = intersectioned;                   
                intersectioned = isc.Intersectioning(is, ch3ds.get(i));
                unioned = uc.Unioning(unioned, ch3ds.get(i));
            }
            

        }   
        

        
    }
}
