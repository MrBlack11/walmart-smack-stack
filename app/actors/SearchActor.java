package actors;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.AbstractJavaRDDLike;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.datastax.spark.connector.japi.*;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import model.Product;
import model.ProductComparator;
import model.ResultSearch;
import model.SearchParam;

import akka.event.LoggingAdapter;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;

public class SearchActor extends AbstractActor {
	private JavaSparkContext context;
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public static Props getPros() {
		return Props.create(SearchActor.class);
	}

	@Override
	public void preStart() throws Exception {
		try {
			SparkConf conf = new SparkConf().setAppName("teste").setMaster("local");
			context = new JavaSparkContext(conf);
		} catch (NullPointerException e) {
			log.info("Ocorreu uma exceção do tipo NullPointer: " + e.getMessage());
		}
	}

	@Override
	public void postStop() throws Exception {
		try {
			context.close();
		} catch (NullPointerException e) {
			log.info("Recurso já foi desalocado" + e.getMessage());
		}
		
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(String.class, s -> {
			String all = (String) s;
			getSender().tell(selectUsers(), getSelf());
		}).match(SearchParam.class, sp -> {
			SearchParam searchParam = (SearchParam) sp;
			getSender().tell(searchProducts(searchParam), getSelf());
		}).build();
	}

	private String selectUsers() {
		JavaRDD<String> cassadraRdd = CassandraJavaUtil.javaFunctions(context)
				.cassandraTable("teste", "users", mapColumnTo(String.class)).select("fname");
		List<String> results = cassadraRdd.collect();

		StringBuilder sb = new StringBuilder();
		for (String str : results) {
			sb.append(str);
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * Search product that match with product name, category or price range
	 * 
	 * @param sp
	 * @return product list
	 */
	private ResultSearch searchProducts(SearchParam sp) {
		JavaRDD<Product> cassadraRdd = CassandraJavaUtil.javaFunctions(context)
				.cassandraTable("teste", "product", mapRowTo(Product.class))
				.select("id", "name", "price", "quantity", "type").repartition(context.defaultParallelism());

		List<Product> resultRdd = cassadraRdd.filter(p -> p.getName().equals(sp.getProductName())).takeOrdered(1, new ProductComparator());
//		Product resultRdd = cassadraRdd.filter(p -> p.getName().equals(sp.getProductName())).min(new ProductComparator());
		
		for(Product p : resultRdd) {
			System.out.println(p.getName());
			System.out.println(p.getPrice());
		}
			
		ResultSearch rs = new ResultSearch(resultRdd);

		rs.setRequestActor(sp.getRequestActor());
		rs.setMasterActor(sp.getMasterActor());

		return rs;
	}
}
