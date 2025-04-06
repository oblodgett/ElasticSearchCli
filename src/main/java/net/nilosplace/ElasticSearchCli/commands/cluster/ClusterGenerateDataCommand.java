package net.nilosplace.ElasticSearchCli.commands.cluster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

import com.github.javafaker.Faker;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import lombok.Data;
import net.nilosplace.process_display.ProcessDisplayHelper;

public class ClusterGenerateDataCommand extends ClusterCommand {

	private String indexName;
	private String threadCount;
	private String docAmount;
	private ElasticsearchClient client;
	private ProcessDisplayHelper ph = new ProcessDisplayHelper(10000);

	public ClusterGenerateDataCommand(String indexName, String docAmount, String threadCount) {
		this.indexName = indexName;
		this.docAmount = docAmount;
		this.threadCount = threadCount;
		this.client = configHelper.getEsClient();
	}

	@Override
	public void execute() {

		int docsPerThread = Integer.parseInt(docAmount) / Integer.parseInt(threadCount);

		ph.startProcess("Document Loading", Integer.parseInt(docAmount));
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File("/Volumes/BigDisk34/text_output2.txt")));
		} catch (Exception e) {
			return;
		}

		ArrayList<Thread> threads = new ArrayList<>();
		for (int i = 0; i < Integer.parseInt(threadCount); i++) {
			ThreadWorker worker = new ThreadWorker(reader, docsPerThread);
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
		private BufferedReader reader;

		public ThreadWorker(BufferedReader reader, int docsPerThread) {
			this.reader = reader;
			this.docsPerThread = docsPerThread;
		}

		@Override
		public void run() {
			while (docsPerThread > 0) {
				try {
					PersonDocument doc = new PersonDocument(reader, faker);
					client.index(i -> i.index(indexName).document(doc));
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
		private String document1;
		private String document2;
		private String document3;
		private String document4;
		private String document5;
		private PersonAddressDocument mailAddress;
		private PersonAddressDocument shippingAddress;
		private PersonAddressDocument billingAddress;

		public PersonDocument(BufferedReader reader, Faker faker) {
			this.id = faker.idNumber().valid();
			this.prefix = faker.name().prefix();
			this.firstname = faker.name().firstName();
			this.lastName = faker.name().lastName();
			this.displayName = faker.name().name();
			try {
				this.document1 = reader.readLine();
				this.document2 = reader.readLine();
				this.document3 = reader.readLine();
				this.document4 = reader.readLine();
				this.document5 = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
