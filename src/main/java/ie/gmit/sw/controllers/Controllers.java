package ie.gmit.sw.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ie.gmit.sw.DAO.DAO;
import ie.gmit.sw.Service.Service;

@Controller
@Scope("session")
public class Controllers 
{
 
  String roomID="";
  private DAO dao= new DAO();
  private Service srv=new Service();
  
  @RequestMapping(value = "/*")//return index page
  public String helloWorld(Model model) 
  {
    model.addAttribute("test", "Hello hua World!");
    return "helloWorld";
  }
  

  @RequestMapping(value = "/r/*/*")
  public void getFile(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException
  {
	  System.out.println("Download");
	  String fileName=request.getRequestURI().toString();
	  fileName=fileName.substring(fileName.lastIndexOf("/")+1);


	  response.setContentType("application/octet-stream");
      response.addHeader("Content-Disposition", "attachment; filename="+fileName);
      
      byte[] file =srv.getFile(roomID, fileName);
      try
      {
    	  response.getOutputStream().write(file);
          
          //Files.copy
          response.getOutputStream().flush();
      } 
      catch (Exception ex) 
      {
          System.out.println(ex.getMessage());
      }//dao.getFile(fileName);
  }

  @RequestMapping(value="/createRoom")
  public String createRoom(Model model)
  {
	  int roomID = srv.CreateRoomBeta(); 
	  
	  if(roomID==-1)
		  return "redirect:/helloWorld";

	  String a = String.valueOf(roomID);

	  return "redirect:/r/"+a; 
  }
 
  
  @RequestMapping(value = "/r/*", method = RequestMethod.GET)
  public String CheckRoom(Model model,HttpServletRequest request) throws SQLException
  {
	 //ArrayList<String> data = new ArrayList<String>();
	 roomID=request.getRequestURI().toString();
	 roomID=roomID.substring(roomID.lastIndexOf("/")+1); //getting the id of the room from url
	 
	 if(roomID=="")
		 return "helloWorld";
	 
	 model.addAttribute("greeting",roomID);
	 
	 //data=dao.getFiles(roomID); //getting rs
	 ArrayList<String> listOfFiles=dao.getAllFiles(roomID);
	 String tst="<h1>Files:</h1>";
	 for (int i = 0; i < listOfFiles.size(); i++) 
	 {
		 
		tst+="<a href=\"/r/"+roomID+"/"+listOfFiles.get(i)+"\">"+listOfFiles.get(i)+"</a>\n";
	 }
	 model.addAttribute("g",tst);
	 	 
	 return "Room";
  }
  
  @RequestMapping(value = "/savefile", method = RequestMethod.POST)
  public String SaveFile(@RequestParam(value="file", required=true) MultipartFile file) throws IOException
  {
	 if(srv.SaveFile(file, file.getOriginalFilename(), roomID))
		  return "helloWorld";
	  return "redirect:/r/"+roomID;
  }
}
