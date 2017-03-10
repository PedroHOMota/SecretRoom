package ie.gmit.sw.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ie.gmit.sw.DAO.DAO;

@Controller
public class Controllers 
{
  private DAO dao;
  @RequestMapping(value = "/*")//return index page
  public String helloWorld(Model model) 
  {
    model.addAttribute("test", "Hello World!");
    return "helloWorld";
  }
  
  @RequestMapping(value = "/r/*")
  public String checkRoom(Model model,HttpServletRequest request)
  {
	 String roomID="";
	 ArrayList<String> data = new ArrayList<String>();
	 
	 roomID=request.getRequestURI().toString();
	 roomID=roomID.substring(roomID.lastIndexOf("/")+1); //getting the id of the room from url
	 model.addAttribute("greeting",roomID);
	 
	 data=dao.getData(roomID); //getting rs
	 
	 if(data.size()==0) //if empty room doesn't exist
	 {
		 model.addAttribute("greeting",roomID);
		 return "notFound"; //return not found page
	 }
	 
	 return "helloWorld";
  }


}
