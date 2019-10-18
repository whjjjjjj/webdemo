package com.eshanren.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.*;

import java.util.List;

/**
 * Created by Administrator on 2019/10/15.
 */
public class BaseService {

    protected <T extends Model>Page<T> findPage(T model, String countSql, String selectSql, Kv kv, int pageNumber, int pageSize){
        SqlPara sqlPara = Db.getSqlPara(countSql,kv);
        int count = Db.queryLong(sqlPara.getSql(),sqlPara.getPara()).intValue();
        int totalPage = count / pageSize + (count % pageSize == 0 ? 0 : 1);

        pageNumber = pageNumber <= 0 ? 1 : pageNumber ;

        kv.set("offset", (pageNumber - 1) * pageSize).set("limit",pageSize);
        sqlPara = Db.getSqlPara(selectSql,kv);
        List<T> list = model.find(sqlPara);
        return new Page<>(list,pageNumber,pageSize,totalPage,count);
    }

    protected Page<Record> findPage(String countSql, String selectSql, Kv kv, int pageNumber, int pageSize) {
        SqlPara sqlPara = Db.getSqlPara(countSql,kv);
        int count = Db.queryLong(sqlPara.getSql(),sqlPara.getPara()).intValue();
        int totalPage = count / pageSize + ( count % pageSize == 0 ? 0 : 1);

        pageNumber = pageNumber <= 0 ? 1 : pageNumber;
        kv.set("offset", (pageNumber - 1) * pageSize).set("limit",pageSize);
        sqlPara = Db.getSqlPara(selectSql,kv);
        List<Record> list = Db.find(sqlPara);
        return new Page<>(list,pageNumber,pageSize,totalPage,count);
    }

}
