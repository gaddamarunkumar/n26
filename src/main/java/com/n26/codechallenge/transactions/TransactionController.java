package com.n26.codechallenge.transactions;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TransactionController
 * 
 * @author Gaddam
 *
 */

@RestController
public class TransactionController {

	private Logger logger = Logger.getLogger(TransactionController.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/transactions")
	@ResponseBody
	public ResponseEntity<Void> addTransaction(@RequestBody String transaction) throws Exception {
		logger.info("Recieved transaction :" + transaction);
		int response = 0;
		try {
			JsonNode actualObj = mapper.readTree(transaction);

			response = transactionService.persistTransaction(actualObj.path("amount").asDouble(),
					actualObj.path("timestamp").asLong());

			logger.info("response " + response);
		} catch (Exception exception) {
			logger.error("Error " + transaction, exception);
			response = 500;
		}
		return new ResponseEntity<Void>(HttpStatus.valueOf(response));
	}

	@GetMapping("/statistics")
	@ResponseBody
	public String getStatistics() throws Exception {
		logger.info("Getting statistics");
		String responseStr = "";
		try {
			responseStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(transactionService.getStatistics());
			logger.info("Statistics @" + Calendar.getInstance().getTime() + ": " + responseStr);
		} catch (Exception exception) {
			exception.printStackTrace();
			logger.error("Error ", exception);
			JsonNode rootNode = mapper.createObjectNode();
			((ObjectNode) rootNode).put("error", "exception");
			responseStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		}
		return responseStr;
	}
}
