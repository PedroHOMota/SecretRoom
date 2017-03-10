package ie.gmit.sw.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ie.gmit.sw.DAO.DAO;

@Controller
public class Controllers 
{
  private DAO dao;
  @RequestMapping(value = "/*")
  public String helloWorld(Model model) {
    model.addAttribute("test", "Hello World!");
    return "helloWorld";
  }
  
  @RequestMapping(value = "/r/*")
  public String checkRoom(Model model,HttpServletRequest request)
  {
	 String roomID="";
	 String[] data = null;
	 
	 roomID=request.getRequestURI().toString();
	 roomID=roomID.substring(roomID.lastIndexOf("/")+1);
	 model.addAttribute("greeting",roomID);
	 
	 data=dao.getData(roomID);
	 
	 if(data==null)
	 {
		 model.addAttribute("greeting",roomID);
		 return "notFound"; //return not found page
	 }
	 
	 return "helloWorld";
  }
  
  
}
