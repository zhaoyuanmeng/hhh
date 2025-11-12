package com.xaxc.teqin.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes({JSONObject.class})
public class JsonbTypeHandler extends BaseTypeHandler<JSONObject> {
    private static final PGobject jsonObject = new PGobject();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, JSONObject o, JdbcType jdbcType) throws SQLException {
        if (preparedStatement != null) {
            jsonObject.setType("jsonb");
            jsonObject.setValue(JSON.toJSONString(o));
            preparedStatement.setObject(i, jsonObject);
        }
    }

    @Override
    public JSONObject getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JSONObject.parseObject(resultSet.getString(s));
    }

    @Override
    public JSONObject getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JSON.parseObject(resultSet.getString(i));
    }

    @Override
    public JSONObject getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JSON.parseObject(callableStatement.getString(i));
    }
}
