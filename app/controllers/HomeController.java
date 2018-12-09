package controllers;

import scala.compat.java8.FutureConverters;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import actors.InsertActor;
import actors.MasterActor;
import actors.SearchActor;
import actors.SparkActor;
import akka.actor.*;
import akka.util.Timeout;
import model.SearchParam;
import model.User;
import play.mvc.*;

import views.html.*;
import views.html.helper.form;

import static akka.pattern.Patterns.ask;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {
	private ActorRef masterActor;

	private ActorRef searchActor;
	private ActorRef insertActor;
	private ActorRef sparkActor;

	Timeout t = Timeout.create(Duration.ofSeconds(20));

	@Inject
	public HomeController(ActorSystem _system) {
		this.masterActor = _system.actorOf(MasterActor.getProps(), "masterActor");
		
		this.searchActor = _system.actorOf(SearchActor.getPros());
		this.insertActor = _system.actorOf(InsertActor.getPros());
		this.sparkActor = _system.actorOf(SparkActor.getProps());
	}

	public Result index() {
		return ok("Welcome to walmart");
	}

	public CompletionStage<Result> wordcount() {
		String texto = "/home/mrblack/Documentos/words.txt";
		return FutureConverters.toJava(ask(sparkActor, texto, t)).thenApply(response -> ok((String) response));
	}

//	public CompletionStage<Result> search() {
//		return FutureConverters.toJava(ask(searchActor, "Iniciar", t)).thenApply(response -> ok((String) response));
//	}

	public CompletionStage<Result> insert(Integer id, String fname, String lname) {
		User user = new User(id, fname, lname);
		insertActor.tell(user, insertActor);

		return FutureConverters.toJava(ask(insertActor, user, t)).thenApply(response -> ok((String) response));
	}
	
	public CompletionStage<Result> search(String productName) {
		SearchParam searchParam = new SearchParam(productName);
		
		// mudar para list json de produtos
		return FutureConverters.toJava(ask(masterActor, searchParam, t)).thenApply(response -> ok((String) response));
	}
	
}
