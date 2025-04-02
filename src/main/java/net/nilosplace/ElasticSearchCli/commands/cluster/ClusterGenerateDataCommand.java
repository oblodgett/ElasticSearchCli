package net.nilosplace.ElasticSearchCli.commands.cluster;

import java.io.IOException;
import java.util.ArrayList;

import com.github.javafaker.Faker;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import lombok.Data;
import net.nilosplace.process_display.ProcessDisplayHelper;

public class ClusterGenerateDataCommand extends ClusterCommand {

	private String threadCount;
	private String docAmount;
	private ElasticsearchClient client;
	private ProcessDisplayHelper ph = new ProcessDisplayHelper(10000);

	public ClusterGenerateDataCommand(String docAmount, String threadCount) {
		this.docAmount = docAmount;
		this.threadCount = threadCount;
		this.client = configHelper.getEsClient();
	}

	@Override
	public void execute() {

		int docsPerThread = Integer.parseInt(docAmount) / Integer.parseInt(threadCount);

		ph.startProcess("Document Loading", Integer.parseInt(docAmount));

		ArrayList<Thread> threads = new ArrayList<>();
		for (int i = 0; i < Integer.parseInt(threadCount); i++) {
			ThreadWorker worker = new ThreadWorker(docsPerThread);
			threads.add(worker);
			worker.start();
		}

		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ph.finishProcess();

	}

	public class ThreadWorker extends Thread {
		private Faker faker = new Faker();
		private int docsPerThread;

		public ThreadWorker(int docsPerThread) {
			this.docsPerThread = docsPerThread;
		}

		@Override
		public void run() {
			while (docsPerThread > 0) {
				try {
					PersonDocument doc = new PersonDocument(faker);
					client.index(i -> i.index("test").document(doc));
					ph.progressProcess();
				} catch (ElasticsearchException | IOException e) {
					e.printStackTrace();
				}
				docsPerThread--;
			}
		}
	}

	@Data
	public class PersonDocument {
		private String id;
		private String prefix;
		private String firstname;
		private String lastName;
		private String displayName;
		private PersonAddressDocument mailAddress;
		private PersonAddressDocument shippingAddress;
		private PersonAddressDocument billingAddress;

		public PersonDocument(Faker faker) {
			this.id = faker.idNumber().valid();
			this.prefix = faker.name().prefix();
			this.firstname = faker.name().firstName();
			this.lastName = faker.name().lastName();
			this.displayName = faker.name().name();
			this.mailAddress = new PersonAddressDocument(faker);
			this.shippingAddress = new PersonAddressDocument(faker);
			this.billingAddress = new PersonAddressDocument(faker);
		}
	}

	@Data
	public class PersonAddressDocument {
		private String id;
		private String streetName;
		private String city;
		private String state;
		private String zip;
		private String lat;
		private String lon;

		public PersonAddressDocument(Faker faker) {
			this.id = faker.idNumber().valid();
			this.streetName = faker.address().streetName();
			this.city = faker.address().city();
			this.state = faker.address().state();
			this.zip = faker.address().zipCode();
			this.lat = faker.address().latitude();
			this.lon = faker.address().longitude();
		}
	}

}
