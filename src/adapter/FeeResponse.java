package adapter;

public class FeeResponse {
    Double fees;
    Integer numOfCalls;
    Boolean isCache;
    public FeeResponse(Double fees, Integer numOfCalls, Boolean isCache) {
        this.fees = fees;
        this.numOfCalls = numOfCalls;
        this.isCache = isCache;
    }

    public Double getFees() {
        return fees;
    }

    public Integer getNumOfCalls() {
        return numOfCalls;
    }

    public Boolean getCache() {
        return isCache;
    }
}
