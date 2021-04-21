package automail;

public class MailPoolFactory {

	public static MailPool getInstance(int nrobots, double chargeThreshold,
			ChargeServiceInterface chargeService) {
		
		if (chargeThreshold == 0) {
			return new MailPool(nrobots);
		} else {
			return new PriorityMailPool(nrobots, chargeThreshold,chargeService);
		}
	}
}
