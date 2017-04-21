package ie.gmit.sw.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


import org.springframework.web.multipart.MultipartFile;

import ie.gmit.sw.DAO.DAO;

public class Service {
	private DAO dao= new DAO();
	
	public int CreateRoomBeta()
	{
		//Use the epoch time  as seed to make sure all generated numbers will be equal
		Random rdm=new Random(Instant.now().toEpochMilli());
		int id=rdm.nextInt();
		System.out.println(id);
		if(dao.CreateRoom(id))
		{
			return id;
		}
		return -1;
	}
	
	public byte[] GetFile(String room,String fileName) throws IOException, SQLException
	{
		byte[] file;
		file=dao.GetFile(fileName, room);
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
			return false;
		}
		
		return true;
	}
	public ArrayList<String> GetAllFilesName(String roomID) throws SQLException
	{
		return dao.GetAllFilesName(roomID);
	}
	
	public void SaveMessage(String message,String name, String roomID)
	{
		System.out.println("entrou service");
		dao.SaveMessage(roomID, message, name);
	}
	
	public Map<String, String> GetMessages(String roomID) throws SQLException
	{
		return dao.GetAllMessages(roomID);
	}
}
