package actors;

import java.util.Arrays;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.Tuple2;

public class SparkActor extends AbstractActor {
	public static String PATH_FILE = "/home/mrblack/Documentos/";
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	private SparkConf conf;
	private JavaSparkContext context;
	
	public static Props getProps() {
		return Props.create(SparkActor.class);
	}
	
	@Override
	public void preStart() throws Exception {
		try {
			conf = new SparkConf().setAppName("teste").setMaster("local");
			context = new JavaSparkContext(conf);
		} catch (NullPointerException e) {
			log.info("Ocorreu uma exceção: " + e.getMessage());
		} 
	}

	@Override
	public void postStop() throws Exception {
		context.close();
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(String.class, path -> {
			log.info(path);
			getSender().tell(wordCount(path), getSelf());
		}).build();
	}

	private String wordCount(String path) {
		JavaPairRDD<String, Integer> counts = null;

		try {
			context = new JavaSparkContext(conf);
			JavaRDD<String> textFile = context.textFile(path);
			counts = (JavaPairRDD<String, Integer>) textFile.flatMap(s -> Arrays.asList(s.split(" ")).iterator())
					.mapToPair(word -> new Tuple2<>(word, 1)).reduceByKey((a, b) -> a + b);

			Map<String, Integer> result = counts.collectAsMap();

			StringBuilder resultMap = new StringBuilder();

			for (Map.Entry<String, Integer> entry : result.entrySet()) {
				resultMap.append(entry.getKey());
				resultMap.append(": ");
				resultMap.append(entry.getValue());
				resultMap.append("\n");
			}

			return resultMap.toString();

		} catch (Exception e) {
			throw new RuntimeException("Ocorreu uma exceção" + e.getMessage());
		} finally {
			if (context != null)
				context.close();
		}

	}
}
