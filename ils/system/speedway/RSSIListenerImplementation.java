package ils.system.speedway;


//import com.example.sdksamples.*;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import ils.system.DataBuffer;
import ils.system.DataBufferofSpeedway;
import ils.system.Databuf;
import static java.lang.Math.pow;
import java.util.Calendar;

import java.util.List;

public class RSSIListenerImplementation implements TagReportListener {
    
    ////List<DataBufferofSpeedway> d;
    List<Databuf> d;    
    List<String> tagtable;
    
    ////RSSIListenerImplementation(List<DataBufferofSpeedway> db,List<String> tt){
    RSSIListenerImplementation(List<Databuf> db,List<String> tt){        
        d = db;
        tagtable = tt;
    }

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();

        for (Tag t : tags) {
            
            if(!tagtable.contains(t.getEpc().toString())){
                //System.out.println(" EPC: " + t.getEpc().toString());                
                tagtable.add(t.getEpc().toString());
                //System.out.println("Tagtable added! ("+ tagtable.size() +")");
            }                        
            /*
            if (reader.getName() != null) {
                System.out.print(" Reader_name: " + reader.getName());
            } else {
                System.out.print(" Reader_ip: " + reader.getAddress());
            }
            */                                    
            //if (t.isAntennaPortNumberPresent()) {
            //    //System.out.println(" antenna: " + t.getAntennaPortNumber());
            //}
            /*
            if (t.isFirstSeenTimePresent()) {
                System.out.print(" first: " + t.getFirstSeenTime().ToString());
            }

            if (t.isLastSeenTimePresent()) {
                System.out.print(" last: " + t.getLastSeenTime().ToString());
            }

            if (t.isSeenCountPresent()) {
                System.out.print(" count: " + t.getTagSeenCount());
            }

            if (t.isRfDopplerFrequencyPresent()) {
                System.out.print(" doppler: " + t.getRfDopplerFrequency());
            }
            */
            if (t.isPeakRssiInDbmPresent()) {
                //System.out.print(" peak_rssi: " + t.getPeakRssiInDbm()/100);
                
                //正確な距離を換算。なんか値が違うので直す必要あり
                               
                //TxPower = 25.0のとき                                
                double theodist = pow(10,(25-(t.getPeakRssiInDbm()/100))/20);
                double truedist = theodist*2.1597980697 - 39.417777511;
                
                //TxPower = 30.0のとき  
                //double theodist = pow(10,(30-(t.getPeakRssiInDbm()/100))/20);                
                //double truedist = theodist*1.4454012339 - 46.9797032819;               
                
                //d.get(t.getAntennaPortNumber() - 1).SetData(truedist);
                //System.out.print("  theo:"+ theodist +" Distance:" + truedist + "[m]");
                truedist = truedist * 100;//[m] -> [cm]
                ////d.get(t.getAntennaPortNumber() - 1).SetEachData(tagtable.indexOf(t.getEpc().toString()),truedist);
                d.get(t.getAntennaPortNumber() - 1).SetData(Integer.parseInt("111", 2),tagtable.indexOf(t.getEpc().toString()),truedist,true,0);                
                //System.out.println("  theo:"+ theodist +" Distance:" + truedist + "[cm]");                
            }

            //if (t.isChannelInMhzPresent()) {
            //    System.out.print(" chan_MHz: " + t.getChannelInMhz());
            //}

            if (t.isFastIdPresent()) {
                System.out.print("\n     fast_id: " + t.getTid().toHexString());

                System.out.print(" model: " +
                        t.getModelDetails().getModelName());

                System.out.print(" epcsize: " +
                        t.getModelDetails().getEpcSizeBits());

                System.out.print(" usermemsize: " +
                        t.getModelDetails().getUserMemorySizeBits());
            }

            //System.out.println("");
        }
    }
}
