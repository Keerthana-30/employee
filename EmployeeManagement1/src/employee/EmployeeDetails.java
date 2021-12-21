package employee;
import java.util.Scanner;
import java.util.Date;
import java.lang.Thread;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EmployeeDetails extends EmployeeAbstract{
	Scanner scanner=new Scanner(System.in);
	public void employeeDetailsMethod(){
		int option,countOfEmployee=0;
		System.out.println("----Welcome to Employee management system----");
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException exception) {
				System.out.println(exception);
			}
			System.out.println("\nPlease choose an option:\n1.ADD\n2.UPDATE\n3.DELETE.\n4.DISPLAY\n5.EXIT");
			System.out.println("Enter the option:");
			option=scanner.nextInt();
			switch(option) {
				case 1:
					System.out.println("Enter the number of employee to be added:");
					countOfEmployee=scanner.nextInt();		
					if(countOfEmployee<10) {
						addEmployee(countOfEmployee);
					}
					else {
						System.out.println("Maximum limit exceeded. Only a maximum of 10 is allowed.");
					}
					break;
				case 2:
					try{  
						Class.forName("com.mysql.jdbc.Driver");  
						Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeedetails?autoReconnect=true&useSSL=false","root","Baskaran30@");  
						Statement statement=connection.createStatement();  
						ResultSet records=statement.executeQuery("SELECT count(*) FROM employeetable");
						records.next();
						int rows=records.getInt(1);
						if(rows==0) {
							System.out.println("Database is empty");
						}
						else {
							System.out.println("Enter the Employee ID of the employee which is to be updated:");
							String employeeId=scanner.next();
							update(employeeId);
						}
						connection.close();
					}
					catch(Exception e){ 
						System.out.println(e);
					}  
					break;
				case 3:
					System.out.println("Enter the Employee Id of the employee to be removed:");
					String idOfEmployee=scanner.next();
					remove(idOfEmployee);
					break;
				case 4:
					display();
					break;
			}
			if (option==5) {
				break;
			}
		}		
	}
	//body of the abstract method 
	public String validations(String content,String regex,String errorMsg) {
		while(true) {
			System.out.println("Enter the employee "+content);
			String value =scanner.next();
			if(regex=="^ACE+[0-9]{4}" || regex=="^(?=.{2,30}$)(([a-zA-Z0-9])\\2?(?!\\2))+@+(?:[a-zA-Z]+\\.)+[com]{2,7}$"){
				try {
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeedetails?autoReconnect=true&useSSL=false","root","Baskaran30@"); 
					PreparedStatement prepStatement=con.prepareStatement("SELECT count(*) FROM employeetable WHERE EmployeeID=(?) OR EmployeeMail=(?)");
					prepStatement.setString(1,value);
				    prepStatement.setString(2,value);
				    ResultSet record=prepStatement.executeQuery();
					record.next();
					int rows=record.getInt(1);
					if(rows!=0) {
						System.out.println("No duplicate values allowed");
						continue;
					}
				}
				catch(Exception exception){
					System.out.println(exception);
				}
			}
		   	if(!value.matches(regex)){
		   		System.out.println(errorMsg);
		   		continue;
			}
			return value;
		}
	}
	@Override
	    //DOB validation
		//Overriding method
		public String validDate(String date) { 
		if(date=="DOJ") {
			String returnValue=super.validDate(date);
			return returnValue;
		}
		else {
			while(true) {
				String dob=validations("DOB: ","^(29-02-(2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26]))))$"
						+"|^((0[1-9]|1[0-9]|2[0-8])-02-((19|2[0-9])[0-9]{2}))$"
						+"|^(0[1-9]|[12][0-9]|3[01])-(0[13578]|10|12)-((19|2[0-9])[0-9]{2})$" 
						+"|^((0[1-9]|[12][0-9]|30)-(0[469]|11)-((19|2[0-9])[0-9]{2}))$","Enter a valid date of birth.");
				long age=0;
				SimpleDateFormat formatter =new SimpleDateFormat("dd-mm-yyyy");//for setting the date format
				try {
					Date dobDate=formatter.parse(dob);//to convert string date into Date
					Date dDate = new Date(); //getting the current date in dd-mm-yyyy hh:mm:ss gmt 
					String sDate=formatter.format(dDate);//for getting only dd-mm-yyyy
					Date curDate=formatter.parse(sDate);// to convert string to Date
					age= ((curDate.getTime()-dobDate.getTime())/86400000)/365;
				} 
				catch (ParseException exception) {
					System.out.println(exception);
				}
				if(age>=18 && age<=60) {
					return dob;
				}
				System.out.println("Age is not between 18 and 60.");
				continue;
			}
		}
	}
}

