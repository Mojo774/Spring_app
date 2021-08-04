package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	public static void main(String[] args) {
		String s1 = "";
		String s2 = null;

		System.out.println(StringUtils.isEmpty(s1));
		System.out.println(StringUtils.isEmpty(s2));
	}

}
