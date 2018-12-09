package actors;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.datastax.spark.connector.japi.CassandraJavaUtil;

import akka.actor.AbstractActor;
import akka.actor.Props;
import model.User;
	
public class InsertActor extends AbstractActor {
	private SparkConf conf;
	private JavaSparkContext context;

	public static Props getPros() {
		return Props.create(InsertActor.class);
	}

	@Override
	public void preStart() throws Exception {
		try {
			conf = new SparkConf().setAppName("teste").setMaster("local");
			context = new JavaSparkContext(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void postStop() throws Exception {
		context.close();
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(User.class, u -> {
			getSender().tell(insertUser(u), getSelf());
		}).build();
	}

	private String insertUser(User user) {
		List<User> users = new ArrayList<>();
		users.add(user);

		JavaRDD<User> rdd = context.parallelize(users);
		CassandraJavaUtil.javaFunctions(rdd).writerBuilder("teste", "users", mapToRow(User.class)).saveToCassandra();
		
		return "Inserindo";
	}
}
