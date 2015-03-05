Note: if the format of the following contents is not readable, please use other editor(like sublime), thanks.

Java code:
(When you run on huge data, please set the memory to maximum)

	
	1.File  structure:

	
	javacode/
		
		src/
			
			TSV2XHTMLMain.java
			
			XHTML2JSONMain.java
			
			CrawlerMain.java
			
			DeduplicateMain.java
			
			TSVParser.java
			
			JSONTableContentHandler.java
			
			XHTMLParser.java
			
			SimHash.java
		
		lib/

			commons-lang3-3.3.2.jar

			json-simple-1.1.1.jar



	2.How to run

	First, import all java filse in javacode/src

	Second, import all external jars in javacode/lib

	For 3.1 one tsv to one xhtml:

		(1)put a tsv file (original.tsv) into /some/path/original.tsv

		(2)in Eclipse, open TSV2XHTMLMain.java, set 2 arguments: /some/path/original.tsv /some/path/result.xhtml

		(3)after a few seconds, go to /some/path/ to check result.xhtml

	For 3.2 one xhtml to n json:

		(1)put a xhtml file (original.xhtml) into /some/path/original.xhtml

		(2)in Eclipse, open XHTML2JSONMain.java, set 2 arguments: /some/path/original.xhtml /some/path/json/

		(3)after a few seconds, go to /some/path/json/, all generated json files are here

	For 3.3 a  simple  crawler from n tsv files to m json files:

		(1)put all tsv filse into /some/path/tsv/

		(2)in Eclipse, open CrawlerMain.java, set 2 arguments: /some/path/tsv/ /some/path/json/

		(3)after a few seconds, go to /some/path/json/, all generated json files are here

	For 3.5 deduplication:

		(1)put all tsv filse into /some/path/tsv/

		(2)in Eclipse, open DeduplicateMain.java, set 1 arguments: /some/path/tsv/

		(3)we can see the results in the console


Python code:


	1.File structure:

	pythoncode/

		data/ -----put tsv file into it

		json/ -----put json files into it

		tsv2json/ -----the code for etllib to convert tsv to single json

			tsv2json.py

			etl/ -----in case of couldn't import etl

		intermediate/ -----for intermediate file (the output from etllib:tsvtojson)

		encoding/ -----for encoding file

			encoding.txt

		header/ -----for column header file

			header.txt

		hashtype.py -----for simhash

		simhash.py -----for simhash

		main.py

	2.How to run:

	For 3.4 etllib problem:
 
		(1)First put some tsv files into "pythoncode/data/"

		(2)cd pythoncode/tsv2json

		(3)python tsv2json.py

		(4)After finished, the json files will be generated in "pythoncode/tsv2json"

	For 3.5 deduplicate problem:

		(1)First put some single json files into "pythoncode/json"

		(2)cd pythoncode/

		(3)python main.py

		(4)waiting for the result




External jars: (use of these jars please look at "java code")


commons-lang3-3.3.2.jar

json-simple-1.1.1.jar
