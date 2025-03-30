grammar CommandGrammar;

options {
	language=Java;
}

@header {
import net.nilosplace.ElasticSearchCli.commands.*;
}

input returns[Command command]:
	config { $command = $config.command; }
	| index
	| repo
	| snapshot
	| alias
	| cluster
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

cluster:
	CLUSTER INFO
	| CLUSTER NODES
	| CLUSTER CONFIG GET ARG
	| CLUSTER CONFIG SET ARG ARG
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

ARG:		[A-Za-z0-9_.]+;

WS: [ \t\r\n] -> skip;
EOL: '\r'? '\n' -> skip;
