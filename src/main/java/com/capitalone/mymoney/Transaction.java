package com.capitalone.mymoney;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "isPending", "payeeNameOnlyForTesting", "aggregationTime", "accountId", "clearDate", "memoOnlyForTesting", "transactionId", "categorization", "merchant" })
public class Transaction {
	private Double amount;
	private Boolean isPending;
	private String payeeNameOnlyForTesting;
	private Long aggregationTime;
	private String accountId;
	private Long clearDate;
	private String memoOnlyForTesting;
	private String transactionId;
    private String rawMerchant;
	private String categorization;
	private String merchant;
	private String transactionTime;
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	@JsonProperty("is-pending") 
	public Boolean getIsPending() {
		return isPending;
	}
	@JsonProperty("is-pending") 
	public void setIsPending(Boolean isPending) {
		this.isPending = isPending;
	}
	@JsonProperty("payee-name-only-for-testing") 
	public String getPayeeNameOnlyForTesting() {
		return payeeNameOnlyForTesting;
	}
	@JsonProperty("payee-name-only-for-testing") 
	public void setPayeeNameOnlyForTesting(String payeeNameOnlyForTesting) {
		this.payeeNameOnlyForTesting = payeeNameOnlyForTesting;
	}
	@JsonProperty("aggregation-time") 
	public Long getAggregationTime() {
		return aggregationTime;
	}
	@JsonProperty("aggregation-time") 
	public void setAggregationTime(Long aggregationTime) {
		this.aggregationTime = aggregationTime;
	}
	@JsonProperty("account-id") 
	public String getAccountId() {
		return accountId;
	}
	@JsonProperty("account-id") 
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	@JsonProperty("clear-date") 
	public Long getClearDate() {
		return clearDate;
	}
	@JsonProperty("clear-date") 
	public void setClearDate(Long clearDate) {
		this.clearDate = clearDate;
	}
	@JsonProperty("memo-only-for-testing") 
	public String getMemoOnlyForTesting() {
		return memoOnlyForTesting;
	}
	@JsonProperty("memo-only-for-testing") 
	public void setMemoOnlyForTesting(String memoOnlyForTesting) {
		this.memoOnlyForTesting = memoOnlyForTesting;
	}	
	@JsonProperty("transaction-id") 
	public String getTransactionId() {
		return transactionId;
	}
	@JsonProperty("transaction-id") 
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	@JsonProperty("raw-merchant") 
	public String getRawMerchant() {
		return rawMerchant;
	}
	@JsonProperty("raw-merchant") 
	public void setRawMerchant(String rawMerchant) {
		this.rawMerchant = rawMerchant;
	}
	public String getCategorization() {
		return categorization;
	}
	public void setCategorization(String categorization) {
		this.categorization = categorization;
	}
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	@JsonProperty("transaction-time")
	public String getTransactionTime() {
		return transactionTime;
	}
	@JsonProperty("transaction-time")
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
}
