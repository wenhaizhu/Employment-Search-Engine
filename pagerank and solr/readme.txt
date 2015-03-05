readme:

1. folder structure
|-Demonstrate Program/: the program to demonstrate the answer to the four questions
|-LinedBasedRank/: here is the code to compute the linked based rank algorithm
|-OODT Policy/: OODT workflow and filemanager policy
|-Solr Scheme/
|-readme.txt
|-report.pdf

2. OODT

How to configure OODT
(1) Replace all the xml files under folder policy that have been changed:
	1)       Crawler
		a.       action-bean.xml
		b.      cmd-line-option.xml
	2)      Workflow
		a.       event.xml
		b.      myWorkflow.workflow.xml
		c.       tasks.xml
		d.      workflow-lifecycle.xml
		e.       workflow-instance-met.xml
		f.       add pge-configs folder
		g.      add tikaextractor folder that include tikaextractor.config
(2) Configure environment variable and change path in configuration files
	1)      Add environment variable
		a.       OODT_HOME
		b.      WORKFLOW_HOME
		c.       FILEMGR_HOME
		d.      CRAWLER_HOME
	2)      Change Path
		a.       In tasks.xml, change PGETask_ConfigFilePath to your PGEConfig.xml path
		b.      In tasks.xml, change PCS_ActionRepoFile to your crawler-config.xml path
		c.       In PGEConfig.xml, change the first cd command path to the JSON file path
		d.      In PGEConfig.xml, change poster path to your Etllib poster location
(3) Include fileconcatenator-pge-1.0.jar in the workflow folder lib.
 
How to run
(1) Locate to the crawler bin folder
(2) Run command:
	./crawler_launcher \
	--operation --launchMetCrawler \
	--filemgrUrl http://localhost:9000 \
	--clientTransferer org.apache.oodt.cas.filemgr.datatransfer.LocalDataTransferFactory \
	--productPath /Volumes/STORAGE/jsons \
	--workflowMgrUrl http://localhost:9001 \
	-ais TriggerPostIngestWorkflow \
	--metExtractor org.apache.oodt.cas.metadata.extractors.TikaCmdLineMetExtractor \
	--metExtractorConfig /Volumes/STORAGE/oodt/tikaextractor/tikaextractor.config
 
Change the productPath to the JSON files path
Change the metExtractorConfig path to the tikaextractor.config path
Note: or you can remove the \ and put the command as a whole line to make sure it runs correctly.


3. Linked based rank alogrithm:

how to run:
You can directly import the java project under “LinedBasedRank” folder. Before you run the program, please make sure it satisfies the following requirements:
1. Put all the date into "/path/to/data/" folder, and make sure the file are named from 0 to some number, like 0.json, 1.json, 2.json .... 100.json.
2. Make sure the "fileSize" is set to a reasonable value.
3. Change the value of  "fileFolder" of buildHashMap and  writeIntoFile function  into "/path/to/data/"
Then run the program.
After finished, you can see there are 3 new fields in the json files.


4. Demonstrate program:
The demonstare program is written in python. If you want to run this file, please open it and change "firsturl", "secondurl", "thirdurl", and "foururl" to the right Solr query url. Then in the command line, run “python demonstrate.py”

5. External jar:
json_simple-1.1.jar: this jar file has already in the java project. No need to import it again.
