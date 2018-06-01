package ua.yandex.jere184.c4tappydefender;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

//region lib  универсальные классы работы с данными
public class c_queryHTTP extends Thread {
  //region CallBack
  public interface myCallback {
    public void queryFinished(byte state);
  }

  private myCallback _myCallbackObj;
  //endregion
  public String Logcat_TAG = "myOwnTAG";//"c_queryHTTP";
  //region public

  //region enums
  public static final class e_states {
    public static final byte _null = 0;//поток не инициализирован и не запущен
    public static final byte empty = 1;// поток свободен (не выполняет никаких запросов и данные из него забрали )
    public static final byte busy = 2; // (занят) в процессе выполнения запроса
    public static final byte finished = 3; // завершил отправку запроса и получил ответ (ждет когда заберут ответ)
    public static final byte killed = 99;// поток убит, при следующей итерации run он завершится
  }

  public static final class e_errors {
    public static final byte notInit_url = 10;
    public static final byte notInit_sendDATA = 11;
    public static final byte notEmpty = 13;
    public static final byte iAmBusy = 13;
    public static final byte allInitOk = 15;
    public static final byte allOk = 15;
    public static final byte success = 15;
  }
  //endregion
  //region function activators
  public byte Go() {
    if (!is_runed) {
      this.start();
    }
    if (_state != e_states.empty) {
      return e_errors.notEmpty;
    }
    if (is_init() != e_errors.allInitOk) {
      return is_init();
    }
    //!!! еще какието важные проверки возможно
    t_timeGoPoint = System.currentTimeMillis();
    Log.d(Logcat_TAG, "|||public Go() |||мс=" + (t_timeGoPoint - t_timePoint));
    is_Go = true;
    return e_errors.allInitOk;

  }
  //endregion
  //region Getters g_...
  public boolean g_isNeedGetDATA() {
    return is_NeedGetDATA;
  }
  public JSONObject g_resultData_JsonObj() {
    _state = e_states.empty;
    return _resultData_JsonObj;
  }
  public byte g_state() {
    return _state;
  }
  public String g_httpURL() {
    return _httpURL;
  }
  //endregion
  //region Setters s_...
  public byte s_isNeedSendDATA(boolean isNeedSend) {
    if (isNeedSend == is_NeedSendDATA)
      return e_errors.success;
    if (_state == e_states.busy) {
      return e_errors.iAmBusy;
    }
    this.is_NeedSendDATA = isNeedSend;
    return e_errors.success;
  }
  public byte s_isNeedGetDATA(boolean isNeedGet) {
    if (isNeedGet == is_NeedGetDATA)
      return e_errors.success;
    if (_state == e_states.busy) {
      return e_errors.iAmBusy;
    }
    this.is_NeedGetDATA = isNeedGet;
    return e_errors.success;
  }
  public byte s_httpURL(String httpURL) {
    if (httpURL == _httpURL)
      return e_errors.success;
    if (_state == e_states.busy) {
      return e_errors.iAmBusy;
    }
    this._httpURL = httpURL;
    return e_errors.success;
  }
  public byte s_sendedDATA(ArrayList<NameValuePair> DATA) {
    if (_state == e_states.busy) {
      return e_errors.iAmBusy;
    }
    this._sendedDATA = DATA;
    return e_errors.success;
  }
  public byte s_sendedDATA(ArrayList<String> DATA, boolean asPairs) {
    if (_state == e_states.busy) {
      return e_errors.iAmBusy;
    }
    if (_sendedDATA == null) _sendedDATA = new ArrayList<>();
    _sendedDATA.clear();
    if (asPairs) {
      for (int i = 0; i < DATA.size() - 1; i += 2) {
        _sendedDATA.add(new BasicNameValuePair(DATA.get(i), DATA.get(i + 1)));
      }
    } else {
      for (int i = 0; i < DATA.size(); i++) {
        _sendedDATA.add(new BasicNameValuePair("_" + i, DATA.get(i)));
      }
    }
    return e_errors.success;
  }
  public byte s_isUseJSONObject(boolean isUseJSONObject) {
    if (isUseJSONObject == is_useJSONObject)
      return e_errors.success;
    if (_state == e_states.busy) {
      return e_errors.iAmBusy;
    }
    this.is_useJSONObject = isUseJSONObject;
    return e_errors.success;
  }
  public void setOnQueryFinished(myCallback obj) {
    _myCallbackObj = obj;
  }
  //endregion

  //endregion
  //region members _...
  //region flags
  private byte _state = 0;
  private boolean is_Go = false;
  private boolean is_NeedSendDATA = false;
  private boolean is_NeedGetDATA = false;
  private boolean is_NeedGetContentString = false;
  private boolean is_useJSONObject = true;
  private boolean is_runed = false;//этот поток уже запускался
  //endregion
  private ArrayList<NameValuePair> _sendedDATA = null;
  private String _httpURL = null;
  private String _resultContentStr = null;
  private JSONObject _resultData_JsonObj;//полученый из запроса, обьект json

  private long t_timePoint;//удалить после тестов 
  private long t_timeGoPoint;
  //endregion
  //region constructors
  public c_queryHTTP(String httpURL) {
    c_myLog L = new c_myLog(Logcat_TAG, "|||constructor", null);
    t_timePoint = System.currentTimeMillis();
    L.LT();
    _state = e_states.empty;

    _sendedDATA = null;

    _httpURL = httpURL;

    is_NeedSendDATA = false;

    is_NeedGetDATA = false;

    is_NeedGetContentString = true;

    is_useJSONObject = true;

  }
  public c_queryHTTP(ArrayList<NameValuePair> inParams, String httpURL, boolean isNeedSendData,
                     boolean isNeedGetData, boolean isNeedGetContentString, boolean isUseJSONObject) {
    c_myLog L = new c_myLog(Logcat_TAG, "|||constructor with params", null);

    t_timePoint = System.currentTimeMillis();
    L.LT();
    _state = e_states.empty;

    _sendedDATA = inParams;

    _httpURL = httpURL;

    is_NeedSendDATA = isNeedSendData;

    is_NeedGetDATA = isNeedGetData;

    is_NeedGetContentString = isNeedGetContentString;

    is_useJSONObject = isUseJSONObject;

  }

  //endregion
  @Override
  public void run()/*run this thread*/ {
    Log.d(Logcat_TAG, "|||run in");
    is_runed = true;
    while (_state != e_states.killed) {
      sleepThread();
      if (is_Go) {
        try {
          run_Go();
        } catch (Exception ex) {
        }
      }
    }
  }
  //region functions
  private void run_Go() {
    is_Go = false;
    _state = e_states.busy;
    //region создаем, описываем и отправляем запрос
    try {
      HttpClient httpclient = new DefaultHttpClient(); // создаем Http клиента
      HttpPost httppost = new HttpPost(_httpURL); // создаем описание запроса с указанием адреса
      if (is_NeedSendDATA) /*проверяем хочет ли пользователь данного класса прикреплять данные */ {
        httppost.setEntity(new UrlEncodedFormEntity(_sendedDATA, "UTF-8"));// прикрепляем к описанию запроса данные
      }
      HttpResponse response = httpclient.execute(httppost);//выполням запрос
      if (is_NeedGetDATA) /*проверяем хочет ли пользователь данного класса получить какойто ответ */ {
        HttpEntity t_entity = response.getEntity(); // достаем из полученного ответа данные в виде HttpEntity
        InputStream t_inputStream = t_entity.getContent(); // из нашего HttpEntity получаем именно поток содержимого( в виде InputStream)
        BufferedReader t_bufferedReader = new BufferedReader(new InputStreamReader(t_inputStream, "UTF-8"), 8);//??? что за sz ?
        // считываем наш inputStream явно указываем кодировку UTF-8 и инициализируем BufferedReader
        StringBuilder t_stringBuilder = new StringBuilder();// создаем новый StringBuilder
        String _line;
        while ((_line = t_bufferedReader.readLine()) != null) {//считываем одну строку
          t_stringBuilder.append(_line + "\n"); // помещаем строку в стринг билдер
        }
        t_inputStream.close(); // закрываем поток
        _resultContentStr = t_stringBuilder.toString();
      }
    } catch (Exception e) {
      Log.d(Logcat_TAG, "|||run_GO 1 Exception |||" + e.getMessage());
    }
    //endregion

    //region создаем json из полученого _resultContentStr
    if (is_NeedGetDATA && is_useJSONObject) /*проверяем хочет ли пользователь данного класса получить какойто ответ и преобразовать его в JSONObject*/ {
      if (is_useJSONObject) {
        try {
          //region оставляем в строке тоько json подстроку
          String _resultStr = _resultContentStr.substring(_resultContentStr.indexOf("{"), _resultContentStr.indexOf("}") + 1);
          //endregion
          _resultData_JsonObj = new JSONObject(_resultStr);
        } catch (Exception e) {
          Log.d(Logcat_TAG, "|||run_GO 2 Exception |||" + e.getMessage());
        }
      }
    }
    //endregion

    if (is_NeedGetDATA)
      _state = e_states.finished;
    else
      _state = e_states.empty;
    if (_resultContentStr != null)
      Log.d(Logcat_TAG, "|||finished;| мс=" + (System.currentTimeMillis() - t_timeGoPoint) + " res=" + _resultContentStr);
      //  + _resultContentStr.substring(_resultContentStr.indexOf("{"), _resultContentStr.indexOf("}") + 1));
    else
      Log.d(Logcat_TAG, "|||finished;| мс=" + (System.currentTimeMillis() - t_timeGoPoint));
    if (_myCallbackObj != null)
      _myCallbackObj.queryFinished(_state);
  }
  private byte is_init() {
    if (_httpURL == null || _httpURL.length() < 5) {
      return e_errors.notInit_url;
    } else if (is_NeedSendDATA) {
      if (_sendedDATA == null || _sendedDATA.size() <= 0)
        return e_errors.notInit_sendDATA;
    }

    return e_errors.allInitOk;
  }
  void sleepThread() {
    try {
      Thread.sleep(100);
    } catch (Exception ex) {
    }
  }
  //endregion
}

class c_localDB {
  private class c_TableInfo {
    String _tableName;
    byte _countCollums;

    public c_TableInfo(String name, byte countCollums) {
      _tableName = name;
      _countCollums = countCollums;
    }
  }

  //region public

  //region constructors
  public c_localDB(Context context) {
    _context = context;
    f_loadTablesInfo();
  }
  //endregion

  //region functions
  public void f_insert(String tableName, String... array) {

    c_TableInfo table = f_getTableInfo(tableName);
    f_beginTable(table);
    String t_str = "INSERT INTO " + table._tableName + " VALUES (";
    for (int i = 0; i < array.length; i++) {
      if (i != 0)
        t_str = t_str + ",";
      t_str = t_str + "'" + array[i] + "'";
    }
    t_str = t_str + ");";

    db.execSQL(t_str);
    db.close();
  }
  //region
  /*public void f_update(String tableName, byte where_num, String where_val, String newArray) {//!!!!
//"UPDATE touch_speed_connect SET coordX='".$coordX."', coordY='".$coordY."' WHERE str_id ='test'"
    c_TableInfo table = f_getTableInfo(tableName);
    f_beginTable(table);
    String t_str = "UPDATE " + table._tableName + " SET ";
    for (int i = 0; i < array.length; i++) {
      if (i != 0)
        t_str = t_str + ",";
      t_str = t_str + "'" + array[i] + "'";
    }
    t_str = t_str + ");";

    db.execSQL(t_str);
    db.close();
  }*/
  //endregion
  public ArrayList<ArrayList<String>> f_selectAll(String tableName) {
    c_TableInfo table = f_getTableInfo(tableName);
    f_beginTable(table);
    Cursor query = db.rawQuery("SELECT * FROM " + table._tableName + " ;", null);
    ArrayList<ArrayList<String>> returned = new ArrayList<>();
    if (query.moveToFirst()) {
      do {
        ArrayList<String> line = new ArrayList<>();
        for (int i = 0; i < table._countCollums; i++) {
          line.add(query.getString(i));
        }
        returned.add(line);
      } while (query.moveToNext());
    }
    query.close();
    db.close();
    return returned;
  }
  public void f_clear(String tableName) {
    c_TableInfo table = f_getTableInfo(tableName);
    f_beginTable(table);
    //Cursor query = db.rawQuery("DELETE FROM " + table._tableName + " ;", null);
    //query.close();
    db.delete(tableName, "", null);
    db.close();
  }

  public void addNewTable(String name, byte countCollums) {
    if (f_getTableInfo(name) == null) {
      _tables.add(new c_TableInfo(name, countCollums));
      f_saveTablesInfo();
    }
  }
  //endregion

  //endregion

  //region members
  SQLiteDatabase db;
  Context _context;// ссылка на контекст приложения
  ArrayList<c_TableInfo> _tables = new ArrayList<>();
  //endregion

  //region private functions
  private c_TableInfo f_getTableInfo(String tableName) {
    for (int i = 0; i < _tables.size(); i++) {
      c_TableInfo t_tableInfo = _tables.get(i);
      if (t_tableInfo._tableName == tableName)
        return t_tableInfo;
    }
    return null;
  }
  private void f_beginTable(c_TableInfo table) {
    db = _context.openOrCreateDatabase("c_localDB.db", MODE_PRIVATE, null);
    String t_str = "CREATE TABLE IF NOT EXISTS " + table._tableName + "( ";
    for (int i = 0; i < table._countCollums; i++) {
      if (i != 0)
        t_str = t_str + ",";
      t_str = t_str + "_" + i + "_ TEXT";
    }
    t_str = t_str + ")";
    db.execSQL(t_str);
  }

  private void f_saveTablesInfo() {
    db = _context.openOrCreateDatabase("c_localDB.db", MODE_PRIVATE, null);
    db.execSQL("CREATE TABLE IF NOT EXISTS c_localDBTables( table_name TEXT, collums INTEGER)");
    db.execSQL("DELETE from c_localDBTables ");

    for (int i = 0; i < _tables.size(); i++) {
      db.execSQL("INSERT INTO c_localDBTables VALUES ( '" + _tables.get(i)._tableName + "', " + _tables.get(i)._countCollums + ");");
    }
    db.close();
  }
  private void f_loadTablesInfo() {
    db = _context.openOrCreateDatabase("c_localDB.db", MODE_PRIVATE, null);
    db.execSQL("CREATE TABLE IF NOT EXISTS c_localDBTables( table_name TEXT, collums INTEGER)");
    Cursor query = db.rawQuery("SELECT * FROM c_localDBTables;", null);
    if (query.moveToFirst()) {
      _tables.clear();
      do {
        _tables.add(new c_TableInfo(query.getString(0), (byte) query.getInt(1)));
      } while (query.moveToNext());
    }
    query.close();
    db.close();
  }
  //endregion
}
//endregion

class c_GlobalSettings/*пока что не отвлекаемся на него не реализаваная идея*/ {
  public static boolean is_SendAllResult = true;
}

/**
 * нативный класс работы с данными использует класы lib адаптируя их функции под контекст этого проэкта
 * (для каждого проэкта заново переписывается)
 */
class c_nativWorkWithData implements c_queryHTTP.myCallback {
  //region members
  TextView tv_localData;
  TextView tv_servData;
  public static c_queryHTTP _queryHttp;
  public static c_localDB _localDB;
  public static String _localResultsTable = "LocalResults";
  public static String _servCopyResultsTable = "ServCopyResults";
  //endregion

  //region constructors
  public c_nativWorkWithData() {
    initHttp();

  }
  //endregion

  public void f_addNewResult(float dist, long time) {
    f_insertLocalDB(dist, time);
    if (c_GlobalSettings.is_SendAllResult)
      f_sendResultToDB(dist, time);
    else
      f_sendTotalInfoToDB();
  }

  //region SQLite
  public void f_readLocalDB() {
    _localDB = new c_localDB(c_Public._context);//инициализируем и подгружаем таблици из файла 

    //region добавляем таблици если их еще нет (предположительно только первый запуск)
    _localDB.addNewTable(_localResultsTable, (byte) 3);//datetime,dist,time
    _localDB.addNewTable(_servCopyResultsTable, (byte) 4);//pos,dist,time,name
    //endregion

    //region get local
    ArrayList<ArrayList<String>> t_result = _localDB.f_selectAll(_localResultsTable);
    String t_str = "\t|\tDate\t|\tDistance\t|\tTime\n";
    tv_localData.setText(t_str);
    for (ArrayList<String> row : t_result) {
      for (String item : row) {
        t_str = t_str + "\t| " + item + " ";
      }
      t_str = t_str + "\n";
    }
    tv_localData.setText(t_str);
    //endregion

    //region get serv copy
    t_result = _localDB.f_selectAll(_servCopyResultsTable);
    t_str = "|\tPos\t|\tDist\t|\tTime\t|\tName\t|\n";
    tv_servData.setText(t_str);
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
    tv_servData.setText(t_str);
    //endregion
  }
  public void f_insertLocalDB(float dist, long time) {
    String t_currentTimeStr = (android.text.format.DateFormat.format("dd.MM hh:mm", new Date(System.currentTimeMillis()))).toString();
    _localDB.f_insert(_localResultsTable, t_currentTimeStr, String.valueOf(dist), String.valueOf(time));
  }
  //endregion

  //region _queryHttp
  private void initHttp() {
    _queryHttp = new c_queryHTTP("");
    _queryHttp.setOnQueryFinished(this);
    _queryHttp.s_isNeedGetDATA(true);
    _queryHttp.s_isNeedSendDATA(true);

  }
  @Override
  public void queryFinished(byte state)/*Callback для получения данных*/ {
    if (_queryHttp.g_isNeedGetDATA()) /*проверяет нужно ли этому запросу получать данные */ {
      //if (t_queryHttp.g_state() != c_queryHTTP.e_states.finished) return; // проверка статуса, но она излишся так как метод колбек вызывается сразу после установки статуса в finished
      JSONObject json = _queryHttp.g_resultData_JsonObj(); //забираем данные из предыдущего запроса
      if (json != null) {
        if (_queryHttp.g_httpURL().contains("get10")) {
          //c_Public._data.tv_servData.setText("");
          _localDB.f_clear(_servCopyResultsTable);//вызываем функцию очистки локальной копии серверных данных, в качестве аргумента строка с названием таблицы
          //_localDB.f_clear(_localResultsTable);
          for (int i = 1; i <= 10; i++) {
            try {
              int position = json.getInt("pos_" + i);
              String name = json.getString("name_" + i);
              double distance = json.getDouble("dist_" + i);
              long time = json.getLong("time_" + i);
//!!!
              _localDB.f_insert(_servCopyResultsTable, String.valueOf(position),
                      String.valueOf(distance), String.valueOf(time), name);
              //c_Public._data.tv_servData.setText(c_Public._data.tv_servData.getText() + "\t| " + position + "\t| " + name + "\t| "
              //        + distance + "\t| " + time + " " + "\n");
            } catch (Exception e) {
              new c_myLog(c_Public._myLogcatTAG, null, "queryFinished(" + state + ") ").L();
            }
          }
        }
      }
      else {
        new c_myLog(c_Public._myLogcatTAG, null, "queryFinished(" + state + ") JSONObject == null |||").L();}
    }

  }
  public void f_sendResultToDB(float t_distance, long t_time) {
    //_queryHttp.s_httpURL("https://rewheeldev.000webhostapp.com/TappyDefender/input.php");
    _queryHttp.s_httpURL("http://rewheeldev.eu5.org/TappySpace/insert/");
    ArrayList<String> data = new ArrayList<>();
    data.add("_distance");
    data.add("" + t_distance);
    data.add("_time");
    data.add("" + t_time);
    data.add("_name");
    data.add("" + c_Public._PlayerName);
    data.add("_wifi");
    data.add("" + c_Public._wifiSN);
    _queryHttp.s_sendedDATA(data, true);
    _queryHttp.Go();
  }
  public void f_getTopResultsFromDB() {
    //_queryHttp.s_httpURL("https://rewheeldev.000webhostapp.com/TappyDefender/get10.php");
    _queryHttp.s_httpURL("http://rewheeldev.eu5.org/TappySpace/get10/");
    ArrayList<String> data = new ArrayList<>();
    data.add("my_id");
    data.add("0");
    _queryHttp.s_sendedDATA(data, true);
    _queryHttp.Go();
  }
  void f_sendTotalInfoToDB() {
  }
  //endregion
}
