package ua.yandex.jere184.c4tappydefender.net

import android.text.format.DateFormat
import android.widget.TextView
import ua.yandex.jere184.c4tappydefender.db.LocalDB
import ua.yandex.jere184.c4tappydefender.net.QueryHTTP.MyCallback
import ua.yandex.jere184.c4tappydefender.util.GlobalSettings
import ua.yandex.jere184.c4tappydefender.util.MyLog
import ua.yandex.jere184.c4tappydefender.util.Public
import java.util.*

/**
 * нативный класс работы с данными использует класы lib адаптируя их функции под контекст этого проэкта
 * (для каждого проэкта заново переписывается)
 */
class NativeWorkWithData : MyCallback {
    //region members
    @JvmField
    var tvLocalData: TextView? = null
    @JvmField
    var tvServData: TextView? = null

    //endregion
    fun addNewResult(dist: Float, time: Long) {
        insertLocalDB(dist, time)
        if (GlobalSettings.isSendAllResult) sendResultToDB(dist, time) else sendTotalInfoToDB()
    }

    //region SQLite
    fun readLocalDB() {
        localDB = LocalDB(Public.context!!) //инициализируем и подгружаем таблици из файла

        //region добавляем таблици если их еще нет (предположительно только первый запуск)
        localDB!!.addNewTable(localResultsTable, 3.toByte()) //datetime,dist,time
        localDB!!.addNewTable(servCopyResultsTable, 4.toByte()) //pos,dist,time,name
        //endregion

        //region get local
        var result = localDB!!.selectAll(localResultsTable)
        var str = "\t|\tDate\t|\tDistance\t|\tTime\n"
        tvLocalData!!.text = str
        for (row in result) {
            for (item in row) {
                str = "$str\t| $item "
            }
            str = """
                $str
                
                """.trimIndent()
        }
        tvLocalData!!.text = str
        //endregion

        //region get serv copy
        result = localDB!!.selectAll(servCopyResultsTable)
        str = "|\tPos\t|\tDist\t|\tTime\t|\tName\t|\n"
        tvServData!!.text = str
        for (row in result) {
            var id = row[0]
            if (id.length == 1) id = id + "\t"
            var dist = row[1]
            if (dist.length > 6) dist = dist.substring(0, 5)
            val time = row[2].substring(0, row[2].length - 3)
            var name = row[3]
            if (name.length > 15) name = name.substring(0, 14)
            str = "$str|\t$id\t|\t$dist\t|\t$time\t|\t$name\t|\n"
            //      for (String item : row) {
//        str = str + "|\t" + item + "\t";
//      }
//      str = str + "\n";
        }
        tvServData!!.text = str
        //endregion
    }

    fun insertLocalDB(dist: Float, time: Long) {
        val currentTimeStr = DateFormat.format("dd.MM hh:mm", Date(System.currentTimeMillis()))
            .toString()
        localDB!!.f_insert(localResultsTable, currentTimeStr, dist.toString(), time.toString())
    }

    //endregion
    //region _queryHttp
    private fun initHttp() {
        queryHTTP = QueryHTTP("")
        queryHTTP!!.setOnQueryFinished(this)
        queryHTTP!!.setIsNeedGetData(true)
        queryHTTP!!.isNeedSendData(true)
    }

    override fun queryFinished(state: Byte) /*Callback для получения данных*/ {
        if (queryHTTP!!.isNeedGetDATA) /*проверяет нужно ли этому запросу получать данные */ {
            //if (t_queryHttp.g_state() != c_queryHTTP.e_states.finished) return; // проверка статуса, но она излишся так как метод колбек вызывается сразу после установки статуса в finished
            val json = queryHTTP!!.resultDataJsonObj() //забираем данные из предыдущего запроса
            if (json != null) {
                if (queryHTTP!!.httpURL!!.contains("get10")) {
                    //c_Public._data.tv_servData.setText("");
                    localDB!!.clear(servCopyResultsTable) //вызываем функцию очистки локальной копии серверных данных, в качестве аргумента строка с названием таблицы
                    //_localDB.f_clear(_localResultsTable);
                    for (i in 1..10) {
                        try {
                            val position = json.getInt("pos_$i")
                            val name = json.getString("name_$i")
                            val distance = json.getDouble("dist_$i")
                            val time = json.getLong("time_$i")
                            //!!!
                            localDB!!.f_insert(
                                servCopyResultsTable,
                                position.toString(),
                                distance.toString(),
                                time.toString(),
                                name
                            )
                            //c_Public._data.tv_servData.setText(c_Public._data.tv_servData.getText() + "\t| " + position + "\t| " + name + "\t| "
                            //        + distance + "\t| " + time + " " + "\n");
                        } catch (e: Exception) {
                            MyLog(Public.myLogcatTAG, null, "queryFinished($state) ").L()
                        }
                    }
                }
            } else {
                MyLog(Public.myLogcatTAG, null, "queryFinished($state) JSONObject == null |||").L()
            }
        }
    }

    fun sendResultToDB(t_distance: Float, t_time: Long) {
        //_queryHttp.s_httpURL("https://rewheeldev.000webhostapp.com/TappyDefender/input.php");
        queryHTTP!!.setHttpURL("http://rewheeldev.eu5.org/TappySpace/insert/")
        val data = ArrayList<String?>()
        data.add("_distance")
        data.add("" + t_distance)
        data.add("_time")
        data.add("" + t_time)
        data.add("_name")
        data.add("" + Public.playerName)
        data.add("_wifi")
        data.add("" + Public.wifiSN)
        queryHTTP!!.s_sendedDATA(data, true)
        queryHTTP!!.go()
    }

    //_queryHttp.s_httpURL("https://rewheeldev.000webhostapp.com/TappyDefender/get10.php");
    val topResultsFromDB: Unit
        get() {
            //_queryHttp.s_httpURL("https://rewheeldev.000webhostapp.com/TappyDefender/get10.php");
            queryHTTP!!.setHttpURL("http://rewheeldev.eu5.org/TappySpace/get10/")
            val data = ArrayList<String?>()
            data.add("my_id")
            data.add("0")
            queryHTTP!!.s_sendedDATA(data, true)
            queryHTTP!!.go()
        }

    fun sendTotalInfoToDB() {} //endregion

    companion object {
        var queryHTTP: QueryHTTP? = null
        var localDB: LocalDB? = null
        var localResultsTable = "LocalResults"
        var servCopyResultsTable = "ServCopyResults"
    }

    //endregion
    //region constructors
    init {
        initHttp()
    }
}