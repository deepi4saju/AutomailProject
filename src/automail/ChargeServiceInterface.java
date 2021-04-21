package automail;

public interface ChargeServiceInterface {
	
	 public Charge calculateCharge(MailItem deliveryItem, boolean estimateCharge);
	 
	 public Double getTotalCharge();
	 
	 public Double getTotalBillableActivity();
	 
	 public Double getTotalFee();
	 
	 public Double getTotalBillableActivityCost();
	 
	 public Integer getFeeCallSuccessCount();
	    
	 public Integer getFeeCallFailureCount();
	 
	 public void resetStats();

}
