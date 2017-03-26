/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system.thread;

import ils.system.LSTurn;

/**
 *
 * @author Wataru-T
 */
public class CameraSensorThread extends Thread{
    
    LSTurn lsturn;
    long ms;
    
    public CameraSensorThread(LSTurn lst,long mills){
        lsturn = lst;
        ms = mills;
    }
    
    public void run(){
        System.out.println("  CameraSensor Thread Start.");
        lsturn.readingCameraSensor(ms);
        System.out.println("  CameraSensor Thread ended.");
    }    
}
