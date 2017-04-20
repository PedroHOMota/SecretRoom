package ie.gmit.sw.Service;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Random;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.web.multipart.MultipartFile;

import com.mysql.jdbc.Blob;

import ie.gmit.sw.DAO.DAO;

public class Service {
	private DAO dao= new DAO();
	
	public int CreateRoomBeta()
	{
		Random rdm=new Random(Instant.now().toEpochMilli());
		int id=rdm.nextInt();
		System.out.println(id);
		if(dao.CreateRoom(id))
		{
			System.out.println("sem erro");
			return id;
		}
		System.out.println("Error");
		return -1;
	}
	
	public byte[] getFile(String room,String fileName) throws IOException, SQLException
	{
		byte[] file;
		file=dao.getFile(fileName, room);
		if(file==null)
			return null;
		
		
		
		
		return file;
	}

	public boolean SaveFile(MultipartFile file, String name, String roomID)
	{
		
		try {
			dao.SaveFile(file.getInputStream(), name, roomID);
		} catch (Exception e) 
		{
			System.out.println("Retournou false; service");
			return false;
		}
		
		System.out.println("Retournou true service");
		return true;
	}	
}
