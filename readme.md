# Crawler

Web cralwer for GoCardless


## Usage
- Language used for coding- Java 1.8
- Build tool: Gradle
- Dependency: Java 1.8, Gradle 2.7+
- The output is printed to STDOUT and is also written to result.txt file.
```bash
$ ./gradlew build
$ ./gradlew run -PappArgs="['https://gocardless.com/', 60]"
(The first argument to run is the "domain name-proper url" and the second (here 60) is the "max time (in seconds) " the crawler should run. Since crawling takes lot of time we might want to run crawler only for some time)
$ ./gradlew test (to run test cases)
```

## Specs

- simple non-concurrent crawler
- should only parse a single domain, ie. ignore outgoing links
- for each page display which static assets each page depends on: images, css, js, video, etc.
- prevent infinite loops, make sure you don't crawl pages you already crawled

## Features, Issues and TODOs
1. `Extracting assets from a page` - I am using here DOM implementation (Jsoup) to fetch all assets. .I'm have looked for `<script>`, `<link> (ignoring rel-'alternate' and rel-'cranonical')` and `<img>` tags.

2. `Extracting links from a page` - I look for `<a>` tags and extracted their `href` value.

3. `Test suite` - test suite exists in the `src/test` folder, it covers the core functionality and
the most of the methods in the code base. Used `WireMock` for mocking external HTTP resources. `Mockito with Junit` for interface dependency. 
`code coverage`: 69% lines. For most part of the code coverage is > 80% lines. 

4. `State of the crawler` - Currently the state of crawler is in memory so if the crawler stops in between we loose the all work done by it. To implement this in production we should store the state in some persistence memory with replication. State includes pages crawled, urls to be crawled and any other data required to restart crawler.

5. `Error handling` - Errors produced by the parser are logged but muffled, ie. no bespoke action is taken. There is 10secs timeout configured to get the page via http call. Have catched exception at proper places to let crawler work.
In production, error aggregation and monitoring are crucial for extending the
effictiveness of the crawler. 

## Architecture

```
               +--------+                  +-----------------------+
               | Start  |----------------> | Writer to file/stdout | 
               +--------+                  +------------------------
                 ^ ^ | |
   Pages crawled | | | | Domain & time to crawl
                 | | v v
               +---------+                 +----------------+
          +----| Crawler |<--------------->| Page-extractor |
          |    |         |                 +----------------+
          |    |         |<--------------->+----------------+
          |    |         |                 |SiteMapXmlParser|
          |    |         |                 +----------------+
          |    |         |<--------------->+----------------+
     push |    +---------+                 |  UrlCrawlRule  |                 
     urls |      ^                         +----------------+ 
          |      | fetch
          |      | a url
          |      |
          v      |
      +------------+         
      | PageRequest|         
      |   Queue    |
      +------------+        
                     
```

## Components

- Start
    - start the cralwer with the domain name and time to crawl.
    - when the crawler finishes, print the list of pages.
- PageRequestQueue
    - It contains all the urls which are to processed by the crawler.
    - No duplicate url is present in the queue.
- PageExtractor
    - Given a url it fetches the page by making http request.
    - Using Jsoup (DOM parser) it extracts all the assets and links from the page.
- SiteMapXmlParser
    - it process sitemap.xml for the given url and returns list of url available for parsing.
- UrlCrawlRule
    - It fetches robots.txt file for the give url and create rules for the same
    - Each url should satisfy all the rules inorder to be processed by the crawler
- Crawler
    - at initialization the crawler gets links from SiteMapXmlParser, then push this links to queue and        create instance of UrlCrawlrule for the give domain name
    - retrieve a url from the queue
    - checks if the url is not already processed and urlcrawlRule allowes the url
    - calls pageExtractor module to retrieve the page object (contains assets links and list of links)
    - add the page to the list of pages (final result)
    - for all the links in the page,check for the rules and then push to the queue
    - stop when the queue is empty or the crawler is finish running for the given seconds

## Performance
- Tested crawler on https://gocardless.com/. It crawled 275 pages in 5 minutes
- The crawler is single threaded. The time is also spent on waiting for certain pages, timeout being 10secs.

## Data Structures
- Page - contains info about a page
```
{
    url: this page url,
    assets: list of strings
    children: list of links on this page
}
```

- PageRequestQueue - a simple queue containing un-parsed urls. It is backed by hashSet to contain unique urls only
```
[url1, url2]
```

