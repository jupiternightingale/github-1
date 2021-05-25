package org.hth.internship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InternshipApplication {

	public static void main(String[] args) {

		System.out.println("InternshipApplication.main");
		for(int i = 0; i < 100; i=i+1) {
			System.out.println("Hi Nicole! " + i);
		}
	}

}
