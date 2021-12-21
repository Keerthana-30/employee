package employee;

import java.util.Date;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//abstract class
public abstract class EmployeeAbstract extends StoringDetails{
	public abstract String validations(String content,String regex,String errorMsg);//abstract method
	Scanner scanner=new Scanner(System.in);
	//adding employees
	public void addEmployee(int countOfEmployee) {
		while(countOfEmployee>0){                                      
			StoringDetails employee =new StoringDetails();
			System.out.println("\nEmployee details:");
			employee.setEmployeeID(validations("ID: ","^ACE+[0-9]{4}","Not a valid employeeID. No special characters or numerics allowed.EmployeeID should begin with ACE followed by  4 digits."));
			employee.setEmployeeName(validations("Name: ","^(?=.{2,20}$)(([a-zA-Z_\s])\\2?(?!\\2))+$","Employee name must contain only alphabets. Do not include special characters."));
			employee.setEmployeeEmail(validations("Email: ","^(?=.{2,30}$)(([a-zA-Z0-9])\\2?(?!\\2))+@+(?:[a-zA-Z]+\\.)+[com]{2,7}$","Invalid Email.Please enter a valid email id.Please enter a valid email id.Eg:email@domain.com."));		
			employee.setEmployeeDob(validDate("DOB"));
			employee.setEmployeeDoj(validDate("DOJ"));
			try{  
				Class.forName("com.mysql.jdbc.Driver");  
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeedetails?autoReconnect=true&useSSL=false","root","Baskaran30@");  
				PreparedStatement prepStatement= con.prepareStatement("INSERT INTO employeetable (EmployeeID,EmployeeName,EmployeeMail,EmployeeDOB,EmployeeDOJ) VALUES (?, ?, ?, ?, ?)");
				prepStatement.setString(1,employee.employeeID);
				prepStatement.setString(2,employee.employeeName);
				prepStatement.setString(3,employee.employeeEmail);
				prepStatement.setString(4,employee.employeeDob);
				prepStatement.setString(5,employee.employeeDoj);
				prepStatement.executeUpdate();
				con.close();
				}
			catch(Exception exception){ 
				System.out.println(exception);
			}  
			countOfEmployee--;
		}
		System.out.println("Employee details has been added!!");	
	}
	//updating employees
	public void update(String employeeId) {
		String query;
		int choose=0;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeedetails?autoReconnect=true&useSSL=false","root","Baskaran30@");  
			PreparedStatement prepStatement = con.prepareStatement("SELECT COUNT(*) FROM employeetable WHERE EmployeeID=(?)");
			prepStatement.setString(1,employeeId );
			ResultSet record=prepStatement.executeQuery();
			record.next();
			int rows=record.getInt(1);
			//System.out.println(rows);
			if(rows==0) {
				System.out.println("No such ID found.");
			}
			else{
				while(true) {
					System.out.println("which field do you want to update?\n1.ID\n2.name\n3.Email\n4.dob\n5.DOJ\nTo exit press 6");
					choose=scanner.nextInt();
					switch(choose) {
						case 1:
							String ID=validations("ID to be updated: ","^ACE+[0-9]{4}","Not a valid employeeID. No special characters or numerics allowed.EmployeeID should begin with ACE followed by  4 digits.");
							query="UPDATE employeetable SET EmployeeID=(?) WHERE EmployeeID=(?)";
							queryUpdate(query,ID,employeeId,"ID");
							break;
						case 2:
							String name=validations("name to be updated: ","^(?=.{2,20}$)(([a-zA-Z])\\2?(?!\\2))+$","Employee name must contain only alphabets. Do not include special characters and numerics.");
							query="UPDATE employeetable SET EmployeeName=(?) WHERE EmployeeID=(?)";
							queryUpdate(query,name,employeeId,"Name");
							break;
						case 3:
							String email=validations("Email to be updated: ","^(?=.{2,30}$)(([a-zA-Z0-9])\\2?(?!\\2))+@+(?:[a-zA-Z]+\\.)+[com]{2,7}$","Invalid Email.Please enter a valid email id.Eg:email@domain.com.");
							query="UPDATE employeetable SET EmployeeMail=(?) WHERE EmployeeID=(?)";
							queryUpdate(query,email,employeeId,"Email");
							break;
						case 4:
							String dob=validDate("DOB");
							query="UPDATE employeetable SET EmployeeDOB=(?) WHERE EmployeeID=(?)";
							queryUpdate(query,dob,employeeId,"DOB");
							break;
						case 5:
							String doj=validDate("DOJ");
							query="UPDATE employeetable SET EmployeeDOJ=(?) WHERE EmployeeID=(?)";
							queryUpdate(query,doj,employeeId,"DOJ");
							break;
					}	
					if(choose==6) {
						break;
					}
				}
				con.close();
			}
		}
		catch(Exception exception){ 
			System.out.println(exception);
		}  
	}
	//removing employees
	public void remove(String idOfEmployee) {
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeedetails?autoReconnect=true&useSSL=false","root","Baskaran30@");  
			PreparedStatement pStatementDelete=connection.prepareStatement("DELETE FROM employeetable WHERE EmployeeID =(?)");
			pStatementDelete.setString(1,idOfEmployee);
			
			int rows=pStatementDelete.executeUpdate();
			if(rows==0) {
				System.out.println("ID is not found.Please enter a valid employeeID.");
			}
			else {
				System.out.println("Deleted successfully.");
			}
			connection.close();
		}
		catch(Exception exception){ 
			System.out.println(exception);
		} 
	}
	//displaying employees
	public void display() {
		int count=1;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeedetails?autoReconnect=true&useSSL=false","root","Baskaran30@");
			PreparedStatement prepStatement = connection.prepareStatement("SELECT COUNT(*) FROM employeetable");
			ResultSet record=prepStatement.executeQuery();
			record.next();
			int rows=record.getInt(1);
			if(rows==0) {
				System.out.println("Database is empty.");
			}
			else {
				System.out.println("choose the options:\n1.Display the Details of a particular employee\n2.Display all employees details");
				int displayOption = scanner.nextInt();
				switch(displayOption) {
					case 1:
						System.out.println("Enter the ID to be displayed:");
						String displayID=scanner.next();
							//String displayID=validations("ID to be displayed:","^ACE+[0-9]{4}","Not a valid employeeID. No special characters or numerics allowed.EmployeeID should begin with ACE followed by  4 digits.");
							PreparedStatement StatementDisplay = connection.prepareStatement("SELECT * FROM employeetable WHERE EmployeeID=?");
							StatementDisplay.setString(1, displayID);
							ResultSet displayRecord=StatementDisplay.executeQuery();
							while(displayRecord.next()) {
								 System.out.println("---------------------------------------------------------------");
								 System.out.println("Employee Details: "+count);
								 System.out.println("Employee ID   : "+displayRecord.getString(1));
								 System.out.println("Employee Name : "+displayRecord.getString(2));
								 System.out.println("Employee Email: "+displayRecord.getString(3));
								 System.out.println("Employee DOB  : "+displayRecord.getString(4));
								 System.out.println("Employee DOJ  : "+displayRecord.getString(5));
								 System.out.println("---------------------------------------------------------------");
								 count++;
							}
							break;
					case 2:
							PreparedStatement StatementDisplays = connection.prepareStatement("SELECT * FROM employeetable");
							ResultSet records=StatementDisplays.executeQuery();
							while(records.next()) {
								 System.out.println("---------------------------------------------------------------");
								 System.out.println("Employee Details: "+count);
								 System.out.println("Employee ID   : "+records.getString(1));
								 System.out.println("Employee Name : "+records.getString(2));
								 System.out.println("Employee Email: "+records.getString(3));
								 System.out.println("Employee DOB  : "+records.getString(4));
								 System.out.println("Employee DOJ  : "+records.getString(5));
								 System.out.println("---------------------------------------------------------------");
								 count++;
							}
							break;
				}
			}
		}
		catch(Exception exception) {
			System.out.println(exception);
		}
	}
	//Overridden method
		public String validDate(String date) { 
			while(true) {
				String doj=validations("DOJ: ","^(29-02-(2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26]))))$"
						+"|^((0[1-9]|1[0-9]|2[0-8])-02-((19|2[0-9])[0-9]{2}))$"
						+"|^(0[1-9]|[12][0-9]|3[01])-(0[13578]|10|12)-((19|2[0-9])[0-9]{2})$" 
						+"|^((0[1-9]|[12][0-9]|30)-(0[469]|11)-((19|2[0-9])[0-9]{2}))$","Please enter a valid date of joining");
				long days=0;
				SimpleDateFormat formatter =new SimpleDateFormat("dd-mm-yyyy");//For setting the date format
				try {
					Date dojDate=formatter.parse(doj);//to covert string date into Date
					Date dDate = new Date(); //getting the current date in dd-mm-yyyy hh:mm:ss gmt 
					String sDate=formatter.format(dDate);//for getting only dd-mm-yyyy
					Date curDate=formatter.parse(sDate);// to convert string to Date
					days= ((curDate.getTime()-dojDate.getTime())/86400000)%365;
					//System.out.println(days);
					//System.out.println("ji");
				} 
				catch (ParseException exception) {
					System.out.println(exception);
				}
				if(days>0 && days<=7) {
					return doj;
				}
				System.out.println("No future dates are allowed");
				continue;
			}
		}
		public void queryUpdate(String query, String value, String employeeId,String message) {
			try {
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employeedetails?autoReconnect=true&useSSL=false","root","Baskaran30@");
				PreparedStatement pstatement=connection.prepareStatement(query);
				pstatement.setString(1,value);
				pstatement.setString(2, employeeId);
				pstatement.executeUpdate();
				System.out.println(message+" has been updated successfully.");
			} catch (SQLException exception) {
				exception.printStackTrace();
			}  
		}
}
