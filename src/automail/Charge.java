package automail;

public class Charge {
    Double charge;
    Double cost;
    Double fee;
    Double activity;

    public Charge(Double charge, Double cost, Double fee, Double activity) {
        this.charge = charge;
        this.cost = cost;
        this.fee = fee;
        this.activity = activity;
    }

    public String toString(){
        return String.format("Charge: %.2f | Cost: %.2f | Fee: %.2f | Activity: %.2f", charge,cost,fee,activity);
    }
}
