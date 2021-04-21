package adapter;

public interface FeeAdapter {

    FeeResponse getFee(int floor, boolean cachePreference);
    
    Integer getSuccessCount();
    
    Integer getFailureCount();
    
    public void resetStats();
}
