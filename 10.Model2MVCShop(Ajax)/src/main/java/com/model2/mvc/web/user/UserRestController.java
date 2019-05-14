package com.model2.mvc.web.user;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;


//==> ȸ������ RestController
@RestController
@RequestMapping("/user/*")
public class UserRestController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method ���� ����
		
	public UserRestController(){
		System.out.println(this.getClass());
	}
	
	@RequestMapping( value="json/getUser/{userId}", method=RequestMethod.GET )
	public User getUser( @PathVariable String userId ) throws Exception{
		
		System.out.println("/user/json/getUser : GET");
		
		//Business Logic
		return userService.getUser(userId);
	}

	@RequestMapping( value="json/login", method=RequestMethod.POST )
	public User login(	@RequestBody User user,
									HttpSession session ) throws Exception{
	
		System.out.println("/user/json/login : POST");
		//Business Logic
		System.out.println("::"+user);
		User dbUser=userService.getUser(user.getUserId());
		
		if( user.getPassword().equals(dbUser.getPassword())){
			session.setAttribute("user", dbUser);
		}
		
		return dbUser;
	}
	
	@RequestMapping( value="json/addUser", method=RequestMethod.POST )
	public User addUser(@RequestBody User user,
									HttpSession session ) throws Exception{
	
		System.out.println("/user/json/addUser : POST");
		//Business Logic
		System.out.println("::"+user);
		
		userService.addUser(user);
		
		User dbUser = userService.getUser(user.getUserId());

		return dbUser;
	}
	
	@RequestMapping( value="json/updateUser", method=RequestMethod.POST )
	public User updateUser(@RequestBody User user) throws Exception{
	
		System.out.println("/user/json/addUser : POST");
		//Business Logic
		System.out.println("::"+user);
		
		userService.updateUser(user);
		
		User dbUser = userService.getUser(user.getUserId());

		return dbUser;
	}
	
	@RequestMapping(value="json/listUser", method=RequestMethod.POST)
	public Map listUser(@RequestBody Search search) throws Exception {
		
		System.out.println("1 "+search);
		System.out.println("2 "+search.getStartRowNum());
		System.out.println("3 "+search.getEndRowNum());
		
		Map<String, Object> map=userService.getUserList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), 1, 5);
		System.out.println(resultPage);
		
		map.put("list", map.get("list"));
		map.put("resultPage", resultPage);
		map.put("search", search);
		
		return map;
	}
}