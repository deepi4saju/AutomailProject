package adapter;

import java.util.HashMap;
import java.util.Map;

import com.unimelb.swen30006.wifimodem.WifiModem;

public class BMCFeeAdapterImpl implements FeeAdapter{

    private WifiModem modem;

    private Map<Integer,Double> floorServiceFeeMap = new HashMap<Integer,Double>();
    
    private static int successCalls = 0;
    private static int failureCalls = 0;

    public BMCFeeAdapterImpl(WifiModem modem) {
        this.modem = modem;
    }
    
    

    @Override
    public Double getFee(int floorNum) {

        Double serviceFee = modem.forwardCallToAPI_LookupPrice(floorNum);
        if(serviceFee < 0){
            if(floorServiceFeeMap.containsKey(floorNum)) {
                serviceFee = floorServiceFeeMap.get(floorNum);
                failureCalls++;
            } else {
                // making the lookup in a loop until it returns a positive value. Negative means failure call.
                while (serviceFee < 0) {
                    failureCalls++;
                    serviceFee = modem.forwardCallToAPI_LookupPrice(floorNum);
                }
            }
        } else {
            successCalls++;
            floorServiceFeeMap.put(floorNum,serviceFee);
        }

        return serviceFee;
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
