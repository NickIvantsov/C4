package ua.yandex.jere184.c4tappydefender.net

import android.util.Log
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.json.JSONObject
import ua.yandex.jere184.c4tappydefender.util.MyLog
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

//region lib  универсальные классы работы с данными
class QueryHTTP : Thread {
    //region CallBack
    interface MyCallback {
        fun queryFinished(state: Byte)
    }

    private var myCallbackObj: MyCallback? = null

    //endregion
    var logcatTag = "myOwnTAG" //"c_queryHTTP";

    //region public
    //region enums
    object States {
        const val _null: Byte = 0 //поток не инициализирован и не запущен
        const val empty: Byte =
            1 // поток свободен (не выполняет никаких запросов и данные из него забрали )
        const val busy: Byte = 2 // (занят) в процессе выполнения запроса
        const val finished: Byte =
            3 // завершил отправку запроса и получил ответ (ждет когда заберут ответ)
        const val killed: Byte = 99 // поток убит, при следующей итерации run он завершится
    }

    object Errors {
        const val notInitUrl: Byte = 10
        const val notInitSendData: Byte = 11
        const val notEmpty: Byte = 13
        const val iAmBusy: Byte = 13
        const val allInitOk: Byte = 15
        const val allOk: Byte = 15
        const val success: Byte = 15
    }

    //endregion
    //region function activators
    fun go(): Byte {
        if (!isRuned) {
            start()
        }
        if (state != States.empty) {
            return Errors.notEmpty
        }
        if (isInit != Errors.allInitOk) {
            return isInit
        }
        //!!! еще какието важные проверки возможно
        timeGoPoint = System.currentTimeMillis()
        Log.d(logcatTag, "|||public Go() |||мс=" + (timeGoPoint - timePoint))
        isGo = true
        return Errors.allInitOk
    }

    fun resultDataJsonObj(): JSONObject? {
        state = States.empty
        return resultDataJsonObj
    }

    fun g_state(): Byte {
        return state
    }

    //endregion
    //region Setters s_...
    fun isNeedSendData(isNeedSend: Boolean): Byte {
        if (isNeedSend == isNeedSendData) return Errors.success
        if (state == States.busy) {
            return Errors.iAmBusy
        }
        isNeedSendData = isNeedSend
        return Errors.success
    }

    fun setIsNeedGetData(isNeedGet: Boolean): Byte {
        if (isNeedGet == isNeedGetDATA) return Errors.success
        if (state == States.busy) {
            return Errors.iAmBusy
        }
        isNeedGetDATA = isNeedGet
        return Errors.success
    }

    fun setHttpURL(httpURL: String): Byte {
        if (httpURL === this.httpURL) return Errors.success
        if (state == States.busy) {
            return Errors.iAmBusy
        }
        this.httpURL = httpURL
        return Errors.success
    }

    fun s_sendedDATA(DATA: ArrayList<NameValuePair>?): Byte {
        if (state == States.busy) {
            return Errors.iAmBusy
        }
        sendedData = DATA
        return Errors.success
    }

    fun s_sendedDATA(DATA: ArrayList<String?>, asPairs: Boolean): Byte {
        if (state == States.busy) {
            return Errors.iAmBusy
        }
        if (sendedData == null) sendedData = ArrayList()
        sendedData!!.clear()
        if (asPairs) {
            var i = 0
            while (i < DATA.size - 1) {
                sendedData!!.add(BasicNameValuePair(DATA[i], DATA[i + 1]))
                i += 2
            }
        } else {
            for (i in DATA.indices) {
                sendedData!!.add(BasicNameValuePair("_$i", DATA[i]))
            }
        }
        return Errors.success
    }

    fun s_isUseJSONObject(isUseJSONObject: Boolean): Byte {
        if (isUseJSONObject == this.isUseJSONObject) return Errors.success
        if (state == States.busy) {
            return Errors.iAmBusy
        }
        this.isUseJSONObject = isUseJSONObject
        return Errors.success
    }

    fun setOnQueryFinished(obj: MyCallback?) {
        myCallbackObj = obj
    }

    //endregion
    //endregion
    //region members _...
    //region flags
    private var state: Byte = 0
    private var isGo = false
    private var isNeedSendData = false

    //endregion
    //region Getters g_...
    var isNeedGetDATA = false
        private set
    private var isNeedGetContentString = false
    private var isUseJSONObject = true
    private var isRuned = false //этот поток уже запускался

    //endregion
    private var sendedData: ArrayList<NameValuePair>? = null
    var httpURL: String? = null
        private set
    private var resultContentStr: String? = null
    private var resultDataJsonObj //полученый из запроса, обьект json
            : JSONObject? = null
    private var timePoint //удалить после тестов
            : Long
    private var timeGoPoint: Long = 0

    //endregion
    //region constructors
    constructor(httpURL: String?) {
        val l = MyLog(logcatTag, "|||constructor", null)
        timePoint = System.currentTimeMillis()
        l.LT()
        state = States.empty
        sendedData = null
        this.httpURL = httpURL
        isNeedSendData = false
        isNeedGetDATA = false
        isNeedGetContentString = true
        isUseJSONObject = true
    }

    constructor(
        inParams: ArrayList<NameValuePair>?, httpURL: String?, isNeedSendData: Boolean,
        isNeedGetData: Boolean, isNeedGetContentString: Boolean, isUseJSONObject: Boolean
    ) {
        val L = MyLog(logcatTag, "|||constructor with params", null)
        timePoint = System.currentTimeMillis()
        L.LT()
        state = States.empty
        sendedData = inParams
        this.httpURL = httpURL
        this.isNeedSendData = isNeedSendData
        isNeedGetDATA = isNeedGetData
        this.isNeedGetContentString = isNeedGetContentString
        this.isUseJSONObject = isUseJSONObject
    }

    //endregion
    override fun run() /*run this thread*/ {
        Log.d(logcatTag, "|||run in")
        isRuned = true
        while (state != States.killed) {
            sleepThread()
            if (isGo) {
                try {
                    runGo()
                } catch (ex: Exception) {
                }
            }
        }
    }

    //region functions
    private fun runGo() {
        isGo = false
        state = States.busy
        //region создаем, описываем и отправляем запрос
        try {
            val httpclient: HttpClient = DefaultHttpClient() // создаем Http клиента
            val httppost = HttpPost(httpURL) // создаем описание запроса с указанием адреса
            if (isNeedSendData) /*проверяем хочет ли пользователь данного класса прикреплять данные */ {
                httppost.entity = UrlEncodedFormEntity(
                    sendedData,
                    "UTF-8"
                ) // прикрепляем к описанию запроса данные
            }
            val response = httpclient.execute(httppost) //выполням запрос
            if (isNeedGetDATA) /*проверяем хочет ли пользователь данного класса получить какойто ответ */ {
                val t_entity =
                    response.entity // достаем из полученного ответа данные в виде HttpEntity
                val t_inputStream =
                    t_entity.content // из нашего HttpEntity получаем именно поток содержимого( в виде InputStream)
                val t_bufferedReader =
                    BufferedReader(InputStreamReader(t_inputStream, "UTF-8"), 8) //??? что за sz ?
                // считываем наш inputStream явно указываем кодировку UTF-8 и инициализируем BufferedReader
                val t_stringBuilder = StringBuilder() // создаем новый StringBuilder
                var _line: String
                while (t_bufferedReader.readLine()
                        .also { _line = it } != null
                ) { //считываем одну строку
                    t_stringBuilder.append(
                        """
    $_line
    
    """.trimIndent()
                    ) // помещаем строку в стринг билдер
                }
                t_inputStream.close() // закрываем поток
                resultContentStr = t_stringBuilder.toString()
            }
        } catch (e: Exception) {
            Log.d(logcatTag, "|||run_GO 1 Exception |||" + e.message)
        }
        //endregion

        //region создаем json из полученого _resultContentStr
        if (isNeedGetDATA && isUseJSONObject) /*проверяем хочет ли пользователь данного класса получить какойто ответ и преобразовать его в JSONObject*/ {
            if (isUseJSONObject) {
                try {
                    //region оставляем в строке тоько json подстроку
                    val _resultStr = resultContentStr!!.substring(
                        resultContentStr!!.indexOf("{"),
                        resultContentStr!!.indexOf("}") + 1
                    )
                    //endregion
                    resultDataJsonObj = JSONObject(_resultStr)
                } catch (e: Exception) {
                    Log.d(logcatTag, "|||run_GO 2 Exception |||" + e.message)
                }
            }
        }
        //endregion
        state = if (isNeedGetDATA) States.finished else States.empty
        if (resultContentStr != null) Log.d(
            logcatTag,
            "|||finished;| мс=" + (System.currentTimeMillis() - timeGoPoint) + " res=" + resultContentStr
        ) else Log.d(logcatTag, "|||finished;| мс=" + (System.currentTimeMillis() - timeGoPoint))
        if (myCallbackObj != null) myCallbackObj!!.queryFinished(state)
    }

    private val isInit: Byte
        private get() {
            if (httpURL == null || httpURL!!.length < 5) {
                return Errors.notInitUrl
            } else if (isNeedSendData) {
                if (sendedData == null || sendedData!!.size <= 0) return Errors.notInitSendData
            }
            return Errors.allInitOk
        }

    private fun sleepThread() {
        try {
            sleep(100)
        } catch (ex: Exception) {
        }
    } //endregion
}