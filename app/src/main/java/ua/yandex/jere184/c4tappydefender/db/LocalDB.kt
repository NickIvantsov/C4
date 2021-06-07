package ua.yandex.jere184.c4tappydefender.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.*

class LocalDB(  // ссылка на контекст приложения
    var context: Context
) {
    inner class c_TableInfo(var _tableName: String, var _countCollums: Byte)

    //endregion
    //region functions
    fun f_insert(tableName: String, vararg array: String) {
        val table = getTableInfo(tableName)
        beginTable(table)
        var t_str = "INSERT INTO " + table!!._tableName + " VALUES ("
        for (i in 0 until array.size) {
            if (i != 0) t_str = "$t_str,"
            t_str = t_str + "'" + array[i] + "'"
        }
        t_str = "$t_str);"
        db!!.execSQL(t_str)
        db!!.close()
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
    fun selectAll(tableName: String): ArrayList<ArrayList<String>> {
        val table = getTableInfo(tableName)
        beginTable(table)
        val query = db!!.rawQuery("SELECT * FROM " + table!!._tableName + " ;", null)
        val returned = ArrayList<ArrayList<String>>()
        if (query.moveToFirst()) {
            do {
                val line = ArrayList<String>()
                for (i in 0 until table._countCollums) {
                    line.add(query.getString(i))
                }
                returned.add(line)
            } while (query.moveToNext())
        }
        query.close()
        db!!.close()
        return returned
    }

    fun clear(tableName: String) {
        val table = getTableInfo(tableName)
        beginTable(table)
        //Cursor query = db.rawQuery("DELETE FROM " + table._tableName + " ;", null);
        //query.close();
        db!!.delete(tableName, "", null)
        db!!.close()
    }

    fun addNewTable(name: String, countCollums: Byte) {
        if (getTableInfo(name) == null) {
            tables.add(c_TableInfo(name, countCollums))
            saveTablesInfo()
        }
    }

    //endregion
    //endregion
    //region members
    var db: SQLiteDatabase? = null
    var tables = ArrayList<c_TableInfo>()

    //endregion
    //region private functions
    private fun getTableInfo(tableName: String): c_TableInfo? {
        for (i in tables.indices) {
            val t_tableInfo = tables[i]
            if (t_tableInfo._tableName === tableName) return t_tableInfo
        }
        return null
    }

    private fun beginTable(table: c_TableInfo?) {
        db = context.openOrCreateDatabase("c_localDB.db", Context.MODE_PRIVATE, null)
        var t_str = "CREATE TABLE IF NOT EXISTS " + table!!._tableName + "( "
        for (i in 0 until table._countCollums) {
            if (i != 0) t_str = "$t_str,"
            t_str = t_str + "_" + i + "_ TEXT"
        }
        t_str = "$t_str)"
        db!!.execSQL(t_str)
    }

    private fun saveTablesInfo() {
        db = context.openOrCreateDatabase("c_localDB.db", Context.MODE_PRIVATE, null)
        db!!.execSQL("CREATE TABLE IF NOT EXISTS c_localDBTables( table_name TEXT, collums INTEGER)")
        db!!.execSQL("DELETE from c_localDBTables ")
        for (i in tables.indices) {
            db!!.execSQL("INSERT INTO c_localDBTables VALUES ( '" + tables[i]._tableName + "', " + tables[i]._countCollums + ");")
        }
        db!!.close()
    }

    private fun loadTablesInfo() {
        db = context.openOrCreateDatabase("c_localDB.db", Context.MODE_PRIVATE, null)
        db!!.execSQL("CREATE TABLE IF NOT EXISTS c_localDBTables( table_name TEXT, collums INTEGER)")
        val query = db!!.rawQuery("SELECT * FROM c_localDBTables;", null)
        if (query.moveToFirst()) {
            tables.clear()
            do {
                tables.add(c_TableInfo(query.getString(0), query.getInt(1).toByte()))
            } while (query.moveToNext())
        }
        query.close()
        db!!.close()
    } //endregion

    //region public
    //region constructors
    init {
        loadTablesInfo()
    }
}