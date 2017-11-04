package com.scada.server;

import com.scada.model.dataBase.Andon.Andon;
import rx.Observable;

import javax.xml.soap.SAAJResult;
import java.text.SimpleDateFormat;
import java.util.*;

public class OPCServer {

    private static Map<String, Andon> andonList = new HashMap();

    public OPCServer() {
        andonList.put("LEVEL_1_MIN", new Andon(1, "LEVEL_1_MIN", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_2_MIN", new Andon(1, "LEVEL_1_MIN", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_3_MIN", new Andon(1, "LEVEL_1_MIN", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_1_MIN_CRITICAL", new Andon(1, "LEVEL_1_MIN_CRITICAL", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_2_MIN_CRITICAL", new Andon(1, "LEVEL_1_MIN_CRITICAL", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_3_MIN_CRITICAL", new Andon(1, "LEVEL_1_MIN_CRITICAL", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_1_MAX", new Andon(1, "LEVEL_1_MAX", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_2_MAX", new Andon(1, "LEVEL_1_MAX", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_3_MAX", new Andon(1, "LEVEL_1_MAX", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_1_MAX_CRITICAL", new Andon(1, "LEVEL_1_MAX_CRITICAL", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_2_MAX_CRITICAL", new Andon(1, "LEVEL_1_MAX_CRITICAL", 26.3, "2017-10-30 22:00:00"));
        andonList.put("LEVEL_3_MAX_CRITICAL", new Andon(1, "LEVEL_1_MAX_CRITICAL", 26.3, "2017-10-30 22:00:00"));

    }

    public List<Andon> getAndonData() {
        final ArrayList<Andon> andons = new ArrayList<>();
        //sprawdzenie czy data jest większa od ostatnio monitorowanej
//        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Date date = simpleDateFormat.parse(...);
//        if(date.compareTo(date2) <= 0) {
////            update in andonList;
//            andons.add(new Andon())
//        }

        return andons;
    }

    public List<Record> getSystemVariableData() {
        final List<Record> values = new ArrayList<>();
        values.add(new Record("OUTPUT"));
        values.add(new Record("LEVEL_1"));
        values.add(new Record("LEVEL_2"));
        values.add(new Record("LEVEL_3"));
        return values;
    }

    public List<ControllerRecord> getControllersData() {
        final List<ControllerRecord> values = new ArrayList<>();
        values.add(new ControllerRecord("VALVE_1", "AUTOMATIC_MODE"));
        values.add(new ControllerRecord("VALVE_2", "AUTOMATIC_MODE"));
        values.add(new ControllerRecord("VALVE_3", "AUTOMATIC_MODE"));
        values.add(new ControllerRecord("VALVE_4", "AUTOMATIC_MODE"));
        return values;
    }

//    private static Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Random generator = new Random();

    public static class Record {

        private String tag;
        private Double value;
        private String date;

        public Record(String tag, Double value, String date) {
            this.tag = tag;
            this.value = value;
            this.date = date;
        }
        public Record(String tag) {
            this.tag = tag;
            this.value = generator.nextDouble()*10;
            final Calendar calendar = Calendar.getInstance();
            final Date date = calendar.getTime();
            this.date = dateFormat.format(date);
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public static class ControllerRecord extends Record {

        private  String mode;
        public ControllerRecord(String tag, String mode, Double value, String date) {
            super(tag, value, date);
            this.mode = mode;
        }

        public ControllerRecord(String tag, String mode) {
            super(tag);
            this.mode = mode;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}
