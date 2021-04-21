package automail;

import java.util.Properties;

import adapter.BMCFeeAdapterImpl;
import adapter.FeeAdapter;
import adapter.FeeResponse;

public class ChargeService implements ChargeServiceInterface{

    private static ChargeService _instance = null;
    private FeeAdapter feeAdapter;
    private Properties automailProperties;

    private static Double totalCharge = 0D;
    private static Double totalBillableActivityCost = 0D;
    private static Double totalFee = 0D;
    private static Double totalBillableActivity = 0D;
    

    private ChargeService(Properties automailProperties) {
        this.feeAdapter = new BMCFeeAdapterImpl();
        this.automailProperties = automailProperties;
    }

    public static ChargeService getInstance(Properties automailProperties) {
        if (_instance == null) {
            synchronized (ChargeService.class) {
                if (_instance == null) {
                    _instance = new ChargeService(automailProperties);
                }
            }
        }
        return _instance;
    }

    @Override
    public Charge calculateCharge(MailItem deliveryItem, boolean estimateCharge) {
        //if estimateCharge is true, then the cachePreference is also true
        FeeResponse feeResponse = feeAdapter.getFee(deliveryItem.getDestFloor(),estimateCharge);

        Double activityCost = 0D;
        //configurable property below
        Double activityUnitPrice = Double.parseDouble(automailProperties.get("ActivityUnitPrice").toString());
        Double stepMovementUnitCost = Double.parseDouble(automailProperties.get("stepMovementCost").toString());

        Double movementCostUnit = stepMovementUnitCost * deliveryItem.getDestFloor() * 2;
        Double lookUpCost = Double.parseDouble(automailProperties.get("lookUpCost").toString());
        Double markUpPercentage = Double.parseDouble(automailProperties.get("MarkupPercentage").toString());

        Double activityUnits = movementCostUnit + lookUpCost*1;
        activityCost = activityUnits * activityUnitPrice;
        Double cost = feeResponse.getFees() + activityCost;
        Double charge = cost * (1 + markUpPercentage);
        Charge chargeObj = new Charge(charge, cost, feeResponse.getFees(), activityCost);

        Double billableActivity = 0D;
        if(estimateCharge) {
            billableActivity = lookUpCost*feeResponse.getNumOfCalls();
        } else {
            billableActivity = movementCostUnit + lookUpCost * feeResponse.getNumOfCalls();
            totalCharge += charge;
            totalFee += feeResponse.getFees();
        }
        totalBillableActivity += billableActivity;
        totalBillableActivityCost += billableActivity * activityUnitPrice;
        
        return chargeObj;
    }

	@Override
	public Double getTotalCharge() {
		return totalCharge;
	}

	@Override
	public Double getTotalBillableActivity() {
		return totalBillableActivity;
	}

	@Override
	public Double getTotalFee() {
		return totalFee;
	}

	@Override
	public Double getTotalBillableActivityCost() {
		return totalBillableActivityCost;
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
        totalBillableActivityCost = 0D;
		totalFee = 0D;
        totalBillableActivity = 0D;
		feeAdapter.resetStats();
	}

}
