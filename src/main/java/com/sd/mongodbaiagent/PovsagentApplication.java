package com.sd.mongodbaiagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PovsagentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PovsagentApplication.class, args);
	}

}

//@SpringBootApplication
//public class PovsagentApplication implements CommandLineRunner {
//
//	private final OrderSummaryRepository orderSummaryRepository;
//
//	public PovsagentApplication(OrderSummaryRepository orderSummaryRepository) {
//		this.orderSummaryRepository = orderSummaryRepository;
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(PovsagentApplication.class, args);
//	}
//
//	@Override
//	public void run(String... args) {
//		try {
//			System.out.println(
//					"Orders for CEVA: " +
//							orderSummaryRepository.findByVendorName("CEVA")
//			);
//		} catch (Exception e) {
//			e.printStackTrace(); // IMPORTANT during debugging
//		}
//	}
//}

