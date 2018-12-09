package actors;

import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.Props;
import model.BuyList;
import model.Product;

public class BuyActor extends AbstractActor {

	public static Props getProps() {
		return Props.create(BuyActor.class);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(BuyList.class, bl -> {
			BuyList buyList = (BuyList) bl;
			getSender().tell(buyProducts(buyList.getProducts()), getSelf());
		}).build();
	}

	/**
	 * Buy the product that passed to buy actor
	 * 
	 * @param products
	 * @return true when buy products, false otherwise
	 */
	private boolean buyProducts(List<Product> products) {
		// TODO: implement method that buy the products

		return false;
	}
}
