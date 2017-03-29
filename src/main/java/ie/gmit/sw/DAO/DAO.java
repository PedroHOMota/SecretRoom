package ie.gmit.sw.DAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.DirectoryManager;
import javax.sql.DataSource;

import org.apache.taglibs.standard.extra.spath.Path;
import org.springframework.stereotype.Repository;


public class DAO
{
	private DataSource mysqlDS;
	
	/*public DAO() throws NamingException
	{
		Context context = new InitialContext();
		String ds = "ds path";
		mysqlDS = (DataSource) context.lookup(ds);
		
	}*/
	
	
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
				data.add(rs.getString("fileLink"));
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
			PreparedStatement stmt = con.prepareStatement("INSERT "+roomID+" "+name+" INTO files");
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
	
	public boolean CreateRoom()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		try
		{
			Connection con=mysqlDS.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Rooms r WHERE r.expiredate < "+dateFormat.format(date));
		}
		catch(Exception e){}
		
		return true;
	}
	
	public int CreateRoomBeta()
	{
		Random a=new Random(Instant.now().toEpochMilli());
		int aa=a.nextInt();
		new File("\\"+aa).mkdir();
		
		return aa;
	}
	public File[] getRoomBeta(String roomID)
	{
			File folder = new File("\\"+roomID);
			if(!folder.exists())
				return null;
			File[] listOfFiles = folder.listFiles();
			
			return listOfFiles;
	}
}