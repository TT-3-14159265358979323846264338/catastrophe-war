package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
	@GetMapping("/")
	String index() {
		return "auth/index";
	}
	
	@GetMapping("/login")
	String login(@RequestParam(required = false) String logout, Model model) {
		if (logout != null) {
			model.addAttribute("message", "ログアウトしました。");
		}
		return "guest/login/login";
	}
	
	@GetMapping("/error/logout")
	String errorLogout() {
		return "guest/error-logout/error-logout";
	}
}