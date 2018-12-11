package actors;

import java.time.Duration;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.routing.RoundRobinPool;
import model.BuyList;
import model.ResultSearch;
import model.SearchParam;

public class StockActor extends AbstractActor {
	private ActorRef searchActor;

	private static SupervisorStrategy strategy = new OneForOneStrategy(10, Duration.ofMinutes(1),
			DeciderBuilder
				.match(NullPointerException.class, e -> SupervisorStrategy.restart())
				.match(IllegalArgumentException.class, e -> SupervisorStrategy.stop())
				.build());

	public static Props getProps() {
		return Props.create(StockActor.class, StockActor::new);
	}
	
	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}
	
	public StockActor() {
		this.searchActor = getContext().actorOf(SearchActor.getPros(), "search");
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SearchParam.class, sp -> {
			searchActor.tell(sp, self());
		}).match(ResultSearch.class, rs -> {
			rs.getMasterActor().tell(rs, self());
		}).build();
	}
}
