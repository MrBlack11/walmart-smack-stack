package controllers;

import static akka.pattern.Patterns.ask;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import actors.MasterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import model.SearchParam;
import play.libs.Json;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import views.html.*;
import views.html.helper.form;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
@Singleton
public class HomeController extends Controller {
	final ActorRef masterActor;
	public static final Timeout TIME = Timeout.create(Duration.ofSeconds(100));

	@Inject
	public HomeController(ActorSystem system) {
		this.masterActor = system.actorOf(MasterActor.getProps(), "masterActor");
	}

	public Result index() {
		return ok("Welcome to walmart");
	}

	/**
	 * Search by name
	 * 
	 * @param productName
	 * @return product list
	 */
	public CompletionStage<Result> search(String productName) {
		SearchParam searchParam = new SearchParam(productName);

		// mudar para list json de produtos
		return FutureConverters.toJava(ask(masterActor, searchParam, TIME))
				.thenApply(response -> ok(Json.toJson(response)));
	}
}
