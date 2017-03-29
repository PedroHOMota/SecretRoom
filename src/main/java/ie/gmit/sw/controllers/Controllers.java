package ie.gmit.sw.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ie.gmit.sw.DAO.DAO;

@Controller
public class Controllers 
{
 
  private DAO dao= new DAO();
  
  @RequestMapping(value = "/*")//return index page
  public String helloWorld(Model model) 
  {
    model.addAttribute("test", "Hello hua World!");
    return "helloWorld";
  }
  
  /*@RequestMapping(value = "/r/*")
  public String checkRoom(Model model,HttpServletRequest request)
  {
	 String roomID="";
	 //ArrayList<String> data = new ArrayList<String>();
	 
	 roomID=request.getRequestURI().toString();
	 roomID=roomID.substring(roomID.lastIndexOf("/")+1); //getting the id of the room from url
	 model.addAttribute("greeting",roomID);
	 
	 //data=dao.getFiles(roomID); //getting rs
	 File[] listOfFiles=dao.getRoomBeta(roomID);
	 String tst="";
	 for (int i = 0; i < listOfFiles.length; i++) 
	 {
		tst+=listOfFiles[i].getPath()+"\n";
	 }
	 model.addAttribute("greeting",tst);
	 /*if(data.size()==0) //if empty room doesn't exist
	 {
		 model.addAttribute("greeting",tst);
		 return "notFound"; //return not found page
	 }
	 
	 return "helloWorld";
  }*/

  @RequestMapping(value = "/r/*/*")
  public byte[] getFile(HttpServletRequest request) throws IOException
  {
	  String fileName=request.getRequestURI().toString();
	  fileName=fileName.substring(fileName.lastIndexOf("/")+1);
	  
	  dao.getFile(fileName);
	  return dao.getFile(fileName);
  }

  @RequestMapping(value="/createRoom")
  public String createRoom(Model model)
  {
	  System.out.println("entrou");
	  int roomID = dao.CreateRoomBeta(); 
	  dao.CreateRoomBeta(); 
	  String a = String.valueOf(roomID);

	  return "redirect:/r/"+a; 
  }
 
  public String checkRoom(String roomID)
  {
	 
	 File[] listOfFiles=dao.getRoomBeta(roomID);
	 String tst="";
	 for (int i = 0; i < listOfFiles.length; i++) 
	 {
		tst+=listOfFiles[i].getPath()+"\n";
	 }
	 System.out.println("chegou");
	 
	 return tst;
	
  }
  
  @RequestMapping(value = "/r/*")
  public String checkRoom(Model model,HttpServletRequest request)
  {
	 String roomID="";
	 //ArrayList<String> data = new ArrayList<String>();
	 roomID=request.getRequestURI().toString();
	 roomID=roomID.substring(roomID.lastIndexOf("/")+1); //getting the id of the room from url
	 
	 if(roomID=="")
		 return "helloWorld";
	 
	 System.out.println("id: "+roomID);
	 
	 model.addAttribute("greeting",roomID);
	 
	 //data=dao.getFiles(roomID); //getting rs
	 File[] listOfFiles=dao.getRoomBeta(roomID);
	 String tst="<h1>Files:</h1>";
	 for (int i = 0; i < listOfFiles.length; i++) 
	 {
		tst+="<p>"+listOfFiles[i].getPath()+"</p>\n";
	 }
	 model.addAttribute("g",tst);
	 	 
	 return "helloWorld";
  }
}
