package model;

import akka.actor.ActorRef;

public class RefActors {

	private ActorRef masterActor;
	private ActorRef requestActor;

	public RefActors() {

	}

	public ActorRef getMasterActor() {
		return masterActor;
	}

	public void setMasterActor(ActorRef masterActor) {
		this.masterActor = masterActor;
	}

	public ActorRef getRequestActor() {
		return requestActor;
	}

	public void setRequestActor(ActorRef requestActor) {
		this.requestActor = requestActor;
	}

}
