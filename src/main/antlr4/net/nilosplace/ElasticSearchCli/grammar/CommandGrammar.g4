grammar CommandGrammar;

options {
	language=Java;
}

@header {
import net.nilosplace.ElasticSearchCli.commands.*;
import net.nilosplace.ElasticSearchCli.commands.cluster.*;
import net.nilosplace.ElasticSearchCli.commands.config.*;
import net.nilosplace.ElasticSearchCli.commands.estop.*;
import net.nilosplace.ElasticSearchCli.commands.quit.*;
}

input returns[Command command]:
	config { $command = $config.command; }
	| index
	| repo
	| estop { $command = $estop.command; }
	| snapshot
	| alias
	| cluster { $command = $cluster.command; }
	| quit { $command = $quit.command; }
	EOF;

config returns[ConfigCommand command]:
	CONFIG SET name=ARG value=ARG { $command = new ConfigSetCommand($name.text, $value.text); }
	| CONFIG GET name=ARG { $command = new ConfigGetCommand($name.text); }
	| CONFIG PRINT { $command = new ConfigPrintCommand(); }
	| CONFIG LOAD filename=ARG { $command = new ConfigLoadCommand($filename.text); }
	| CONFIG SAVE filename=ARG { $command = new ConfigSaveCommand($filename.text); }
	;

index:
	INDEX LIST
	| INDEX INFO ARG
	| INDEX SWITCHALIAS ARG ARG ARG
	| INDEX DELETE ARG
	;

estop returns[EsTopCommand command]:
	ESTOP { $command = new EsTopCommand(); }
	;

repo:
	REPO LIST
	| REPO DELETE ARG
	| REPO CREATE ARG ARG
	;

snapshot:
	SNAPSHOT LIST
	| SNAPSHOT DELETE ARG
	| SNAPSHOT CREATE ARG ARG
	;

alias:
	ALIAS LIST ARG
	| ALIAS CREATE ARG ARG
	| ALIAS REMOVE ARG ARG
	;

cluster returns[ClusterCommand command]:
	CLUSTER INFO { $command = new ClusterInfoCommand(); }
	| CLUSTER NODES { $command = new ClusterNodesCommand(); }
	| CLUSTER CONFIG GET configName=ARG { $command = new ClusterConfigGetCommand($configName.text); }
	| CLUSTER CONFIG SET configName=ARG configValue=ARG { $command = new ClusterConfigSetCommand($configName.text, $configValue.text); }
	;

quit returns[QuitCommand command]:
	(QUIT | EXIT) { $command = new QuitCommand(); }
	;

CONFIG: 'config';
INDEX: 'index';
REPO: 'repo';
SNAPSHOT: 'snapshot';
LIST: 'list';
DELETE: 'delete';
CREATE: 'create';
REMOVE: 'remove';
SET: 'set';
GET: 'get';
ESTOP: 'estop';
CLUSTER: 'cluster';
INFO: 'info';
NODES: 'nodes';
SWITCHALIAS: 'switchalias';
PRINT: 'print';
ALIAS: 'alias';
LOAD: 'load';
SAVE: 'save';
QUIT: 'quit';
EXIT: 'exit';

ARG:		[A-Za-z0-9_.:,]+;

WS: [ \t\r\n] -> skip;
EOL: '\r'? '\n' -> skip;
