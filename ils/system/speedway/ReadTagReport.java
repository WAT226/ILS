/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system.speedway;

//import ils.system.speedway.*;
//import com.example.sdksamples.SampleProperties;
//import com.example.sdksamples.TagReportListenerImplementation;
import com.impinj.octane.AntennaConfigGroup;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReaderMode;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.ReportMode;
import com.impinj.octane.Settings;
import ils.system.DataBuffer;
import ils.system.DataBufferofSpeedway;
import ils.system.Databuf;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Wataru-T
 */
public class ReadTagReport {
    
    String hostname;
    ImpinjReader reader;
    Settings settings;
    ReportConfig report;
    AntennaConfigGroup antennas;
    
    List<String> tagtable = new ArrayList<>();
    
    //public static void main(String[] args) {
    //public void Setting(List<DataBufferofSpeedway> d,short[] portnumlist){  
    public void Setting(List<Databuf> d,short[] portnumlist){          
        //SpeedWayとの接続を確立
        try {
            hostname = System.getProperty(SampleProperties.hostname);

            if (hostname == null) {
                throw new Exception("Must specify the '"
                        + SampleProperties.hostname + "' property");
            }

            reader = new ImpinjReader();
            
            System.out.println(hostname);      
                      
            System.out.println("Connecting");
            reader.connect(hostname);

            settings = reader.queryDefaultSettings();

            report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setMode(ReportMode.Individual);
            
            
            //added
            report.setIncludePeakRssi(true);

            // The reader can be set into various modes in which reader
            // dynamics are optimized for specific regions and environments.
            // The following mode, AutoSetDenseReader, monitors RF noise and interference and then automatically
            // and continuously optimizes the reader’s configuration
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);

            // set some special settings for antenna 1
            antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(portnumlist);      //使うアンテナの番号のリスト、初めはnew short[]{1}が入ってた
            
            for(int i=0;i<portnumlist.length;i++){ //getAntennnaの引数は最初は全部(short) 1だった
                antennas.getAntenna(portnumlist[i]).setIsMaxRxSensitivity(false);
                antennas.getAntenna(portnumlist[i]).setIsMaxTxPower(false);
                antennas.getAntenna(portnumlist[i]).setTxPowerinDbm(25.0);
                antennas.getAntenna(portnumlist[i]).setRxSensitivityinDbm(-70);
            }
            
            reader.setTagReportListener(new RSSIListenerImplementation(d,tagtable));

            System.out.println("Applying Settings");
            reader.applySettings(settings);
            /*
            System.out.println("Starting");
            reader.start();

            System.out.println("Press Enter to exit.");
            Scanner s = new Scanner(System.in);
            s.nextLine();

            reader.stop();
            reader.disconnect();
            System.out.println("Speedway Ended.");
            */
        } catch (OctaneSdkException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
    
    public void Reading(long millis){
        //Speedwayに読み取らせる
        try{
            //System.out.println("Starting");
            reader.start();//読み取り開始

            //System.out.println("Press Enter to exit.");
            //Scanner s = new Scanner(System.in);
            //s.nextLine();

            try{
                Thread.sleep(millis);       //読み取り開始から読み取り終了までmillis[ミリ秒]間停止させることで
            }catch(InterruptedException e){ //millis[ms]間読み取りが可能になる               
            }
            
            reader.stop();//読み取り終了
            //reader.disconnect();
            //System.out.println("Speedway Ended.");
        } catch (OctaneSdkException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }       
    }
    
    public void Disconnecting(){
        //SpeedWayとの接続を切る
        try{
            reader.disconnect();
            System.out.println("Speedway Disconnected.");
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }

    /*
    public static void main(String[] args) {

        try {
            String hostname = System.getProperty(SampleProperties.hostname);

            if (hostname == null) {
                throw new Exception("Must specify the '"
                        + SampleProperties.hostname + "' property");
            }

            ImpinjReader reader = new ImpinjReader();
            
            System.out.println(hostname);            

            System.out.println("Connecting");
            reader.connect(hostname);

            Settings settings = reader.queryDefaultSettings();

            ReportConfig report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setMode(ReportMode.Individual);
            
            //added
            report.setIncludePeakRssi(true);

            // The reader can be set into various modes in which reader
            // dynamics are optimized for specific regions and environments.
            // The following mode, AutoSetDenseReader, monitors RF noise and interference and then automatically
            // and continuously optimizes the reader’s configuration
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);

            // set some special settings for antenna 1
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[]{1});
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(20.0);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

            DataBuffer d = new DataBuffer();
            reader.setTagReportListener(new RSSIListenerImplementation(d));

            System.out.println("Applying Settings");
            reader.applySettings(settings);

            System.out.println("Starting");
            reader.start();

            System.out.println("Press Enter to exit.");
            Scanner s = new Scanner(System.in);
            s.nextLine();

            reader.stop();
            reader.disconnect();
        } catch (OctaneSdkException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }    
    */
    
}


/*
//public class ReadTagReport {
    
    public static void main(String[] args) {
    //public void ReadTag(DataBuffer d){    

        try {
            String hostname = System.getProperty(SampleProperties.hostname);

            if (hostname == null) {
                throw new Exception("Must specify the '"
                        + SampleProperties.hostname + "' property");
            }

            ImpinjReader reader = new ImpinjReader();
            
            System.out.println(hostname);            

            System.out.println("Connecting");
            reader.connect(hostname);

            Settings settings = reader.queryDefaultSettings();

            ReportConfig report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setMode(ReportMode.Individual);
            
            
            //added
            report.setIncludePeakRssi(true);

            // The reader can be set into various modes in which reader
            // dynamics are optimized for specific regions and environments.
            // The following mode, AutoSetDenseReader, monitors RF noise and interference and then automatically
            // and continuously optimizes the reader’s configuration
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);

            // set some special settings for antenna 1
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[]{1});
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(20.0);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

            reader.setTagReportListener(new RSSIListenerImplementation(d));

            System.out.println("Applying Settings");
            reader.applySettings(settings);

            System.out.println("Starting");
            reader.start();

            System.out.println("Press Enter to exit.");
            Scanner s = new Scanner(System.in);
            s.nextLine();

            reader.stop();
            reader.disconnect();
            System.out.println("Speedway Ended.");
        } catch (OctaneSdkException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
    
    
}
*/
