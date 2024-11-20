package com.study.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
    static final String USER = "ebsoft";
    static final String PASS = "ebsoft";

    public static Connection getConnection() throws SQLException {
        // MySQL JDBC 드라이버를 명시적으로 로드 (MySQL 8.0 이상에서는 com.mysql.cj.jdbc.Driver 사용)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 데이터베이스 연결 설정
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
