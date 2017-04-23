package ie.gmit.sw.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import ie.gmit.sw.Service.Service;

@Controller
@Scope("session")
public class Controllers 
{
 
  private String roomID="";
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
      
      byte[] file =srv.GetFile(roomID, fileName);
      try
      {
    	  response.getOutputStream().write(file);
          response.getOutputStream().flush();
      } 
      catch (Exception ex) 
      {
          System.out.println(ex.getMessage());
      }
  }

  @RequestMapping(value="/createRoom")
  public String CreateRoom(Model model)
  {
	  int roomID = srv.CreateRoomBeta(); 
	  
	  if(roomID==-1)
		  return "redirect:/";

	  String a = String.valueOf(roomID);

	  return "redirect:/r/"+a; 
  }
 
  
  @RequestMapping(value = "/r/*", method = RequestMethod.GET)
  public String CheckRoom(Model model,HttpServletRequest request) throws SQLException
  {
	 roomID=request.getRequestURI().toString();
	 roomID=roomID.substring(roomID.lastIndexOf("/")+1); //getting the id of the room from url
	 
	 if(roomID=="")
		 return "redirect:/";
	  
	 ArrayList<String> listOfFiles=srv.GetAllFilesName(roomID);
	 System.out.println(listOfFiles.size());
	 String tst="<h1>  </h1>";
	 for (int i = 0; i < listOfFiles.size(); i++) 
	 {
		tst+="<tr><td style=\"float:left\"><a href=\"/r/"+roomID+"/"+listOfFiles.get(i)+"\">"+listOfFiles.get(i)+"</a></td></tr>";
	 }
	 model.addAttribute("files",tst);
	 model.addAttribute("id", roomID);
	 	 
	 return "Room";
  }
  
  @RequestMapping(value = "/savefile", method = RequestMethod.POST)
  public String SaveFile(@RequestParam(value="file", required=true) MultipartFile file, Model model) throws IOException
  {
	 //If file is succesufully saved, return the user to room's page
	 //If fails return showing erro message
	 if(srv.SaveFile(file, file.getOriginalFilename(), roomID))
		  return "redirect:/r/"+roomID;
	 
	 model.addAttribute("error", "Could not upload file. File is larger than 100mb or already exists");
	 return "redirect:/r/"+roomID;
  }
  
  @RequestMapping(value = "/savemessage", method = RequestMethod.POST)
  public void SaveMessage(String msg, String user, String date) throws IOException, ParseException
  {
	 System.out.println(date);
	 srv.SaveMessage(msg, user, roomID, date); 
	
  }
  
  @ResponseBody
  @RequestMapping(value = "/getmessage", method = RequestMethod.POST)
  public String GetMessage(@RequestBody String msgCount) throws IOException, SQLException, ParseException
  {
	 msgCount=msgCount.substring(9);
	 System.out.println(msgCount);
	 msgCount=msgCount.replace("+", " ");
	 msgCount=msgCount.replace("%3A", ":");
	 System.out.println(msgCount);
	 ArrayList<Map<String, String>> msgs = srv.GetMessages(roomID,msgCount);
	
	 if(msgs==null)
		 return "";
	 System.out.println(msgs.size());
	 ObjectMapper mapper = new ObjectMapper();
	 String rsp=mapper.writeValueAsString(msgs);
	 System.out.println(rsp);
	 return rsp;
  }
  
}
