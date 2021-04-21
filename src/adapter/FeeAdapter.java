package adapter;

public interface FeeAdapter {

    Double getFee(int floor);
    
    Integer getSuccessCount();
    
    Integer getFailureCount();
    
    public void resetStats();
}
