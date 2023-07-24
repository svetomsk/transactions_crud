package com.svetomsk.crudtransactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestCrudTransactionsApplication {

	public static void main(String[] args) {
		SpringApplication.from(CrudTransactionsApplication::main).with(TestCrudTransactionsApplication.class).run(args);
	}

}
