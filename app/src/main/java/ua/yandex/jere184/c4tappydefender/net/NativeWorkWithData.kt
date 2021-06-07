package ua.yandex.jere184.c4tappydefender.net;

import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ua.yandex.jere184.c4tappydefender.util.GlobalSettings;
import ua.yandex.jere184.c4tappydefender.util.MyLog;
import ua.yandex.jere184.c4tappydefender.db.LocalDB;
import ua.yandex.jere184.c4tappydefender.util.Public;

/**
 * нативный класс работы с данными использует класы lib адаптируя их функции под контекст этого проэкта
 * (для каждого проэкта заново переписывается)
 */
public class NativeWorkWithData implements QueryHTTP.MyCallback {
    //region members
    public TextView tvLocalData;
    public TextView tvServData;
    public static QueryHTTP queryHTTP;
    public static LocalDB localDB;
    public static String localResultsTable = "LocalResults";
    public static String servCopyResultsTable = "ServCopyResults";
    //endregion

    //region constructors
    public NativeWorkWithData() {
        initHttp();

    }
    //endregion

    public void addNewResult(float dist, long time) {
        insertLocalDB(dist, time);
        if (GlobalSettings.isSendAllResult)
            sendResultToDB(dist, time);
        else
            sendTotalInfoToDB();
    }

    //region SQLite
    public void readLocalDB() {
        localDB = new LocalDB(Public.context);//инициализируем и подгружаем таблици из файла

        //region добавляем таблици если их еще нет (предположительно только первый запуск)
        localDB.addNewTable(localResultsTable, (byte) 3);//datetime,dist,time
        localDB.addNewTable(servCopyResultsTable, (byte) 4);//pos,dist,time,name
        //endregion

        //region get local
        ArrayList<ArrayList<String>> t_result = localDB.f_selectAll(localResultsTable);
        String t_str = "\t|\tDate\t|\tDistance\t|\tTime\n";
        tvLocalData.setText(t_str);
        for (ArrayList<String> row : t_result) {
            for (String item : row) {
                t_str = t_str + "\t| " + item + " ";
            }
            t_str = t_str + "\n";
        }
        tvLocalData.setText(t_str);
        //endregion

        //region get serv copy
        t_result = localDB.f_selectAll(servCopyResultsTable);
        t_str = "|\tPos\t|\tDist\t|\tTime\t|\tName\t|\n";
        tvServData.setText(t_str);
        for (ArrayList<String> row : t_result) {
            String t_id = row.get(0);
            if (t_id.length() == 1) t_id = t_id + "\t";
            String t_dist = row.get(1);
            if (t_dist.length() > 6) t_dist = t_dist.substring(0, 5);
            String t_time = row.get(2).substring(0, row.get(2).length() - 3);
            String t_name = row.get(3);
            if (t_name.length() > 15) t_name = t_name.substring(0, 14);

            t_str = t_str + "|\t" + t_id + "\t|\t" + t_dist + "\t|\t" + t_time + "\t|\t" + t_name + "\t|\n";
//      for (String item : row) {
//        t_str = t_str + "|\t" + item + "\t";
//      }
//      t_str = t_str + "\n";
        }
        tvServData.setText(t_str);
        //endregion
    }

    public void insertLocalDB(float dist, long time) {
        String t_currentTimeStr = (android.text.format.DateFormat.format("dd.MM hh:mm", new Date(System.currentTimeMillis()))).toString();
        localDB.f_insert(localResultsTable, t_currentTimeStr, String.valueOf(dist), String.valueOf(time));
    }
    //endregion

    //region _queryHttp
    private void initHttp() {
        queryHTTP = new QueryHTTP("");
        queryHTTP.setOnQueryFinished(this);
        queryHTTP.s_isNeedGetDATA(true);
        queryHTTP.s_isNeedSendDATA(true);

    }

    @Override
    public void queryFinished(byte state)/*Callback для получения данных*/ {
        if (queryHTTP.g_isNeedGetDATA()) /*проверяет нужно ли этому запросу получать данные */ {
            //if (t_queryHttp.g_state() != c_queryHTTP.e_states.finished) return; // проверка статуса, но она излишся так как метод колбек вызывается сразу после установки статуса в finished
            JSONObject json = queryHTTP.g_resultData_JsonObj(); //забираем данные из предыдущего запроса
            if (json != null) {
                if (queryHTTP.g_httpURL().contains("get10")) {
                    //c_Public._data.tv_servData.setText("");
                    localDB.f_clear(servCopyResultsTable);//вызываем функцию очистки локальной копии серверных данных, в качестве аргумента строка с названием таблицы
                    //_localDB.f_clear(_localResultsTable);
                    for (int i = 1; i <= 10; i++) {
                        try {
                            int position = json.getInt("pos_" + i);
                            String name = json.getString("name_" + i);
                            double distance = json.getDouble("dist_" + i);
                            long time = json.getLong("time_" + i);
//!!!
                            localDB.f_insert(servCopyResultsTable, String.valueOf(position),
                                    String.valueOf(distance), String.valueOf(time), name);
                            //c_Public._data.tv_servData.setText(c_Public._data.tv_servData.getText() + "\t| " + position + "\t| " + name + "\t| "
                            //        + distance + "\t| " + time + " " + "\n");
                        } catch (Exception e) {
                            new MyLog(Public.myLogcatTAG, null, "queryFinished(" + state + ") ").L();
                        }
                    }
                }
            } else {
                new MyLog(Public.myLogcatTAG, null, "queryFinished(" + state + ") JSONObject == null |||").L();
            }
        }

    }

    public void sendResultToDB(float t_distance, long t_time) {
        //_queryHttp.s_httpURL("https://rewheeldev.000webhostapp.com/TappyDefender/input.php");
        queryHTTP.s_httpURL("http://rewheeldev.eu5.org/TappySpace/insert/");
        ArrayList<String> data = new ArrayList<>();
        data.add("_distance");
        data.add("" + t_distance);
        data.add("_time");
        data.add("" + t_time);
        data.add("_name");
        data.add("" + Public.playerName);
        data.add("_wifi");
        data.add("" + Public.wifiSN);
        queryHTTP.s_sendedDATA(data, true);
        queryHTTP.go();
    }

    public void getTopResultsFromDB() {
        //_queryHttp.s_httpURL("https://rewheeldev.000webhostapp.com/TappyDefender/get10.php");
        queryHTTP.s_httpURL("http://rewheeldev.eu5.org/TappySpace/get10/");
        ArrayList<String> data = new ArrayList<>();
        data.add("my_id");
        data.add("0");
        queryHTTP.s_sendedDATA(data, true);
        queryHTTP.go();
    }

    void sendTotalInfoToDB() {
    }
    //endregion
}
