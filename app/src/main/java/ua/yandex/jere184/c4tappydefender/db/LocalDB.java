package ua.yandex.jere184.c4tappydefender.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class LocalDB {
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
    public LocalDB(Context context) {
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
