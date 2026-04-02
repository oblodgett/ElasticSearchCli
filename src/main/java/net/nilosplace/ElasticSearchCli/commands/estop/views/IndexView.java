package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.io.IOException;

import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.screen.Screen;

import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;
import net.nilosplace.ElasticSearchCli.commands.estop.model.IndexInfo;

public class IndexView extends ViewBase {

	public IndexView(Screen screen, ClusterDataManager manager) {
		super(screen, manager);
		header = "Index View";
	}

	@Override
	public void draw(boolean clear) throws IOException {
		if (clear) {
			screen.clear();
		}

		int colCount = 6;
		int[] columns = new int[colCount];
		String[][] values = new String[colCount][manager.getIndicesTotal() + 1];

		values[0][0] = "Name";
		values[1][0] = "Docs";
		values[2][0] = "Size";
		values[3][0] = "Pri";
		values[4][0] = "Total";
		values[5][0] = "Segs";

		for (int i = 0; i < colCount; i++) {
			columns[i] = values[i][0].length();
		}
		int c = 0;
		for (IndexInfo indexInfo : manager.getIndexInfos()) {
			c++;
			values[0][c] = indexInfo.getName();
			values[1][c] = indexInfo.getDocCount() + "";
			values[2][c] = indexInfo.getSize() != null ? ClusterDataManager.formatBytes(indexInfo.getSize()) : "";
			values[3][c] = indexInfo.getPrimaryShardCount() + "";
			values[4][c] = indexInfo.getTotalShardCount() + "";
			values[5][c] = indexInfo.getTotalSegmentCount() + "";

			for (int i = 0; i < colCount; i++) {
				if (values[i][c].length() > columns[i]) {
					columns[i] = values[i][c].length();
				}
			}
		}

		for (int i = 0; i <= c; i++) {
			int colStart = 0;
			clearLine(i);
			for (int k = 0; k < columns.length; k++) {
				printText(colStart, i, values[k][i], ANSI.WHITE);
				colStart += (columns[k] + 2);
			}
		}

		screen.refresh();
	}

}
