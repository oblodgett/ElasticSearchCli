package net.nilosplace.ElasticSearchCli.elastic;

import java.io.IOException;

import si.mazi.rescu.RestProxyFactory;

import net.nilosplace.ElasticSearchCli.elastic.adapter.Es7Adapter;
import net.nilosplace.ElasticSearchCli.elastic.adapter.Es8Adapter;
import net.nilosplace.ElasticSearchCli.elastic.adapter.Os2Adapter;
import net.nilosplace.ElasticSearchCli.elastic.api.ElasticSearchApi;
import net.nilosplace.ElasticSearchCli.elastic.api.OpenSearchApi;
import net.nilosplace.ElasticSearchCli.elastic.model.VersionInfo;

public class ClusterFacadeFactory {

	public static ClusterFacade create(String baseUrl) throws IOException {
		ElasticSearchApi api = RestProxyFactory.createProxy(ElasticSearchApi.class, baseUrl);
		VersionInfo versionInfo = api.getVersion();

		String distribution = null;
		int majorVersion = 0;

		if (versionInfo.getVersion() != null) {
			distribution = versionInfo.getVersion().getDistribution();
			String number = versionInfo.getVersion().getNumber();
			if (number != null && !number.isEmpty()) {
				majorVersion = Integer.parseInt(number.split("\\.")[0]);
			}
		}

		if ("opensearch".equalsIgnoreCase(distribution)) {
			OpenSearchApi osApi = RestProxyFactory.createProxy(OpenSearchApi.class, baseUrl);
			return new Os2Adapter(osApi);
		} else if (majorVersion <= 7) {
			return new Es7Adapter(api);
		} else {
			return new Es8Adapter(api);
		}
	}
}
