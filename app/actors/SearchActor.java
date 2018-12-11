package actors;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.datastax.spark.connector.japi.*;

import akka.actor.AbstractActor;
import akka.actor.Props;
import model.Product;
import model.ProductComparator;
import model.ResultSearch;
import model.SearchParam;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;

public class SearchActor extends AbstractActor {
	private JavaSparkContext context;
	private SparkConf conf;

	public static Props getPros() {
		return Props.create(SearchActor.class, SearchActor::new);
	}

	@Override
	public void preStart() throws Exception {
		conf = new SparkConf().setAppName("walmart").setMaster("local");
		context = new JavaSparkContext(conf);
	}

	@Override
	public void postStop() throws Exception {
		if (context != null) {
			context.close();
		}
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(Exception.class, ex -> {
			throw ex;
		}).match(SearchParam.class, sp -> {
			SearchParam searchParam = (SearchParam) sp;
			sender().tell(searchProducts(searchParam), self());
		}).build();
	}

	/**
	 * Search product that match with product name, category or price range
	 * 
	 * @param sp
	 * @return product list
	 */
	private ResultSearch searchProducts(SearchParam sp) {
		long startSearch = System.currentTimeMillis();
		JavaRDD<Product> cassadraRdd = CassandraJavaUtil.javaFunctions(context)
				.cassandraTable("teste", "product", mapRowTo(Product.class))
				.select("id", "name", "price", "quantity", "type");

		long end = System.currentTimeMillis() - startSearch;
		System.out.println("Tempo de busca: " + end + "ms");

		long startfilter = System.currentTimeMillis();
		List<Product> resultRdd = cassadraRdd.filter(p -> p.getName().contains(sp.getProductName())).takeOrdered(10,
				new ProductComparator());

		long endFilter = System.currentTimeMillis() - startfilter;
		System.out.println("Tempo de filtragem: " + endFilter + "ms");

		ResultSearch rs = new ResultSearch();
		rs.setResultProducts(resultRdd);
		rs.setMasterActor(sp.getMasterActor());
		rs.setRequestActor(sp.getRequestActor());
		
		return rs;
	}
}
