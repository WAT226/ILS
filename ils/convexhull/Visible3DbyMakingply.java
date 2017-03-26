/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.convexhull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class Visible3DbyMakingply {
    
    //File file = new File("/Users/Wataru-T/Desktop/Java_writedata/Testnew.ply");  //書き込む.plyファイル名 
    File file;
    int pointnum;    //描画する点の数
    List<Vector3D> pointlist;
    List<List<Integer>> facet_vlist;
    Vector3D colorlist;
    boolean nullflag = false;
    
    Visible3DbyMakingply(String filename,List<Vector3D> v,List<List<Integer>> fvind,Vector3D cl){
        file = new File(filename);
        pointnum = v.size();
        pointlist = v;
        facet_vlist = fvind;
        colorlist = cl;
    }
    
    public Visible3DbyMakingply(String filename,ConvexHull3D c,Vector3D cl){
        if(c != null){
            file = new File(filename);
            pointnum = c.c.size();
            pointlist = c.c;
            facet_vlist = c.facet_v;
            colorlist = cl;
        }else{
            nullflag = true;
        }
    }    
    
    private boolean checkBeforeWritefile(File file){
        if (file.exists()){
          if (file.isFile() && file.canWrite()){
            return true;
          }
        }

        return false;
    }    
    
    private void WriteHeaderofPly(PrintWriter pw){
        //.plyのヘッダを書き込む
        pw.println("ply");
        pw.println("format ascii 1.0");
        pw.println("comment Kinect v1 generated");
        pw.println("element vertex " + pointnum);
        pw.println("property double x");
        pw.println("property double y");
        pw.println("property double z");
        pw.println("property uchar red");
        pw.println("property uchar green");
        pw.println("property uchar blue");
        
        pw.println("element face " + facet_vlist.size());
        pw.println("property list uchar int vertex_index");       
        
        pw.println("end_header");
                
    }
    
    private void WritePointsofPly(PrintWriter pw){
        //.plyに、凸包の点の情報を書き込む
        int r = (int)colorlist.getX(); //点のR成分（色素）
        int g = (int)colorlist.getY();   //点のG成分
        int b = (int)colorlist.getZ();   //点のB成分
        
        for(int i=0;i<pointnum;i++){
            pw.println(pointlist.get(i).getX() +" "+ //点のx座標
                       pointlist.get(i).getY() +" "+ //点のy座標
                       pointlist.get(i).getZ() +" "+ //点のz座標
                       r +" "+ g +" "+ b);
        }
        
    }
    
    private void WriteFacetsofPly(PrintWriter pw){
        //.plyに、凸包の面（ファセット）の情報を書き込む
        for(int i=0;i<facet_vlist.size();i++){
            pw.print(facet_vlist.get(i).size());
            for(int j=0;j<facet_vlist.get(i).size();j++){
                pw.print(" "+facet_vlist.get(i).get(j)); //三角形の面を作る点のインデックス３つ
            }
            pw.println("");
        }       
                
    }
        
    public void WriteplyFile(){
                
        try{
            if(!nullflag){
                if (!checkBeforeWritefile(file)){
                    file.createNewFile();
                    System.out.println(file +"を作りました");
                }   

                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

                WriteHeaderofPly(pw);
                WritePointsofPly(pw);
                WriteFacetsofPly(pw);

                pw.close();
            }else{
                System.out.println("Cannnot Create plyfile.");
            }

        }catch(IOException e){
            System.out.println(e + file.getAbsolutePath() +" Visible3DbyMakingply.WriteplyFile");
        }        
    }
}
