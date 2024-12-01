Data extraction tool creating from two main submodules:
- [Crawler](https://github.com/Badjokke/JCrawlee/tree/master/src/main/java/crawlee/org/src/crawler)
- [REST ETL](https://github.com/Badjokke/JCrawlee/tree/master/src/main/java/crawlee/org/src/etl)

## Crawler
Crawler uses JSOUP to scrape webpages defined in JSON objects using xpath rules (jsoup's support for xpath is not ideal, therefore a design decision was made to extract values from tags in [util class](https://github.com/Badjokke/JCrawlee/blob/master/src/main/java/crawlee/org/src/crawler/util/JsoupXpathUtil.java).   
The parsing rules were created for my personal needs. Override the method from subclass if change is required.
To define more crawlers or change parsing rules you have to change [json file](https://github.com/Badjokke/JCrawlee/blob/master/src/main/resources/crawlee/org/resources/crawler_config.json).  
Where *worker_count* is the number of worker threads and *traverse_expression* key in *xpaths* object is reserved as a keyword and is used for navigation - this is the main difference from general concept of web crawler.  

## ETL
This submodule is not generalized and is created to fill one need, to scrape data from yit rest endpoint.  
For future plans I would like to create generalized REST scraper with similiar json setting as with *crawler*.  
Refer to [json file](https://github.com/Badjokke/JCrawlee/blob/master/src/main/resources/crawlee/org/resources/etl_config.json) for example.  
Very relevant keys are: *projection* and *useOnlyProjection*.  
Projection is very straight forward - for given *x* create a function *f* that returns *y*, i.e. f(x) = y where *x* is from endpoint domain and y is from user defined domain.  
For example *"ApartmentNumber": "apartment number"* x="ApartmentNumber*, y=*apartment number* means that any attributes from the original data source denoted by key *ApartmentNumber* will be transformed to *apartment number* after the transformation process.  
For more concrete example:
```javascript
{"ApartmentNumber": "1A"} ----**transformation_process**----> {"apartment number": "1A"}
```
If *useOnlyProjection* is set to true then only attributes defined in the *projection* object will be available after the *transformation_process*.  
If *useOnlyProjection* is false then projection will be used if defined to transform names and every extracted attribute will be available after transformation.

## How to run this thing
JDK 21 (17 is probably fine too).
Either download a jar file from [releases](https://github.com/Badjokke/JCrawlee/releases) and run the following command:
```
java -jar path/to/crawlee-0.0.1-SNAPSHOT.jar
```
Or go the good old fashioned way (if you have git installed and maven installed and visible at %PATH% for win32 or $PATH for *nix): 
```
git clone https://github.com/Badjokke/JCrawlee
cd JCrawlee
mvn clean package
java -jar target/crawlee-0.0.1-SNAPSHOT.jar
```
