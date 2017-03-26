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
public class SpeedwayThread extends Thread{
    
    LSTurn lsturn;
    long ms;
    
    public SpeedwayThread(LSTurn lst,long mills){
        lsturn = lst;
        ms = mills;
    }
    
    public void run(){
        System.out.println("Speedway Thread Start.");
        lsturn.readingSpeedway(ms);   
        System.out.println("Speedway Thread ended.");
    }
    
    //public static void main(String[] args) {
    //    Thread thread = new SpeedwayThread();
    //  thread.start();  // スレッドの起動。
    //}    
}




