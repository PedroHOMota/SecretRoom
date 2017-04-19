package ie.gmit.sw.DAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class DAO
{
	private MysqlDataSource mysqlDS;
	
	public DAO()
	{
		//Context context;
		try {
			//context = new InitialContext();
			//String ds = "jdbc/secretroom";
			mysqlDS = new MysqlDataSource();
			mysqlDS.setURL("");
			mysqlDS.setUser("");
			mysqlDS.setPassword("");
			System.out.println("configurou");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	//Method used to consult if the rooms exists
	//if it does return data
	public ArrayList<String> getFiles(String roomID)
	{
		ArrayList<String> data = new ArrayList<String>();
		try
		{
			Connection con = mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM rooms r Where r.id LIKE "+roomID);
			ResultSet rs = stmt.executeQuery();
				
			while(rs.next())
			{
				data.add(rs.getString("fileName"));
			}
		
		}catch(Exception e){}
		return data;
	}
	
	//Methhod to delete expired rooms
	public void Delete()
	{	
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		try
		{
			Connection con=mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Rooms r WHERE r.expiredate < "+dateFormat.format(date));
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				String id=rs.getString("roomID");
				//Files.delete("//Rooms//"+id);
				stmt = con.prepareStatement("DELETE * FROM Rooms r, Files f WHERE f.RID = "+id+" AND r.ID = "+id);
			}
			
		}
		catch(Exception e){}
		//Check if which rooms are expired
		//Delete folder than delete the entry on the db
	}
	
	public boolean SaveFile(byte[] file, String name, String roomID) throws IOException
	{
		try
		{
			Connection con = mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT "+file+" "+name+" "+Integer.parseInt(roomID)+" "+" INTO files");
			ResultSet rs = stmt.executeQuery();
			
				
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		

		return true; 
	}
	
	public byte[] getFile(String fileName,String roomID) throws IOException, SQLException
	{
		ResultSet rs = null;
		try
		{
			Connection con = mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("Select "+fileName+" From Files f Where f.RID Like "+roomID);
			rs = stmt.executeQuery();
			
		}catch(Exception e)
		{
			return null;
		}
		
		return rs.getBytes(0);
				 
	}
	//OLD
	public byte[] getFile(String fileName) throws IOException
	{
		FileInputStream fStream = new FileInputStream(fileName);
		
		byte[] file = new byte[fStream.available()];
		fStream.read(file);
		fStream.close();
		return file;
	}
	
	public boolean CreateRoom(int roomID)
	{
		//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Date date = new Date();
		//System.out.println(dateFormat.format(date));
		try
		{
			Connection con=mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("Insert INTO Room(RID,ExpDate) Values("+roomID+",now())");
			stmt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public ArrayList<String> getAllFiles(String roomID) throws SQLException
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
		String line="";
		while(rs.next())
		{
			files.add(rs.getString(0));
		}
		
		return files;
	}

}