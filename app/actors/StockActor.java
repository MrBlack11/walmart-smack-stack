package actors;

import javax.inject.Inject;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import model.BuyList;
import model.ResultSearch;
import model.SearchParam;

public class StockActor extends AbstractActor {
	private ActorRef searchActor;
	private ActorRef buyActor;

	public static Props getProps() {
		return Props.create(StockActor.class);
	}

	public StockActor() {
		this.searchActor = getContext().actorOf(new RoundRobinPool(10).props(SearchActor.getPros()), "search");
		this.buyActor = getContext().actorOf(new RoundRobinPool(10).props(BuyActor.getProps()), "buy");
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SearchParam.class, sp -> {
			searchActor.tell(sp, getSelf());

		}).match(ResultSearch.class, rs -> {
			rs.getMasterActor().tell(rs, getSelf());
		}).match(BuyList.class, bl -> {
			
		}).build();

	}
}
