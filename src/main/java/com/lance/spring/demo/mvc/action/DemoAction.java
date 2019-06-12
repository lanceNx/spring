package com.lance.spring.demo.mvc.action;


import com.lance.spring.demo.service.impl.DemoService;
import com.lance.spring.v1.annotation.Autowried;
import com.lance.spring.v1.annotation.Controller;
import com.lance.spring.v1.annotation.RequestParam;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class DemoAction {
	
	@Autowried
    private DemoService demoService;

	public void query(HttpServletRequest req,HttpServletResponse resp,
		   @RequestParam("name") String name){
		String result = demoService.get(name);
		System.out.println(result);
//		try {
//			resp.getWriter().write(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Test
    public void test() {
        String result = demoService.get("123");
        System.out.println(result);
    }

	public void edit(HttpServletRequest req,HttpServletResponse resp,Integer id){

	}
	
}
