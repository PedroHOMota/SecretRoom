package ie.gmit.sw.DAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.web.multipart.MultipartFile;

import com.mysql.jdbc.Blob;
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
	
	public byte[] getFile(String fileName,String roomID) throws IOException, SQLException
	{
		ResultSet rs = null;
		
		try
		{
			Connection con = mysqlDS.getConnection();
			//PreparedStatement stmt = con.prepareStatement("SELECT file FROM Files f WHERE f.RID LIKE ? AND f.Name Like ?");
			PreparedStatement stmt = con.prepareStatement("select file from files f where f.name like \""+fileName+"\" and f.rid like "+roomID);
			System.out.println(stmt.toString());		
			//stmt.setInt(2, Integer.parseInt(roomID));
			//stmt.setString(, fileName);
			rs = stmt.executeQuery();
			rs.next();

			byte[] file=rs.getBytes("file");
			System.out.println("Tamanho "+file.length);
			return file;
		}catch(Exception e)
		{
			System.out.println("Error DAO: "+e.getMessage());
			return null;
		}
				
		
				 
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
			System.out.println("Retornou falso");
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
			files.add(rs.getString(1));
		}
		
		return files;
	}

}