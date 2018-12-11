package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import model.BuyList;
import model.ResultSearch;
import model.SearchParam;

public class MasterActor extends AbstractActor {
	private ActorRef stockActor;

	public static Props getProps() {
		return Props.create(MasterActor.class, MasterActor::new);
	}

	public MasterActor() {
		this.stockActor = getContext().actorOf(Props.create(StockActor.class), "stock");
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SearchParam.class, sp -> {
			sp.setRequestActor(getSender());
			sp.setMasterActor(getSelf());
			stockActor.tell(sp, self());

		}).match(ResultSearch.class, rs -> {
			rs.getRequestActor().tell(rs.getResultProducts(), self());

		}).match(BuyList.class, bl -> {
			bl.setRequestActor(getSender());
			bl.setMasterActor(getSelf());
			stockActor.tell(bl, getSelf());

		}).build();
	}
}
