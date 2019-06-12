package com.lance.spring.demo.mvc.action;


import com.lance.spring.demo.service.IDemoService;
import com.lance.spring.v1.annotation.Autowried;
import com.lance.spring.v1.annotation.Controller;
import com.lance.spring.v1.annotation.RequestMapping;

@Controller
public class MyAction {

		@Autowried
        IDemoService demoService;

		public void query(){

		}
	
}
