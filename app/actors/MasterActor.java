package actors;

import javax.inject.Inject;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import model.BuyList;
import model.ResultSearch;
import model.SearchParam;

public class MasterActor extends AbstractActor {
	private ActorRef stockActor;

	public static Props getProps() {
		return Props.create(MasterActor.class);
	}

	public MasterActor() {
		this.stockActor = getContext().actorOf(StockActor.getProps());
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SearchParam.class, sp -> {
			sp.setRequestActor(getSender());
			sp.setMasterActor(getSelf());
			stockActor.tell(sp, getSelf());

		}).match(ResultSearch.class, rs -> {
			rs.getRequestActor().tell(rs.getResultProducs(), getSelf());

		}).match(BuyList.class, bl -> {
			bl.setRequestActor(getSender());
			bl.setMasterActor(getSelf());
			stockActor.tell(bl, getSelf());

		}).build();
	}
}
