/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class UserColorList {
    
    List<Vector3D> usercolorlist;
    Vector3D unknownusercolorlist;
    
    UserColorList(){
        usercolorlist = new ArrayList<>();
        usercolorlist.add(new Vector3D(255,0,0));
        usercolorlist.add(new Vector3D(0,255,0));
        usercolorlist.add(new Vector3D(0,0,255));        
        usercolorlist.add(new Vector3D(255,255,0));
        usercolorlist.add(new Vector3D(0,255,255));
        usercolorlist.add(new Vector3D(255,0,255)); 
        
        unknownusercolorlist = new Vector3D(128,128,128);//不明ユーザは灰色
    }
    
    public Vector3D Getcolor(int i){
        
        if(i == -1){
            return unknownusercolorlist;
        }else{
            int index = i % usercolorlist.size();
        
            return usercolorlist.get(index);
        }
    }
}
