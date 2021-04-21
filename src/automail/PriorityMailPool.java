package automail;

import java.util.Comparator;


public class PriorityMailPool extends MailPool{

    double chargeThreshold;
    ChargeServiceInterface chargeService;

    public PriorityMailPool(int nrobots, double chargeThreshold, ChargeServiceInterface chargeService){
       super(nrobots);
       this.chargeThreshold = chargeThreshold;
       this.chargeService = chargeService;
    }

    /**
     * Adds an item to the mail pool based on priority with respect to charge
     * @param mailItem the mail item being added.
     */
    @Override
    public void addToPool(MailItem mailItem) {
        Item item = new Item(mailItem);
        item.charge = chargeService.calculateCharge(mailItem,true).charge;
        getPool().add(item);
        getPool().sort(new ItemComparator());
    }

/*
Comparator class to sort based on chargeThreshold &b destination.
 */
    public class ItemComparator implements Comparator<Item> {
        @Override
        public int compare(Item i1, Item i2) {
        	int x1 = 0;
            if (i1.charge > chargeThreshold || i2.charge > chargeThreshold) {
            	x1 = Double.compare(i2.charge, i1.charge);
            }       
            
            if (x1 != 0)
 				return x1;
            
 			return (int) (Integer.valueOf(i1.destination)
 					.compareTo(Integer.valueOf(i2.destination)));
            
        }
    }
    

}
