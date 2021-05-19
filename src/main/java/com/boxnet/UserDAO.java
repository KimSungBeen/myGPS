package com.boxnet;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
    public UserDTO selectUser(String id, String sns) {
        System.out.println("# select " + id + " Data #");

        UserDTO userDTO = new UserDTO();
        return jt.query("select * from user where id = ? and sns = ?", (rs, rowNum) -> {
            userDTO.setId(rs.getString("id"));
            userDTO.setSns(rs.getString("sns"));
            userDTO.setSignUpDate(rs.getString("signUpDate"));

            return userDTO;
        }, id, sns).size() > 0 ? userDTO : null;
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
