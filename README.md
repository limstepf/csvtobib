# Converts (IEEE) CSV to BibTeX

`csvtobib` is a command line tool to specifically convert CSV files served by the [IEEE Xplore Digital Library](http://ieeexplore.ieee.org/) upon downloading the citations of a search result (max. 2000 results, CSV only) to BibTeX.

Entries without BibTeX `author` field are pruned/rejected, but still stored in a second BibTeX database (e.g. for manual inspection).

## Usage

```
$ java -jar csvtobib-1.0.0-SNAPSHOT.jar <options>

-f, --file <file>
    The (IEEE) CSV file to convert to BibTeX.

-d, --directory <file>
    The output directory where to put the BibTeX file. (OPTIONAL)

-u, --usage
    Print the usage of this program.
```


## Example

```bash
#!/bin/bash
app=./csvtobib.git/target/csvtobib-1.0.0-SNAPSHOT.jar
file=./export2017.03.30-17.36.05.csv

java -jar ${app} --file ${file} 
```


## Related Projects

* [bibsani](https://github.com/limstepf/bibsani): Bib(TeX) Sani(tizer)
* [pdfdbscrap](https://github.com/limstepf/pdfdbscrap): PDF Database Scrap(er)
* [pdfhiscore](https://github.com/limstepf/pdfhiscore): PDF Hi(stogram) Score
