package com.artisan.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.artisan.model.Admin;
import com.artisan.model.Student;
import com.artisan.model.StudentClass;
import com.artisan.util.StringUtil;

public class StudentDao extends BaseDao {
	public boolean addStudent(Student student){
		String sql = "insert into s_student values(null,?,?,?,?)";
		try {
			java.sql.PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, student.getName());
			preparedStatement.setInt(2, student.getClassId());
			preparedStatement.setString(3, student.getPassword());
			preparedStatement.setString(4, student.getSex());
			if(preparedStatement.executeUpdate() > 0)return true;
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}
	public List<Student> getStudentList(Student student){
		List<Student> retList = new ArrayList<Student>();
		StringBuffer sqlString = new StringBuffer("select * from s_student");
		if(!StringUtil.isEmpty(student.getName())){
			sqlString.append(" and name like '%"+student.getName()+"%'");
		}
		if(student.getClassId() != 0){
			sqlString.append(" and classId ="+student.getClassId());
		}
		try {
			PreparedStatement preparedStatement = con.prepareStatement(sqlString.toString().replaceFirst("and", "where"));
			ResultSet executeQuery = preparedStatement.executeQuery();
			while(executeQuery.next()){
				Student s = new Student();
				s.setId(executeQuery.getInt("id"));
				s.setName(executeQuery.getString("name"));
				s.setClassId(executeQuery.getInt("classId"));
				s.setSex(executeQuery.getString("sex"));
				s.setPassword(executeQuery.getString("password"));
				retList.add(s);
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return retList;
	}
	public boolean delete(int id){
		try {
			con.setAutoCommit(false);
			String deleteAttendanceSql = "delete from s_attendance where student_id=?";
			try (PreparedStatement ps0 = con.prepareStatement(deleteAttendanceSql)) {
				ps0.setInt(1, id);
				ps0.executeUpdate();
			}
			String deleteSelectedCourseSql = "delete from s_selected_course where student_id=?";
			try (PreparedStatement ps1 = con.prepareStatement(deleteSelectedCourseSql)) {
				ps1.setInt(1, id);
				ps1.executeUpdate();
			}
			String deleteScoreSql = "delete from s_score where student_id=?";
			try (PreparedStatement ps2 = con.prepareStatement(deleteScoreSql)) {
				ps2.setInt(1, id);
				ps2.executeUpdate();
			}
			String deleteStudentSql = "delete from s_student where id=?";
			try (PreparedStatement ps3 = con.prepareStatement(deleteStudentSql)) {
				ps3.setInt(1, id);
				int result = ps3.executeUpdate();
				con.commit();
				con.setAutoCommit(true);
				return result > 0;
			}
		} catch (SQLException e) {
			try {
				if (con != null) {
					con.rollback();
					con.setAutoCommit(true);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}
	public boolean update(Student student){
		String sql = "update s_student set name=?, classId=?,sex=?,password=? where id=?";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, student.getName());
			preparedStatement.setInt(2, student.getClassId());
			preparedStatement.setString(3, student.getSex());
			preparedStatement.setString(4, student.getPassword());
			preparedStatement.setInt(5, student.getId());
			if(preparedStatement.executeUpdate() > 0){
				return true;
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}
	public String editPassword(Student student,String newPassword){
		String sql = "select * from s_student where id=? and password=?";
		PreparedStatement prst = null;
		int id = 0;
		try {
			prst = con.prepareStatement(sql);
			prst.setInt(1, student.getId());
			prst.setString(2, student.getPassword());
			ResultSet executeQuery = prst.executeQuery();
			if(!executeQuery.next()){
				String retString = "原密码错误";
				return retString;
			}
			id = executeQuery.getInt("id");
		} catch (SQLException e1) {
			// 自动生成的 catch 块
			e1.printStackTrace();
		}//将sql语句发送到数据库执行
		String retString = "修改失败";
		String sqlString = "update s_student set password = ? where id = ?";
		try {
			prst = con.prepareStatement(sqlString);
			prst.setString(1, newPassword);
			prst.setInt(2, id);
			int rst = prst.executeUpdate();
			if(rst > 0){
				retString = "密码修改成功！";
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}//将sql语句发送到数据库执行
		return retString;
	}
	public Student login(Student student){
		String sql = "select * from s_student where name=? and password=?";
		Student studentRst = null;
		try {
			PreparedStatement prst = con.prepareStatement(sql);//将sql语句发送到数据库执行
			prst.setString(1, student.getName());
			prst.setString(2, student.getPassword());
			ResultSet executeQuery = prst.executeQuery();
			if(executeQuery.next()){
				studentRst = new Student();
				studentRst.setId(executeQuery.getInt("id"));
				studentRst.setClassId(executeQuery.getInt("classId"));
				studentRst.setName(executeQuery.getString("name"));
				studentRst.setPassword(executeQuery.getString("password"));
				studentRst.setSex(executeQuery.getString("sex"));
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return studentRst;
	}
}