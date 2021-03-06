import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
	import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;
	
public class CourseService {

		
		public CourseService() {
		}
		
//		@Comment varchar(MAX), 
//		@Rate int, 
//		@RaterName varchar(15),
//		@CourseName varchar(30), 
//		@Professor varchar(15)
		
		public boolean addTakeCourse(String username, int course) {
			CallableStatement cs = null;
			
			try {
				cs = Main.connS.getConnection().prepareCall("{? = call take_Course(?, ?)}");
				
				if(username == null || username.isEmpty()) {
					//print somethin to JFrame
					JOptionPane.showMessageDialog(null,"Empty username not allow");
					return false;
				} 
				if(course < 0) {
					JOptionPane.showMessageDialog(null,"Illegal course id not allow");
					return false;
				} 
				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, username);
				cs.setInt(3, course);
				cs.execute();
				int result = cs.getInt(1);
				System.out.println(result);
				
				if(result == 0) {
					JOptionPane.showMessageDialog(null, "Add succeeded");
					return true;
				} else if(result == 10){
					JOptionPane.showMessageDialog(null, "Empty input");
				}else if(result == 20){
					JOptionPane.showMessageDialog(null, "invalid Student Username");
				}else if(result == 30){
					JOptionPane.showMessageDialog(null, "invalid CourseID");
				}else if(result == 40){
					JOptionPane.showMessageDialog(null, "You have already selected this course!");
				}
				return false;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		public boolean dropTakeCourse(String username, int course) {
			CallableStatement cs = null;
			
			try {
				cs = Main.connS.getConnection().prepareCall("{? = call DropCourseTaken(?, ?)}");
				
				if(username == null || username.isEmpty()) {
					//print somethin to JFrame
					JOptionPane.showMessageDialog(null,"Empty username not allow");
					return false;
				} 
				if(course < 0) {
					JOptionPane.showMessageDialog(null,"Illegal course id not allow");
					return false;
				} 
				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, username);
				cs.setInt(3, course);
				cs.execute();
				int result = cs.getInt(1);
				System.out.println(result);
				
				if(result == 0) {
					JOptionPane.showMessageDialog(null, "You dropped the course");
					return true;
				} else if(result == 10){
					JOptionPane.showMessageDialog(null, "You are not a student");
				}else if(result == 20){
					JOptionPane.showMessageDialog(null, "Not a valid course");
				}else if(result == 30){
					JOptionPane.showMessageDialog(null, "You haven't taken this course");
				}
				return false;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		public int getCourseIDByNumber(String CNumber) {
			int result = 0;
			CallableStatement cs = null;
			try {
				cs = Main.connS.getConnection().prepareCall("{call getCourseByNumber(?)}");
				cs.setString(1, CNumber);
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
		            result = rs.getInt("ID");
		        }
				return result;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}
	
		public ArrayList<String> getCoursesByDepartment(String deptName) {
			ArrayList<String> result = new ArrayList<>();
			result.add("----");
			CallableStatement cs = null;
			
			try {
				cs = Main.connS.getConnection().prepareCall("{call getCourseByDepartment(?)}");
				cs.setString(1, deptName);
				ResultSet rs = cs.executeQuery();
				while (rs.next()) {
		            result.add(rs.getString("Number"));
		        }
				return result;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}
		
		public ArrayList<String> getCoursesTakenByStudent(String username) {
			ArrayList<String> result = new ArrayList<>();
			CallableStatement cs = null;
			try {
				cs = Main.connS.getConnection().prepareCall("{call getCourseByStudent(?)}");
				cs.setString(1, username);
				ResultSet rs = cs.executeQuery();
				while (rs.next()) {
		            result.add(rs.getString("Number"));
		        }
				return result;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}
		
		public ArrayList<ArrayList<String>> getCoursesInfoByStudent(String username){
			ArrayList<ArrayList<String>> result = new ArrayList<>();
			ArrayList<String> titleLine = new ArrayList<String>();
			titleLine.add("Course ID");
			titleLine.add("Course Name");
			titleLine.add("Course Number");
			titleLine.add("Department");
			titleLine.add("Average Rate");
			result.add(titleLine);
			CallableStatement cs = null;
			try {
				cs = Main.connS.getConnection().prepareCall("{call getCourseInfoByStudent(?)}");
				cs.setString(1, username);
				ResultSet rs = cs.executeQuery();
				int courseID = rs.findColumn("ID");
				int courseName = rs.findColumn("Name");
				int courseNum = rs.findColumn("Number");
				int DeptName = rs.findColumn("Department");
				int rate = rs.findColumn("rate");
				while (rs.next()) {
					ArrayList<String> re = new ArrayList<String>();
					re.add(rs.getString(courseID));
					re.add(rs.getString(courseName));
					re.add(rs.getString(courseNum));
					re.add(rs.getString(DeptName));
					re.add(rs.getString(rate));
					result.add(re);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}

		public ArrayList<ArrayList<String>> getCoursesInfoByDeptName(String dept) {
			// TODO Auto-generated method stub
			ArrayList<ArrayList<String>> result = new ArrayList<>();
			ArrayList<String> titleLine = new ArrayList<String>();
			titleLine.add("Course ID");
			titleLine.add("Course Name");
			titleLine.add("Course Number");
			titleLine.add("Average Rate");
			result.add(titleLine);
			CallableStatement cs = null;
			try {
				cs = Main.connS.getConnection().prepareCall("{call getCourseInfoByDept(?)}");
				cs.setString(1, dept);
				ResultSet rs = cs.executeQuery();
				int courseID = rs.findColumn("ID");
				int courseName = rs.findColumn("Name");
				int courseNum = rs.findColumn("Number");
				int rate = rs.findColumn("rate");
				while (rs.next()) {
					ArrayList<String> re = new ArrayList<String>();
					re.add(rs.getString(courseID));
					re.add(rs.getString(courseName));
					re.add(rs.getString(courseNum));
					re.add(rs.getString(rate));
					result.add(re);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}

}
