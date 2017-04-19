package ie.gmit.sw.Service;

import java.io.EOFException;
import java.io.File;
import java.time.Instant;
import java.util.Random;

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
	
	public byte[] getFile(String room,String fileName)
	{
		byte[] file;
		try
		{
			file=dao.getFile(fileName, room);
		}
		catch(Exception e)
		{
			return null;
		}
		return file;
	}

}
