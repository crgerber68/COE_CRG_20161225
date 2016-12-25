package com.capitalone.mymoney;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**

Our API is documented at https://doc.level-labs.com - username: interview@levelmoney.com password: password2. We'd like you to write a program that:
·         Loads a user's transactions from the GetAllTransactions endpoint
·         Determines how much money the user spends and makes in each of the months for which we have data, and in the "average" month. What "average" means is up to you.
·         Output these numbers in the following format (and optionally in a more pretty format, if you see fit)

{"2014-10": {"spent": "$200.00", "income": "$500.00"},
 "2014-11": {"spent": "$1510.05", "income": "$1000.00"},
...

 "2015-04": {"spent": "$300.00", "income": "$500.00"},
 "average": {"spent": "$750.00", "income": "$950.00"}}

You have considerable latitude on how to display this data, obtain it, and what language to use. Please do this in the way that feels most comfortable for you. For many of our applicants, they prefer to use a script you run from the command line. For some, it is a webpage that displays things. For others, it’s a live code notebook. What’s important is that it is reproducible by us.
We’d also like you to try and add at least one “additional feature” to this program (and if you’re able, all of them). They’re listed below as command line switches for a terminal program, but we’d accept any method that lets a user decide how to display this data.
·         --ignore-donuts: The user is so enthusiastic about donuts that they don't want donut spending to come out of their budget. Disregard all donut-related transactions from the spending. You can just use the merchant field to determine what's a donut - donut transactions will be named “Krispy Kreme Donuts” or “DUNKIN #336784”.
·         --crystal-ball: We expose a GetProjectedTransactionsForMonth endpoint, which returns all the transactions that have happened or are expected to happen for a given month. It looks like right now it only works for this month, but that's OK. Merge the results of this API call with the full list from GetAllTransactions and use it to generate predicted spending and income numbers for the rest of this month, in addition to previous months.
·         --ignore-cc-payments: Paying off a credit card shows up as a credit transaction and a debit transaction, but it's not really "spending" or "income". Make your aggregate numbers disregard credit card payments. For the users we give you, credit card payments will consist of two transactions with opposite amounts (e.g. 5000000 centocents and -5000000 centocents) within 24 hours of each other. For verification, you should also output a list of the credit card payments you detected - this can be in whatever format you like.

 */

@Path("mymoney")
public class MyMoneyRestService {
	private final static Logger logger = Logger.getLogger(MyMoneyRestService.class);
	private final static String CAPITAL_ONE_API_URI = "https://2016.api.levelmoney.com/api";
	private final static String ALL_TRANSACTIONS_ENDPOINT = "/v2/core/get-all-transactions";
	private final static String PROJECTED_TRANSACTIONS_ENDPOINT = "/v2/core/projected-transactions-for-month";
	private final static String UID = "1110590645";
	private final static String TOKEN = "E568A6441CB93E7A2025E73A149D29D9";
	private final static String API_TOKEN = "AppTokenForInterview";
	private final static SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH);
	private final static SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM");
	private final static Predicate<Transaction> ignoreDonutsFilter = t -> !t.getRawMerchant().equalsIgnoreCase("Krispy Kreme Donuts") && !t.getRawMerchant().contains("DUNKIN");
	
    protected TransactionsResponse getProjectedTransactionsForMonth() throws JsonParseException, JsonMappingException, IOException {
    	logger.trace("begin getProjectedTransactionsForMonth");
    	String request = "{\"args\":{\"uid\":"+UID+",\"token\":\""+TOKEN+"\",\"api-token\":\""+API_TOKEN+"\",\"json-strict-mode\":false,\"json-verbose-response\":false}, \"year\": 2017, \"month\": 1}"; 

    	Client client = ClientBuilder.newClient();
    	Response response = client.target(CAPITAL_ONE_API_URI)
    			.path(PROJECTED_TRANSACTIONS_ENDPOINT)
    			.request(MediaType.APPLICATION_JSON)
    			.post(Entity.json(request));

    	logger.info(Integer.toString(response.getStatus()));
    	logger.info(response.getStatusInfo().toString());

    	ObjectMapper om = new ObjectMapper();
    	TransactionsResponse tr = om.readValue(response.readEntity(String.class), TransactionsResponse.class);	

    	logger.info("nbr transactions is " + tr.getTransactions().size());
    	logger.info("end getProjectedTransactionsForMonth");
    	return tr;
    }
	
	protected TransactionsResponse getAllTransactions() throws JsonParseException, JsonMappingException, IOException {
    	logger.trace("begin getAllTransactions");
	    String request = "{\"args\":{\"uid\":"+UID+",\"token\":\""+TOKEN+"\",\"api-token\":\""+API_TOKEN+"\",\"json-strict-mode\":false,\"json-verbose-response\":false}}"; 
	    
		Client client = ClientBuilder.newClient();
		Response response = client.target(CAPITAL_ONE_API_URI)
				                  .path(ALL_TRANSACTIONS_ENDPOINT)
				                  .request(MediaType.APPLICATION_JSON)
				                  .post(Entity.json(request));
		
    	logger.info(Integer.toString(response.getStatus()));
    	logger.info(response.getStatusInfo().toString());
	
		ObjectMapper om = new ObjectMapper();
		TransactionsResponse tr = om.readValue(response.readEntity(String.class), TransactionsResponse.class);
	
    	logger.info("nbr transactions is " + tr.getTransactions().size());
    	logger.trace("end getAllTransactions");
		return tr;
	}
	
	/**
	 * @param ignoreDonuts
	 * @param crystalBall
	 * @param ignoreCCPayments
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TransactionReport> getTransactionReports(
			@DefaultValue("false") @QueryParam("ignoreDonuts") boolean ignoreDonuts,
			@DefaultValue("false") @QueryParam("crystalBall") boolean crystalBall) {
    	logger.trace("begin getTransactionReports");
    	logger.info("ignoreDonuts="+ignoreDonuts);
    	logger.info("crystalBall="+crystalBall);

		List<TransactionReport>	reports = new ArrayList<TransactionReport>();
		
		try {
		    List<Transaction> transactions = new ArrayList<>(); 
		    List<Transaction> filteredTransactions = new ArrayList<>(); 
		    Predicate<Transaction> filter = t -> t != null;
		    
		    if(ignoreDonuts) {
                filter = filter.and(ignoreDonutsFilter);
		    } 
		    
			transactions.addAll(getAllTransactions().getTransactions());
			
		    if(crystalBall) {
			    transactions.addAll(getProjectedTransactionsForMonth().getTransactions());
		    } 
			
		    transactions.stream()
		    .filter(filter)
		    .forEach((t) -> {
		    	try {
		    		Date tt = LONG_DATE_FORMAT.parse(t.getTransactionTime());
		    		t.setTransactionTime(SHORT_DATE_FORMAT.format(tt));
		    		filteredTransactions.add(t);
		    	} catch (ParseException e) {
		    		e.printStackTrace();
		    	}
		    });

		    Map<String, List<Transaction>> tByMonth = filteredTransactions.stream()
		    		.collect(
		    				Collectors.groupingBy(Transaction::getTransactionTime));

    	    logger.info("tByMonty size=" + tByMonth.size());
		   
		    List<String> keys = tByMonth.keySet().stream().collect(Collectors.toList()); 
		    
		    Collections.sort(keys, new Comparator<String>() {
		        public int compare(String o1, String o2) {
                    int comparator = 0;		            	
		        	try {
			    		Date d1 = SHORT_DATE_FORMAT.parse(o1);
			    		Date d2 = SHORT_DATE_FORMAT.parse(o2);
			    		comparator = d1.compareTo(d2);
			    	} catch (ParseException e) {
			    		e.printStackTrace();
			    	}
		        	
		           return comparator;
		        }
		    });
		    
		    for(String key: keys) {
		    	Double spent = tByMonth.get(key).stream()
		    			.filter(t -> t.getAmount() < 0)
		    			.mapToDouble(t -> t.getAmount())
		    			.reduce(0.0, Double::sum);
		    	
		    	Double income = tByMonth.get(key).stream()
		    			.filter(t -> t.getAmount() > 0)
		    			.mapToDouble(t -> t.getAmount())
		    			.reduce(0.0, Double::sum);
		    	
		    	TransactionReport tRpt = new TransactionReport();
		    	tRpt.setName(key);
		    	tRpt.setSpent(spent);
		    	tRpt.setIncome(income);

		    	reports.add(tRpt);
		    }
		    
	    	TransactionReport tRpt = new TransactionReport();
	    	tRpt.setName("average");
	    	tRpt.setSpent((new Double(reports.stream().mapToDouble(TransactionReport::getSpent).average().getAsDouble())));
	    	tRpt.setIncome((new Double(reports.stream().mapToDouble(TransactionReport::getIncome).average().getAsDouble())));

		    reports.add(tRpt);
		} catch(Exception e) {
    	    logger.error(e.getMessage());
		}
		
    	logger.trace("end getTransactionReports");
		return reports;
	}
}
