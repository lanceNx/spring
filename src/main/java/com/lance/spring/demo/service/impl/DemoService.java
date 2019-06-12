package com.lance.spring.demo.service.impl;

import com.lance.spring.demo.service.IDemoService;
import com.lance.spring.v1.annotation.Service;

@Service
public class DemoService implements IDemoService {

	public String get(String name) {
		return "My name is " + name;
	}

}
