package com.artisan.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.artisan.model.Student;
import com.artisan.model.Teacher;
import com.artisan.util.StringUtil;

public class TeacherDao extends BaseDao {
	public boolean addTeacher(Teacher teacher){
		String sql = "insert into s_teacher values(null,?,?,?,?,?)";
		try {
			java.sql.PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, teacher.getName());
			preparedStatement.setString(2, teacher.getSex());
			preparedStatement.setString(3, teacher.getTitle());
			preparedStatement.setInt(4, teacher.getAge());
			preparedStatement.setString(5, teacher.getPassword());
			if(preparedStatement.executeUpdate() > 0)return true;
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}

	public List<Teacher> getTeacherList(Teacher teacher) {
		// 自动生成的方法存根
		List<Teacher> retList = new ArrayList<Teacher>();
		StringBuffer sqlString = new StringBuffer("select * from s_teacher");
		if(!StringUtil.isEmpty(teacher.getName())){
			sqlString.append(" where name like '%"+teacher.getName()+"%'");
		}
		try {
			PreparedStatement preparedStatement = con.prepareStatement(sqlString.toString());
			ResultSet executeQuery = preparedStatement.executeQuery();
			while(executeQuery.next()){
				Teacher t = new Teacher();
				t.setId(executeQuery.getInt("id"));
				t.setName(executeQuery.getString("name"));
				t.setSex(executeQuery.getString("sex"));
				t.setTitle(executeQuery.getString("title"));
				t.setAge(executeQuery.getInt("age"));
				t.setPassword(executeQuery.getString("password"));
				retList.add(t);
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return retList;
	}
	public boolean delete(int id){
		try {
			// 设置自动提交为false，开始事务
			con.setAutoCommit(false);
			
			// 1. 先获取该教师的所有课程ID
			List<Integer> courseIds = new ArrayList<>();
			String getCourseIdsSql = "SELECT id FROM s_course WHERE teacher_id=?";
			try (PreparedStatement ps0 = con.prepareStatement(getCourseIdsSql)) {
				ps0.setInt(1, id);
				ResultSet rs = ps0.executeQuery();
				while (rs.next()) {
					courseIds.add(rs.getInt("id"));
				}
			}
			
			// 2. 删除与这些课程相关的选课记录
			if (!courseIds.isEmpty()) {
				String deleteSelectedCourseSql = "DELETE FROM s_selected_course WHERE course_id=?";
				try (PreparedStatement ps1 = con.prepareStatement(deleteSelectedCourseSql)) {
					for (int courseId : courseIds) {
						ps1.setInt(1, courseId);
						ps1.addBatch();
					}
					ps1.executeBatch();
				}
			}
			
			// 3. 删除与这些课程相关的成绩记录
			if (!courseIds.isEmpty()) {
				String deleteScoreSql = "DELETE FROM s_score WHERE course_id=?";
				try (PreparedStatement ps2 = con.prepareStatement(deleteScoreSql)) {
					for (int courseId : courseIds) {
						ps2.setInt(1, courseId);
						ps2.addBatch();
					}
					ps2.executeBatch();
				}
			}
			
			// 4. 删除与这些课程相关的考勤记录
			if (!courseIds.isEmpty()) {
				String deleteAttendanceSql = "DELETE FROM s_attendance WHERE course_id=?";
				try (PreparedStatement ps3 = con.prepareStatement(deleteAttendanceSql)) {
					for (int courseId : courseIds) {
						ps3.setInt(1, courseId);
						ps3.addBatch();
					}
					ps3.executeBatch();
				}
			}
			
			// 5. 删除该教师的课程记录
			String deleteCourseSql = "DELETE FROM s_course WHERE teacher_id=?";
			try (PreparedStatement ps4 = con.prepareStatement(deleteCourseSql)) {
				ps4.setInt(1, id);
				ps4.executeUpdate();
			}
			
			// 6. 最后删除教师记录
			String deleteTeacherSql = "DELETE FROM s_teacher WHERE id=?";
			try (PreparedStatement ps5 = con.prepareStatement(deleteTeacherSql)) {
				ps5.setInt(1, id);
				int result = ps5.executeUpdate();
				
				// 提交事务
				con.commit();
				con.setAutoCommit(true);
				
				return result > 0;
			}
		} catch (SQLException e) {
			try {
				// 发生异常时回滚事务
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
	public boolean update(Teacher teacher){
		String sql = "update s_teacher set name=?, sex=?,title=?,age=?,password=? where id=?";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, teacher.getName());
			preparedStatement.setString(2, teacher.getSex());
			preparedStatement.setString(3, teacher.getTitle());
			preparedStatement.setInt(4, teacher.getAge());
			preparedStatement.setString(5, teacher.getPassword());
			preparedStatement.setInt(6, teacher.getId());
			if(preparedStatement.executeUpdate() > 0){
				return true;
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}
	public Teacher login(Teacher teacher){
		String sql = "select * from s_teacher where name=? and password=?";
		Teacher teacherRst = null;
		try {
			PreparedStatement prst = con.prepareStatement(sql);//将sql语句发送到数据库执行
			prst.setString(1, teacher.getName());
			prst.setString(2, teacher.getPassword());
			ResultSet executeQuery = prst.executeQuery();
			if(executeQuery.next()){
				teacherRst = new Teacher();
				teacherRst.setId(executeQuery.getInt("id"));
				teacherRst.setName(executeQuery.getString("name"));
				teacherRst.setPassword(executeQuery.getString("password"));
				teacherRst.setSex(executeQuery.getString("sex"));
				teacherRst.setAge(executeQuery.getInt("Age"));
				teacherRst.setTitle(executeQuery.getString("title"));
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return teacherRst;
	}
	public String editPassword(Teacher teacher,String newPassword){
		String sql = "select * from s_teacher where id=? and password=?";
		PreparedStatement prst = null;
		int id = 0;
		try {
			prst = con.prepareStatement(sql);
			prst.setInt(1, teacher.getId());
			prst.setString(2, teacher.getPassword());
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
		String sqlString = "update s_teacher set password = ? where id = ?";
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
}