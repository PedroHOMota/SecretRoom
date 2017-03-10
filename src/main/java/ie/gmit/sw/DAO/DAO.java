package ie.gmit.sw.DAO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.DirectoryManager;
import javax.sql.DataSource;

public class DAO
{
	private DataSource mysqlDS;
	
	public DAO() throws NamingException
	{
		Context context = new InitialContext();
		String jndiName = "java:comp/env/jdbc/employeesdb14";
		mysqlDS = (DataSource) context.lookup(jndiName);
		
	}
	
	//Method used to consult if the rooms exists
	//if it does return data
	public ArrayList<String> getData(String roomID)
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
				data.add(rs.getString("fileLink"));
			}
		
		}catch(Exception e){}
		return data;
	}
	
	//Methhod to delete expired rooms
	public void Delete()
	{
		//make connection to DB 
		
		//Check if which rooms are expired
		//Delete folder than delete the entry on the db
	}
	
	public boolean SaveFile(byte[] file, String name, String roomID) throws IOException
	{
		try
		{
			Connection con = mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT "+name+" "+roomID+" INTO files");
			ResultSet rs = stmt.executeQuery();
			
				
		}catch(Exception e){}
		
		//Saving the file to the room's folder on server
		FileOutputStream Fstream = new FileOutputStream("//"+roomID+"//"+name);
		Fstream.write(file);
		Fstream.close();
		return true; 
	}
	
	public byte[] getFile(String fileName) throws IOException
	{
		FileInputStream fStream = new FileInputStream(fileName);
		
		byte[] file = new byte[fStream.available()];
		fStream.read(file);
		fStream.close();
		return file;
	}
}