
import java.sql.*;

public class BasicJDBCDemo
{
    Connection conn;
    String URL = "jdbc:oracle:thin:@cdmoracledb.cti.depaul.edu:1521:def";
    //String URL = "jdbc:oracle:thin:@140.192.30.237:1521:def";

    public static void main(String[] args) throws java.sql.SQLException
    {
	BasicJDBCDemo demo = new BasicJDBCDemo();

	demo.runTest(Integer.parseInt(args[0]));
    }
 
    public BasicJDBCDemo()
    {
    try
	{
	    Class.forName("oracle.jdbc.driver.OracleDriver");
	    String url = "jdbc:oracle:thin:@140.192.30.237:1521:def";
	    this.URL = url;
	    conn = DriverManager.getConnection(url, "tmalik1", "cdm1854842");
	    
	    //doTests();
	    
	    //conn.close();
	}
    catch (ClassNotFoundException ex) {System.err.println("Class not found " + ex.getMessage());}
    catch (SQLException ex)           {System.err.println(ex.getMessage());}
    }

    public void runTest( int which ) throws java.sql.SQLException
    {
	conn = DriverManager.getConnection(URL, "tmalik1", "cdm1854842");

	switch (which) {
	case 1:  
	    doSelectTest();
	    break;
	case 2:   
	    doInsertTest("Student", null);
	    doSelectTest();
	    break;
	case 3:
	    String[] values = {"'Wagner'","'James'","34527"};
	    doInsertTest("Student", values);
	    doSelectTest();
	    break;
	case 4:
	    doPreparedSelectTest();
	    break;
	case 5:
	    doPreparedUpdateTest("Jack");
	    doSelectTest();
	    break;
	case 6:
	    doDeleteTest("Student");
	    doSelectTest();
	    break;
	}

	conn.close();
    }

    private void doTests()
    {
	doSelectTest();

	doInsertTest("Student", null);
	doSelectTest();

	String[] values = {"101", "NULL"};
	doInsertTest("Student", values);
	doSelectTest();

	doPreparedSelectTest();

	doPreparedUpdateTest("StudentName");
	doSelectTest();
    }

    private void doSelectTest()
    {
	System.out.println("\ndoSelectTest:\n");
	String query = "SELECT SID,LastName FROM Student";
    try
	{
	    Statement st = conn.createStatement();
	    ResultSet rs = st.executeQuery(query);
	    while (rs.next())
		{
            
		    String s = rs.getString("SID");
		    String n = rs.getString("LastName");
		    System.out.println(s + "   " + n);
		}
	}
    catch (SQLException ex)
	{
	    System.err.println("Select failure " + ex.getMessage());
	}
    }

    private void doPreparedSelectTest()
    {
	System.out.println("doPreparedSelectTest:\n");
	String selectString =
	    "SELECT SID, LastName,Started FROM Student WHERE Started BETWEEN ? AND ?";

    try
	{
	    PreparedStatement selQuery = conn.prepareStatement(selectString);
	    
	    // Fill in question mark #1 with value 4
	    selQuery.setInt(1, 2008);

	    // Fill in question mark #2 with value 4
	    selQuery.setInt(2, 2012);
	    
	    ResultSet rs = selQuery.executeQuery();


	    while (rs.next())
		{
		    String s = rs.getString("SID");
		    String n = rs.getString("LastName");
		    String t = rs.getString("Started");
		    System.out.println(s + "   " + n + "    " + t);
		}
	}
    catch (SQLException ex)
	{
	    System.err.println("Select failure " + ex.getMessage());
	}
    }

    private void doInsertTest(String table, String[] values)
    {
	System.out.print("\ndoInsertTest using " + table + ":");

    try
	{
	    Statement st = conn.createStatement();
	    if (values == null)
        { 
            String str = "INSERT INTO Student (Lastname,Firstname,SID) VALUES ('Wagner','James',34527)";
            System.out.println(str);
            st.executeUpdate(str);
        }
	    else
		{
		    String upd = "INSERT INTO " + table + " (Lastname,Firstname,SID) VALUES (";
		    for (int i = 0; i < values.length; i++)
			{
			    upd = upd + values[i];
			    if (i < values.length-1)
				upd += ", ";
			    else
				upd += ")";
			}
            System.out.println(upd);
		    st.executeUpdate(upd);
		}
	}
    catch (SQLException ex)
	{
	    System.err.println("Insert failure " + ex.getMessage());
	}
    }

    private void doPreparedUpdateTest(String name)
    {
	System.out.print("\ndoPreparedUpdateTest:\n");

	String selectString =
	    "UPDATE Student SET Started = 2017 WHERE LastName = ?";    

	try
	{
	    Statement st = conn.createStatement();

	    PreparedStatement updQuery = conn.prepareStatement(selectString);

	    updQuery.setString(1, "Wagner");

	    int updated = updQuery.executeUpdate();

	    System.out.println("Updated " + updated + " rows.");
	}
    catch (SQLException ex)
	{
	    System.err.println(ex.getMessage());
	}
    }

    private void doDeleteTest(String table)
    {
	System.out.print("\ndoInsertTest using " + table + ":");

    try
	{
	    Statement st = conn.createStatement();

	    st.executeUpdate("DELETE FROM " + table +  
				 " WHERE SSN IS NULL");
	}
    catch (SQLException ex)
	{
	    System.err.println("Delete failure " + ex.getMessage());
	}
    }


}
