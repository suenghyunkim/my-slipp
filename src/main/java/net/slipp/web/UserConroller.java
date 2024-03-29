package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.User;
import net.slipp.domain.UserRepository;

@Controller
@RequestMapping("/users")
public class UserConroller {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String login(String userId,String password,HttpSession session) {
		User user = userRepository.findByUserId(userId);
		if(user == null) {
			return "redirect:/users/loginForm";
		}
		
		if(!user.matchPassword(password)) {
			return "redirect:/users/loginForm";
		}
		
		System.out.println("loginSuccess!");
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		
		return "redirect:/";
	}
	
	@PostMapping("")
	public String create(User user) {
		System.out.println("user : "+user);
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("users",userRepository.findAll());
		return "/user/list";
	}
	
	@GetMapping("/form")
	public String form() {
		return "/user/form";
	}
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id,Model model,HttpSession session) {
		if(HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/users/loginForm";
		}
		
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);
		if(!sessionedUser.matchId(id)) {
			throw new IllegalStateException("You can't update the anther user");
		}
		
		User user = userRepository.findById(id).get();
		model.addAttribute("user",user);
		return "/user/updateForm";
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id,User updatedUser,HttpSession session) {
		if(HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/users/loginForm";
		}
		
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);
		if(!sessionedUser.matchId(id)) {
			throw new IllegalStateException("You can't update the anther user");
		}

		User user = userRepository.findById(id).get();
		user.update(updatedUser);
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		return "redirect:/";
	}
}
