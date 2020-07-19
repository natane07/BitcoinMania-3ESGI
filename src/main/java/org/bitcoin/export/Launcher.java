package org.bitcoin.export;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Launcher {
    public static void main(String[] args){
        SQL sql = new SQL();
        ArrayList<Map<String, Object>> arr = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("date","19-09-2019");
        map.put("quota",0.01);
        arr.add(map);
        arr.add(map);
        arr.add(map);
        arr.add(map);
        arr.add(map);
        arr.add(map);

        sql.createTable("toast", "date_truc", "quota");
        sql.insertTable("toast","date_truc","quota", arr);
        String path = System.getProperty("user.dir") + File.separator + "sql.sql";
        sql.writeSqlFile(path);
        System.out.println(sql.getFileContent());
    }
}