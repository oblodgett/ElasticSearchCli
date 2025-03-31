package net.nilosplace.ElasticSearchCli.utils;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

public class ElasticSearchConnection {

	private ElasticsearchClient esClient;

	public ElasticSearchConnection(String host, int port) {
		HttpHost[] hosts;

		if(host.contains(",")) {
			String[] nodes = host.split(",");
			hosts = new HttpHost[nodes.length];
			for(int i = 0; i < nodes.length; i++) {
				hosts[i] = HttpHost.create(nodes[i]);
			}
		} else {
			hosts = new HttpHost[1];
			hosts[0] = HttpHost.create(host + ":" + port);
		}

		RestClient restClient = RestClient.builder(hosts).build();

		ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

		esClient = new ElasticsearchClient(transport);
	}

	public ElasticsearchClient getClient() {
		return esClient;
	}

	public void close() {
		try {
			esClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
