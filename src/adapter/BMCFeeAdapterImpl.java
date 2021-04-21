package adapter;

import java.util.HashMap;
import java.util.Map;

import com.unimelb.swen30006.wifimodem.WifiModem;

import simulation.Building;

public class BMCFeeAdapterImpl implements FeeAdapter{

    private WifiModem modem;

    private Map<Integer,Double> floorServiceFeeMap = new HashMap<Integer,Double>();
    
    private static int successCalls = 0;
    private static int failureCalls = 0;

    public BMCFeeAdapterImpl() {
        try {
            this.modem = WifiModem.getInstance(Building.MAILROOM_LOCATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

    @Override
    public FeeResponse getFee(int floorNum, boolean cachePreference) {
        int numOfCalls = 0;
        boolean isCache = false;
        boolean performLookup = true;
        Double serviceFee = 0D;

        if(cachePreference) {
            if(floorServiceFeeMap.containsKey(floorNum)) {
                serviceFee = floorServiceFeeMap.get(floorNum);
                isCache = true;
                performLookup = false;
            }
        }

        if(performLookup) {
            serviceFee = modem.forwardCallToAPI_LookupPrice(floorNum);
            numOfCalls++;
            if (serviceFee < 0) {
                if (floorServiceFeeMap.containsKey(floorNum)) {
                    serviceFee = floorServiceFeeMap.get(floorNum);
                    isCache = true;
                    failureCalls++;
                } else {
                    // making the lookup in a loop until it returns a positive value. Negative means failure call.
                    while (serviceFee < 0) {
                        failureCalls++;
                        serviceFee = modem.forwardCallToAPI_LookupPrice(floorNum);
                        numOfCalls++;
                        if(serviceFee > 0) {
                            successCalls++;
                        }
                    }
                }
            } else {
                successCalls++;
                floorServiceFeeMap.put(floorNum, serviceFee);
            }
        }

        FeeResponse response = new FeeResponse(serviceFee, numOfCalls, isCache);
        return response;
    }



	@Override
	public Integer getSuccessCount() {
		return successCalls;
	}



	@Override
	public Integer getFailureCount() {
		return failureCalls;
	}



	@Override
	public void resetStats() {
		successCalls = 0;
	    failureCalls = 0;
	}
}
