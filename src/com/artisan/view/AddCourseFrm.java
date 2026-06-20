package com.artisan.view;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JButton;

import com.artisan.dao.CourseDao;
import com.artisan.dao.TeacherDao;
import com.artisan.model.Course;
import com.artisan.model.Teacher;
import com.artisan.util.StringUtil;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

public class AddCourseFrm extends JInternalFrame {
	private JTextField courseNameTextField;
	private JTextField studentNumTextField;
	private JComboBox teacherListComboBox;
	private JTextArea courseInfoTextArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddCourseFrm frame = new AddCourseFrm();
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
	public AddCourseFrm() {
		setClosable(true);
		setIconifiable(true);
		setTitle("添加课程");
		setBounds(100, 100, 453, 471);
		
		JLabel label = new JLabel("课程名称：");
		label.setIcon(new ImageIcon(AddCourseFrm.class.getResource("/images/课程.png")));
		label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		courseNameTextField = new JTextField();
		courseNameTextField.setColumns(10);
		
		JLabel label_1 = new JLabel("授课老师：");
		label_1.setIcon(new ImageIcon(AddCourseFrm.class.getResource("/images/老师.png")));
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		teacherListComboBox = new JComboBox();
		
		JLabel label_2 = new JLabel("学生人数：");
		label_2.setIcon(new ImageIcon(AddCourseFrm.class.getResource("/images/人数.png")));
		label_2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		studentNumTextField = new JTextField();
		studentNumTextField.setColumns(10);
		
		JLabel label_3 = new JLabel("课程介绍：");
		label_3.setIcon(new ImageIcon(AddCourseFrm.class.getResource("/images/介绍.png")));
		label_3.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		courseInfoTextArea = new JTextArea();
		
		JButton addCourseButton = new JButton("确认添加");
		addCourseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				addCourseAct(ae);
			}
		});
		addCourseButton.setIcon(new ImageIcon(AddCourseFrm.class.getResource("/images/确认.png")));
		addCourseButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		JButton resetButton = new JButton("重置信息");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				resetValue(ae);
			}
		});
		resetButton.setIcon(new ImageIcon(AddCourseFrm.class.getResource("/images/重置.png")));
		resetButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(88)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(label_2)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(studentNumTextField, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(label_1)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(teacherListComboBox, 0, 149, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(label)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(courseNameTextField, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(label_3)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(courseInfoTextArea, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGap(103)
							.addComponent(addCourseButton)
							.addGap(18)
							.addComponent(resetButton)))
					.addGap(117))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(courseNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(teacherListComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(37)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_2)
						.addComponent(studentNumTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(38)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_3)
						.addComponent(courseInfoTextArea, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(addCourseButton)
						.addComponent(resetButton))
					.addGap(57))
		);
		getContentPane().setLayout(groupLayout);
		setTeacherCombox();
	}

	protected void resetValue(ActionEvent ae) {
		// 重置表单信息
		courseNameTextField.setText("");
		courseInfoTextArea.setText("");
		studentNumTextField.setText("");
		teacherListComboBox.setSelectedIndex(0);
	}

	protected void addCourseAct(ActionEvent ae) {
		// 添加课程业务逻辑
		String couserName = courseNameTextField.getText().toString();
		String courseInfo = courseInfoTextArea.getText().toString();
		Teacher selectedTeacher = (Teacher)teacherListComboBox.getSelectedItem();
		int studentMaxNum = 0;
		try {
			studentMaxNum = Integer.parseInt(studentNumTextField.getText());
		} catch (Exception e) {
			// 处理数字转换异常
			JOptionPane.showMessageDialog(this, "学生人数只能输入数字!");
			return;
		}
		if(StringUtil.isEmpty(couserName)){
			JOptionPane.showMessageDialog(this, "课程名称不能为空!");
			return;
		}
		if(studentMaxNum <= 0){
			JOptionPane.showMessageDialog(this, "学生人数只能输入大于0的数字!");
			return;
		}
		Course course = new Course();
		course.setName(couserName);
		course.setMax_student_num(studentMaxNum);
		course.setInfo(courseInfo);
		course.setTeacher_id(selectedTeacher.getId());
		CourseDao courseDao = new CourseDao();
		if(courseDao.addCourse(course)){
			JOptionPane.showMessageDialog(this, "添加成功!");
		}else{
			JOptionPane.showMessageDialog(this, "添加失败!");
		}
		courseDao.closeDao();
		resetValue(ae);
	}
	private void setTeacherCombox(){
		TeacherDao teacherDao = new TeacherDao();
		List<Teacher> teacherList = teacherDao.getTeacherList(new Teacher());
		teacherDao.closeDao();
		for (Teacher teacher : teacherList) {
			teacherListComboBox.addItem(teacher);
		}
	}
}