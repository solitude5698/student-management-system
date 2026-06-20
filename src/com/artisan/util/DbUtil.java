package com.artisan.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
	private String dbUrl="jdbc:mysql://localhost:3306/student?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai"; 
	private String dbUserName="root"; 
	private String dbPassword="123"; 
	private String jdbcName="com.mysql.cj.jdbc.Driver"; 
	
	public Connection getCon(){
		try {
			Class.forName(jdbcName);
		} catch (ClassNotFoundException e) {
			// 捕获类找不到异常
			e.printStackTrace();
		}
		Connection con = null;
		try {
			con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
		} catch (SQLException e) {
			// 捕获SQL异常
			e.printStackTrace();
		}
		return con;
	}
	
	public void closeCon(Connection con)throws Exception{
		if(con!=null){
			con.close();
		}
	}
	
	public static void main(String[] args) {
		DbUtil dbUtil=new DbUtil();
		try {
			dbUtil.getCon();
			System.out.println("数据库连接成功");
		} catch (Exception e) {
			// 捕获连接异常
			e.printStackTrace();
			System.out.println("数据库连接失败");
		}
	}
}