package automail;

import java.util.Properties;

import adapter.FeeAdapter;

public class ChargeService implements ChargeServiceInterface{

    private static ChargeService _instance = null;
    FeeAdapter feeAdapter;
    Properties automailProperties;

    private static Double totalCharge = 0D;
    private static Double totalCost = 0D;
    private static Double totalFee = 0D;
    private static Double totalActivity = 0D;
    

    private ChargeService(FeeAdapter feeAdapter, Properties automailProperties) {
        this.feeAdapter = feeAdapter;
        this.automailProperties = automailProperties;
    }

    public static ChargeService getInstance(FeeAdapter feeAdapter, Properties automailProperties) {
        if (_instance == null) {
            synchronized (ChargeService.class) {
                if (_instance == null) {
                    _instance = new ChargeService(feeAdapter, automailProperties);
                }
            }
        }
        return _instance;
    }

    @Override
    public Charge calculateCharge(MailItem deliveryItem) {
        Double serviceFee = feeAdapter.getFee(deliveryItem.getDestFloor());

        Double activityCost = 0D;
        //configurable property below
        Double activityUnitPrice = Double.parseDouble(automailProperties.get("activityUnitPrice").toString());
        Double stepMovementCost = Double.parseDouble(automailProperties.get("stepMovementCost").toString());

        Double movementCost = stepMovementCost * deliveryItem.getDestFloor() * 2;
        Double lookUpCost = Double.parseDouble(automailProperties.get("lookUpCost").toString());
        Double markUpPercentage = Double.parseDouble(automailProperties.get("markUpPercentage").toString());

        Double activityUnits = movementCost + lookUpCost;
        activityCost = activityUnits * activityUnitPrice;
        Double cost = serviceFee + activityCost;
        Double charge = cost * (1 + markUpPercentage);
        Charge chargeObj = new Charge(charge, cost, serviceFee, activityCost);
        
        return chargeObj;
    }

    public void updateStats(Charge charge) {
        totalCharge += charge.charge;
        totalActivity += charge.activity;
        totalCost += charge.cost;
        totalFee += charge.fee;
    }

	@Override
	public Double getTotalCharge() {
		return totalCharge;
	}

	@Override
	public Double getTotalCost() {
		return totalCost;
	}

	@Override
	public Double getTotalFee() {
		return totalFee;
	}

	@Override
	public Double getTotalActivity() {
		return totalActivity;
	}
	
	@Override
	public Integer getFeeCallSuccessCount() {
		return feeAdapter.getSuccessCount();
	}

	@Override
	public Integer getFeeCallFailureCount() {
		return feeAdapter.getFailureCount();
	}
	
	public void resetStats() {
		totalCharge = 0D;
		totalCost = 0D;
		totalFee = 0D;
		totalActivity = 0D;
		feeAdapter.resetStats();
	}

}
