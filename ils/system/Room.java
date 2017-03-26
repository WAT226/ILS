/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wataru-T
 */
public class Room extends HouseMapSite{
    
    int roomNo; //部屋番号
    int floor;  //階数
    int depth;  //たて
    int width;  //よこ
    int height; //高さ（２次元の場合いらない）
    List<LSData> ls_list = new ArrayList<>(); //部屋内にあるLS
    
    Room(int rNo, int F, int x, int y,List<LSData> lslist){
        //２次元の場合
        roomNo = rNo;
        floor  = F;
        depth = x;
        width = y;
        ls_list.addAll(lslist);
    }    
    
    Room(int rNo, int F, int x, int y,int z,List<LSData> lslist){
        //３次元の場合
        roomNo = rNo;
        floor  = F;
        depth = x;
        width = y;
        height = z;
        ls_list.addAll(lslist);
    }
    
}
