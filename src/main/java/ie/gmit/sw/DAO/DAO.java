package ie.gmit.sw.DAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class DAO
{
	private MysqlDataSource mysqlDS;
	
	public DAO()
	{
		try {
			mysqlDS = new MysqlDataSource();
			

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public boolean SaveFile(InputStream file, String name, String roomID) throws IOException
	{
		System.out.println("entrou db");
		try
		{
			Connection con = mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO files Values(?,?,?)");
			stmt.setBinaryStream(1, file);;
			stmt.setString(2, name);
			stmt.setInt(3, Integer.parseInt(roomID));
			stmt.executeUpdate();
				
		}catch(Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
			return false;
		}
		

		return true; 
	}
	
	public byte[] GetFile(String fileName,String roomID) throws IOException, SQLException
	{
		ResultSet rs = null;
		
		try
		{
			Connection con = mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("select file from files f where f.name like \""+fileName+"\" and f.rid like "+roomID);
			System.out.println(stmt.toString());		
			rs = stmt.executeQuery();
			rs.next();

			byte[] file=rs.getBytes("file");
			return file;
		}catch(Exception e)
		{
			System.out.println("Error DAO: "+e.getMessage());
			return null;
		}
				
		
				 
	}
	
	public boolean CreateRoom(int roomID)
	{
		try
		{
			Connection con=mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("Insert INTO Room(RID,ExpDate) Values("+roomID+",now()+Interval 2 Day)");
			stmt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public ArrayList<String> GetAllFilesName(String roomID) throws SQLException
	{
		ArrayList<String> files=new ArrayList<String>();
		ResultSet rs=null;
		try
		{
			Connection con=mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT Name FROM Files f WHERE f.RID LIKE "+roomID);
			rs=stmt.executeQuery();
		}
		catch(Exception e)
		{
			return null;
		}
		while(rs.next())
		{
			files.add(rs.getString(1));
		}
		
		return files;
	}

	public boolean SaveMessage(String roomID,String message,String name)
	{
		System.out.println("estrouuuu no savemsg");
		try
		{
			Connection con=mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO messages VALUES(\""+message+"\","+roomID+",\""+name+"\")");
			stmt.executeUpdate();
			return true;
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	public Map<String, String> GetAllMessages(String roomID) throws SQLException
	{
		Map<String, String> msgs = new HashMap<String, String>();
		ResultSet rs = null;
		try
		{
			Connection con=mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("Select message, name FROM messages m Where m.RID Like "+roomID);
			rs=stmt.executeQuery();
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		while(rs.next())
		{
			msgs.put(rs.getString("name"), rs.getString("message"));
		}
		return msgs;
	}
}