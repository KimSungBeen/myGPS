package com.boxnet;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.boxnet.Util.getDate;

@Repository
public class UserDAO {
    final JdbcTemplate jt;

    public UserDAO(JdbcTemplate jt) {
        this.jt = jt;
    }

    /**
     * 유저 회원가입
     */
    public String selectUser(String id, String sns) {
        System.out.println("# select " + id + " Data #");
        return jt.query("select * from user where id = ? and sns = ?", new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {

                String id = rs.getString(1);
                String sns = rs.getString(2);
                String signUpDate = rs.getString(3);

                if (id != null && sns != null && signUpDate != null) {
                    return "SUCCESS";
                } else {
                    return "FAIL";
                }
            }
        }, id, sns);
    }

    /**
     * 유저 회원가입
     */
    public void insertUser(String id, String sns) {
        System.out.println("# Insert " + id + " Data #");
        jt.update("insert into user (id, sns, signUpDate) values (?, ?, ?)",
                id,
                sns,
                getDate());
    }
}
