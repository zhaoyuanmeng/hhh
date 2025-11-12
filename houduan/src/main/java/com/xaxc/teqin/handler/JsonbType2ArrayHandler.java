package com.xaxc.teqin.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes({JSONArray.class})
public class JsonbType2ArrayHandler extends BaseTypeHandler<JSONArray> {
    private static final PGobject jsonObject = new PGobject();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, JSONArray o, JdbcType jdbcType) throws SQLException {
        if (preparedStatement != null) {
            jsonObject.setType("jsonb");
            jsonObject.setValue(JSON.toJSONString(o));
            preparedStatement.setObject(i, jsonObject);
        }
    }

    @Override
    public JSONArray getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JSON.parseArray(resultSet.getString(s));
    }

    @Override
    public JSONArray getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JSON.parseArray(resultSet.getString(i));
    }

    @Override
    public JSONArray getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JSON.parseArray(callableStatement.getString(i));
    }
}
