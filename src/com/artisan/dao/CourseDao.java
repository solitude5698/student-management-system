package com.artisan.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.artisan.model.Course;
import com.artisan.util.StringUtil;

/**
 * 
 * 课程信息操作数据库
 */
public class CourseDao extends BaseDao {
	public boolean addCourse(Course course){
		String sql = "insert into s_course values(null,?,?,?,?,0)";
		try {
			java.sql.PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, course.getName());
			preparedStatement.setInt(2, course.getTeacher_id());
			preparedStatement.setInt(3, course.getMax_student_num());
			preparedStatement.setString(4, course.getInfo());
			if(preparedStatement.executeUpdate() > 0)return true;
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}
	public List<Course> getCourseList(Course course){
		List<Course> retList = new ArrayList<Course>();
		StringBuffer sqlString = new StringBuffer("select * from s_course");
		if(!StringUtil.isEmpty(course.getName())){
			sqlString.append(" and name like '%"+course.getName()+"%'");
		}
		if(course.getTeacher_id() != 0){
			sqlString.append(" and teacher_id ="+course.getTeacher_id());
		}
		try {
			PreparedStatement preparedStatement = con.prepareStatement(sqlString.toString().replaceFirst("and", "where"));
			ResultSet executeQuery = preparedStatement.executeQuery();
			while(executeQuery.next()){
				Course c = new Course();
				c.setId(executeQuery.getInt("id"));
				c.setName(executeQuery.getString("name"));
				c.setTeacher_id(executeQuery.getInt("teacher_id"));
				c.setMax_student_num(executeQuery.getInt("max_student_num"));
				c.setInfo(executeQuery.getString("info"));
				c.setSelected_num(executeQuery.getInt("selected_num"));
				retList.add(c);
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return retList;
	}
	public boolean delete(int id) {
		try {
			// 设置自动提交为false，开始事务
			con.setAutoCommit(false);
			
			// 1. 删除与课程相关的选课记录
			String deleteSelectedCourseSql = "DELETE FROM s_selected_course WHERE course_id=?";
			try (PreparedStatement ps1 = con.prepareStatement(deleteSelectedCourseSql)) {
				ps1.setInt(1, id);
				ps1.executeUpdate();
			}
			
			// 2. 删除与课程相关的成绩记录
			String deleteScoreSql = "DELETE FROM s_score WHERE course_id=?";
			try (PreparedStatement ps2 = con.prepareStatement(deleteScoreSql)) {
				ps2.setInt(1, id);
				ps2.executeUpdate();
			}
			
			// 3. 删除与课程相关的考勤记录
			String deleteAttendanceSql = "DELETE FROM s_attendance WHERE course_id=?";
			try (PreparedStatement ps3 = con.prepareStatement(deleteAttendanceSql)) {
				ps3.setInt(1, id);
				ps3.executeUpdate();
			}
			
			// 4. 最后删除课程记录
			String deleteCourseSql = "DELETE FROM s_course WHERE id=?";
			try (PreparedStatement ps4 = con.prepareStatement(deleteCourseSql)) {
				ps4.setInt(1, id);
				int result = ps4.executeUpdate();
				
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
	public boolean update(Course course){
		String sql = "update s_course set name=?, teacher_id=?,max_student_num=?,info=? where id=?";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, course.getName());
			preparedStatement.setInt(2, course.getTeacher_id());
			preparedStatement.setInt(3, course.getMax_student_num());
			preparedStatement.setString(4, course.getInfo());
			preparedStatement.setInt(5, course.getId());
			if(preparedStatement.executeUpdate() > 0){
				return true;
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}
	public boolean selectedEnable(int course_id){
		String sql = "select * from s_course where id=?";
		try {
			PreparedStatement prst = con.prepareStatement(sql);//将sql语句发送到数据库执行
			prst.setInt(1, course_id);
			ResultSet executeQuery = prst.executeQuery();
			if(executeQuery.next()){
				int max_student_num = executeQuery.getInt("max_student_num");
				int selected_num = executeQuery.getInt("selected_num");
				if(selected_num >= max_student_num)return false;
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
		}
		return true;
	}
	public boolean updateSelectedNum(int course_id,int num){
		String sql = "update s_course set selected_num = selected_num + ? where id = ?";
		if(num < 0){
			sql = "update s_course set selected_num = selected_num - ? where id = ?";
		}
		try {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, 1);
			preparedStatement.setInt(2, course_id);
			if(preparedStatement.executeUpdate() > 0){
				return true;
			}
		} catch (SQLException e) {
			// 自动生成的 catch 块
			e.printStackTrace();
			
		}
		return false;
	}
}