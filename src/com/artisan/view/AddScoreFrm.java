package com.artisan.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.artisan.dao.CourseDao;
import com.artisan.dao.ScoreDao;
import com.artisan.dao.SelectedCourseDao;
import com.artisan.dao.StudentDao;
import com.artisan.model.Course;
import com.artisan.model.Score;
import com.artisan.model.Student;
import com.artisan.model.Teacher;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class AddScoreFrm extends JInternalFrame {
	private JTextField scoreTextField;
	private JComboBox studentComboBox;
	private JComboBox courseComboBox;
	private List<Course> courseList;
	private List<Student> studentList;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddScoreFrm frame = new AddScoreFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddScoreFrm() {
		setClosable(true);
		setIconifiable(true);
		setTitle("成绩录入界面");
		setBounds(100, 100, 641, 474);
		
		JLabel label = new JLabel("学生姓名：");
		label.setIcon(new ImageIcon(AddScoreFrm.class.getResource("/images/学生管理.png")));
		label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		studentComboBox = new JComboBox();
		
		JLabel label_1 = new JLabel("课程信息：");
		label_1.setIcon(new ImageIcon(AddScoreFrm.class.getResource("/images/课程.png")));
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		courseComboBox = new JComboBox();
		courseComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ae) {
				courseChangeAct(ae);
			}
		});
		courseComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
			}
		});
		
		JLabel label_2 = new JLabel("所得成绩：");
		label_2.setIcon(new ImageIcon(AddScoreFrm.class.getResource("/images/成绩.png")));
		label_2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		scoreTextField = new JTextField();
		scoreTextField.setColumns(10);
		
		JButton submitButton = new JButton("录入成绩");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				submitAct(ae);
			}
		});
		submitButton.setIcon(new ImageIcon(AddScoreFrm.class.getResource("/images/确认.png")));
		submitButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(142)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(label_2)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scoreTextField))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(label_1)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(courseComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(label)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(studentComboBox, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(213, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(311, Short.MAX_VALUE)
					.addComponent(submitButton)
					.addGap(249))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(63)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(studentComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(62)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(courseComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(66)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_2)
						.addComponent(scoreTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(74)
					.addComponent(submitButton)
					.addContainerGap(89, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
		setCourseCombox();
		setStudentCombox();
	}
	protected void submitAct(ActionEvent ae) {
		// TODO Auto-generated method stub
		int score = 0;
		try {
			score = Integer.parseInt(scoreTextField.getText().toString());
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "成绩只能输入大于0的数字");
			return;
		}
		if(score <= 0){
			JOptionPane.showMessageDialog(this, "成绩只能输入大于0的数字");
			return;
		}
		Student student = (Student) studentComboBox.getSelectedItem();
		Course course = (Course)courseComboBox.getSelectedItem();
		Score scoreObj = new Score();
		scoreObj.setStudent_id(student.getId());
		scoreObj.setCourse_id(course.getId());
		scoreObj.setScore(score);
		ScoreDao scoreDao = new ScoreDao();
		if(scoreDao.isAdd(scoreObj)){
			JOptionPane.showMessageDialog(this, "成绩已录入，不可重复录入！");
			return;
		}
		if(scoreDao.addScore(scoreObj)){
			JOptionPane.showMessageDialog(this, "成绩录入成功！");
			scoreTextField.setText("");
		}else{
			JOptionPane.showMessageDialog(this, "成绩录入失败！");
		}
		scoreDao.closeDao();
	}

	protected void courseChangeAct(ItemEvent ae) {
		// TODO Auto-generated method stub
		if(ae.getStateChange() == ItemEvent.SELECTED){
			setStudentCombox();
		}
		//JOptionPane.showMessageDialog(this, "changed");
		//setStudentCombox();
	}

	private void setCourseCombox(){
		CourseDao courseDao = new CourseDao();
		courseList = courseDao.getCourseList(new Course());
		courseDao.closeDao();
		for (Course course : courseList) {
			if("教师".equals(MainFrm.userType.getName())){
				Teacher teacher = (Teacher)MainFrm.userObject;
				if(course.getTeacher_id() == teacher.getId()){
					courseComboBox.addItem(course);
				}
				continue;
			}
			//执行的第一个是系统管理员的情况
			courseComboBox.addItem(course);
		}
		
	}
	private void setStudentCombox(){
		studentComboBox.removeAllItems();
		StudentDao studentDao = new StudentDao();
		studentList = studentDao.getStudentList(new Student());
		studentDao.closeDao();
		Course course = (Course)courseComboBox.getSelectedItem();
		List<Student> selectedCourseStudentList = getSelectedCourseStudentList(course);
		for (Student student : studentList) {
			for(Student student2 : selectedCourseStudentList){
				if(student.getId() == student2.getId())
					studentComboBox.addItem(student);
			}
		}
		
	}
	private List<Student> getSelectedCourseStudentList(Course course){
		SelectedCourseDao scDao = new SelectedCourseDao();
		List<Student> selectedCourseStudentList = scDao.getSelectedCourseStudentList(course);
		return selectedCourseStudentList;
	}
}