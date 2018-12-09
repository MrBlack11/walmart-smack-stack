package controllers;

import static akka.pattern.Patterns.ask;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import actors.MasterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import model.SearchParam;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;

public class SearchController extends Controller {
	private ActorRef masterActor;
	private final Timeout t = Timeout.create(Duration.ofSeconds(20));

	@Inject
	public SearchController(ActorSystem _system) {
		this.masterActor = _system.actorOf(MasterActor.getProps());
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
		return FutureConverters.toJava(ask(masterActor, searchParam, t)).thenApply(response -> ok((String) response));
	}

	/**
	 * Search by category and price range
	 * 
	 * @param category
	 * @param minPrice
	 * @param maxPrice
	 * @return product list
	 */
	public CompletionStage<Result> search(String category, Double minPrice, Double maxPrice) {
		SearchParam searchParam = new SearchParam(category, minPrice, maxPrice);

		// mudar para list json de produtos
		return FutureConverters.toJava(ask(masterActor, searchParam, t)).thenApply(response -> ok((String) response));
	}

	/**
	 * Search by product name, category and price range
	 * @param productName
	 * @param category
	 * @param minPrice
	 * @param maxPrice
	 * @return
	 */
	public CompletionStage<Result> search(String productName, String category, Double minPrice, Double maxPrice) {
		SearchParam searchParam = new SearchParam(productName, category, minPrice, maxPrice);

		// mudar para list json de produtos
		return FutureConverters.toJava(ask(masterActor, searchParam, t)).thenApply(response -> ok((String) response));
	}
}
