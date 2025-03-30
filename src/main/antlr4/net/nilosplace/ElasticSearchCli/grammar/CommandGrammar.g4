grammar CommandGrammar;

options {
	language=Java;
}

command:
	(configCommand | indexCommand | repoCommand | snapshotCommand | aliasCommand | clusterCommand | quitCommand) EOF;

configCommand:
	CONFIG SET ARG ARG
	| CONFIG PRINT
	| CONFIG LOAD ARG
	| CONFIG SAVE ARG
	;

indexCommand:
	INDEX LIST
	| INDEX INFO ARG
	| INDEX SWITCHALIAS ARG ARG ARG
	| INDEX DELETE ARG
	;

repoCommand:
	REPO LIST
	| REPO DELETE ARG
	| REPO CREATE ARG ARG
	;

snapshotCommand:
	SNAPSHOT LIST
	| SNAPSHOT DELETE ARG
	| SNAPSHOT CREATE ARG ARG
	;

aliasCommand:
	ALIAS LIST ARG
	| ALIAS CREATE ARG ARG
	| ALIAS REMOVE ARG ARG
	;

clusterCommand:
	CLUSTER INFO
	| CLUSTER NODES
	| CLUSTER CONFIG GET ARG
	| CLUSTER CONFIG SET ARG ARG
	;

quitCommand:
	QUIT
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

ARG:		[A-Za-z0-9_]+;

WS: [ \t\r\n] -> skip;
EOL: '\r'? '\n' -> skip;




