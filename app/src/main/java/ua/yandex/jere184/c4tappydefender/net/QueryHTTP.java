package ua.yandex.jere184.c4tappydefender.net;

import android.util.Log;

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

import ua.yandex.jere184.c4tappydefender.util.MyLog;

//region lib  универсальные классы работы с данными
public class QueryHTTP extends Thread {
    //region CallBack
    public interface MyCallback {
        public void queryFinished(byte state);
    }

    private MyCallback myCallbackObj;
    //endregion
    public String logcatTag = "myOwnTAG";//"c_queryHTTP";
    //region public

    //region enums
    public static final class e_states {
        public static final byte _null = 0;//поток не инициализирован и не запущен
        public static final byte empty = 1;// поток свободен (не выполняет никаких запросов и данные из него забрали )
        public static final byte busy = 2; // (занят) в процессе выполнения запроса
        public static final byte finished = 3; // завершил отправку запроса и получил ответ (ждет когда заберут ответ)
        public static final byte killed = 99;// поток убит, при следующей итерации run он завершится
    }

    public static final class Errors {
        public static final byte notInitUrl = 10;
        public static final byte notInitSendData = 11;
        public static final byte notEmpty = 13;
        public static final byte iAmBusy = 13;
        public static final byte allInitOk = 15;
        public static final byte allOk = 15;
        public static final byte success = 15;
    }

    //endregion
    //region function activators
    public byte go() {
        if (!isRuned) {
            this.start();
        }
        if (state != e_states.empty) {
            return Errors.notEmpty;
        }
        if (isInit() != Errors.allInitOk) {
            return isInit();
        }
        //!!! еще какието важные проверки возможно
        timeGoPoint = System.currentTimeMillis();
        Log.d(logcatTag, "|||public Go() |||мс=" + (timeGoPoint - timePoint));
        isGo = true;
        return Errors.allInitOk;

    }

    //endregion
    //region Getters g_...
    public boolean g_isNeedGetDATA() {
        return isNeedGetData;
    }

    public JSONObject g_resultData_JsonObj() {
        state = e_states.empty;
        return resultDataJsonObj;
    }

    public byte g_state() {
        return state;
    }

    public String g_httpURL() {
        return httpURL;
    }

    //endregion
    //region Setters s_...
    public byte s_isNeedSendDATA(boolean isNeedSend) {
        if (isNeedSend == isNeedSendData)
            return Errors.success;
        if (state == e_states.busy) {
            return Errors.iAmBusy;
        }
        this.isNeedSendData = isNeedSend;
        return Errors.success;
    }

    public byte s_isNeedGetDATA(boolean isNeedGet) {
        if (isNeedGet == isNeedGetData)
            return Errors.success;
        if (state == e_states.busy) {
            return Errors.iAmBusy;
        }
        this.isNeedGetData = isNeedGet;
        return Errors.success;
    }

    public byte s_httpURL(String httpURL) {
        if (httpURL == this.httpURL)
            return Errors.success;
        if (state == e_states.busy) {
            return Errors.iAmBusy;
        }
        this.httpURL = httpURL;
        return Errors.success;
    }

    public byte s_sendedDATA(ArrayList<NameValuePair> DATA) {
        if (state == e_states.busy) {
            return Errors.iAmBusy;
        }
        this.sendedData = DATA;
        return Errors.success;
    }

    public byte s_sendedDATA(ArrayList<String> DATA, boolean asPairs) {
        if (state == e_states.busy) {
            return Errors.iAmBusy;
        }
        if (sendedData == null) sendedData = new ArrayList<>();
        sendedData.clear();
        if (asPairs) {
            for (int i = 0; i < DATA.size() - 1; i += 2) {
                sendedData.add(new BasicNameValuePair(DATA.get(i), DATA.get(i + 1)));
            }
        } else {
            for (int i = 0; i < DATA.size(); i++) {
                sendedData.add(new BasicNameValuePair("_" + i, DATA.get(i)));
            }
        }
        return Errors.success;
    }

    public byte s_isUseJSONObject(boolean isUseJSONObject) {
        if (isUseJSONObject == this.isUseJSONObject)
            return Errors.success;
        if (state == e_states.busy) {
            return Errors.iAmBusy;
        }
        this.isUseJSONObject = isUseJSONObject;
        return Errors.success;
    }

    public void setOnQueryFinished(MyCallback obj) {
        myCallbackObj = obj;
    }
    //endregion

    //endregion
    //region members _...
    //region flags
    private byte state = 0;
    private boolean isGo = false;
    private boolean isNeedSendData = false;
    private boolean isNeedGetData = false;
    private boolean isNeedGetContentString = false;
    private boolean isUseJSONObject = true;
    private boolean isRuned = false;//этот поток уже запускался
    //endregion
    private ArrayList<NameValuePair> sendedData = null;
    private String httpURL = null;
    private String resultContentStr = null;
    private JSONObject resultDataJsonObj;//полученый из запроса, обьект json

    private long timePoint;//удалить после тестов
    private long timeGoPoint;

    //endregion
    //region constructors
    public QueryHTTP(String httpURL) {
        MyLog l = new MyLog(logcatTag, "|||constructor", null);
        timePoint = System.currentTimeMillis();
        l.LT();
        state = e_states.empty;

        sendedData = null;

        this.httpURL = httpURL;

        isNeedSendData = false;

        isNeedGetData = false;

        isNeedGetContentString = true;

        isUseJSONObject = true;

    }

    public QueryHTTP(ArrayList<NameValuePair> inParams, String httpURL, boolean isNeedSendData,
                     boolean isNeedGetData, boolean isNeedGetContentString, boolean isUseJSONObject) {
        MyLog L = new MyLog(logcatTag, "|||constructor with params", null);

        timePoint = System.currentTimeMillis();
        L.LT();
        state = e_states.empty;

        sendedData = inParams;

        this.httpURL = httpURL;

        this.isNeedSendData = isNeedSendData;

        this.isNeedGetData = isNeedGetData;

        this.isNeedGetContentString = isNeedGetContentString;

        this.isUseJSONObject = isUseJSONObject;

    }

    //endregion
    @Override
    public void run()/*run this thread*/ {
        Log.d(logcatTag, "|||run in");
        isRuned = true;
        while (state != e_states.killed) {
            sleepThread();
            if (isGo) {
                try {
                    run_Go();
                } catch (Exception ex) {
                }
            }
        }
    }

    //region functions
    private void run_Go() {
        isGo = false;
        state = e_states.busy;
        //region создаем, описываем и отправляем запрос
        try {
            HttpClient httpclient = new DefaultHttpClient(); // создаем Http клиента
            HttpPost httppost = new HttpPost(httpURL); // создаем описание запроса с указанием адреса
            if (isNeedSendData) /*проверяем хочет ли пользователь данного класса прикреплять данные */ {
                httppost.setEntity(new UrlEncodedFormEntity(sendedData, "UTF-8"));// прикрепляем к описанию запроса данные
            }
            HttpResponse response = httpclient.execute(httppost);//выполням запрос
            if (isNeedGetData) /*проверяем хочет ли пользователь данного класса получить какойто ответ */ {
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
                resultContentStr = t_stringBuilder.toString();
            }
        } catch (Exception e) {
            Log.d(logcatTag, "|||run_GO 1 Exception |||" + e.getMessage());
        }
        //endregion

        //region создаем json из полученого _resultContentStr
        if (isNeedGetData && isUseJSONObject) /*проверяем хочет ли пользователь данного класса получить какойто ответ и преобразовать его в JSONObject*/ {
            if (isUseJSONObject) {
                try {
                    //region оставляем в строке тоько json подстроку
                    String _resultStr = resultContentStr.substring(resultContentStr.indexOf("{"), resultContentStr.indexOf("}") + 1);
                    //endregion
                    resultDataJsonObj = new JSONObject(_resultStr);
                } catch (Exception e) {
                    Log.d(logcatTag, "|||run_GO 2 Exception |||" + e.getMessage());
                }
            }
        }
        //endregion

        if (isNeedGetData)
            state = e_states.finished;
        else
            state = e_states.empty;
        if (resultContentStr != null)
            Log.d(logcatTag, "|||finished;| мс=" + (System.currentTimeMillis() - timeGoPoint) + " res=" + resultContentStr);
            //  + _resultContentStr.substring(_resultContentStr.indexOf("{"), _resultContentStr.indexOf("}") + 1));
        else
            Log.d(logcatTag, "|||finished;| мс=" + (System.currentTimeMillis() - timeGoPoint));
        if (myCallbackObj != null)
            myCallbackObj.queryFinished(state);
    }

    private byte isInit() {
        if (httpURL == null || httpURL.length() < 5) {
            return Errors.notInitUrl;
        } else if (isNeedSendData) {
            if (sendedData == null || sendedData.size() <= 0)
                return Errors.notInitSendData;
        }

        return Errors.allInitOk;
    }

    void sleepThread() {
        try {
            Thread.sleep(100);
        } catch (Exception ex) {
        }
    }
    //endregion
}

//endregion

