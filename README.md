# Mini Search Engine

### Downloading the Wikipedia data

Source of data (~23 GB compressed):
```
https://dumps.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles.xml.bz2
```

Use WikiExtractor to generate subsets of the dataset, utilizing the ```--json``` flag: 
```
https://github.com/attardi/wikiextractor
```

Each file is JSON Lines (JSONL) format. An example line:
```json
{
  "id": "21721040",
  "revid": "1187654321",
  "url": "https://en.wikipedia.org/wiki/Java_(programming_language)",
  "title": "Java (programming language)",
  "text": "Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible.\n\nIt was originally developed by James Gosling at Sun Microsystems..."
}
```

