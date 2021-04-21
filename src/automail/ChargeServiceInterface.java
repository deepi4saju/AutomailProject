package automail;

public interface ChargeServiceInterface {
	
	 public Charge calculateCharge(MailItem deliveryItem);

	 public void updateStats(Charge charge);
	 
	 public Double getTotalCharge();
	 
	 public Double getTotalCost();
	 
	 public Double getTotalFee();
	 
	 public Double getTotalActivity();
	 
	 public Integer getFeeCallSuccessCount();
	    
	 public Integer getFeeCallFailureCount();
	 
	 public void resetStats();

}
