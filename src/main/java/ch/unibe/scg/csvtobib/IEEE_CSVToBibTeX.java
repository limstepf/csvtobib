package ch.unibe.scg.csvtobib;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.csv.CSVRecord;
import org.jbibtex.BibTeXEntry;

/**
 * IEEE CSV to BibTeX. "IEEE CSV" refers to the CSV file offered by
 * ieeexplore.ieee.org (upon clicking the "Download Citations" link) containing
 * your search result (2000 results max.).
 */
public enum IEEE_CSVToBibTeX implements CSVToBibTeX {

	ABSTRACT() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					return getIfSet("Abstract", record);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return KEY_ABSTRACT;
				}
			},
	AUTHOR() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					return getIfSet("Authors", record);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_AUTHOR;
				}
			},
	BOOKTITLE() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					if (BibTeXEntry.TYPE_INBOOK.equals(type)
					|| BibTeXEntry.TYPE_INCOLLECTION.equals(type)
					|| BibTeXEntry.TYPE_INPROCEEDINGS.equals(type)) {
						return getIfSet("Publication Title", record);
					}
					return "";
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_BOOKTITLE;
				}
			},
	DOI() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					return getIfSet("DOI", record);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_DOI;
				}
			},
	ISSN() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					if (record.isSet("ISSN")) {
						final String[] issn = record.get("ISSN").split(";");
						if (issn.length > 0) {
							return issn[0];
						}
					}
					return "";
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return KEY_ISSN;
				}
			},
	JOURNAL() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					if (BibTeXEntry.TYPE_ARTICLE.equals(type)) {
						return getIfSet("Publication Title", record);
					}
					return "";
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_JOURNAL;
				}
			},
	KEYWORDS() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					// we don't care about duplicates (that's also what you're
					// served by IEEE in the BiBTeX file of a single paper)
					final List<String> keywords = new ArrayList<>();
					final String[] keys = new String[]{
						"Author Keywords",
						"IEEE Terms",
						"INSPEC Controlled Terms",
						"INSPEC Non-Controlled Terms",
						"MeSH Terms"
					};
					for (String key : keys) {
						final String terms = getIfSet(key, record);
						if (!terms.isEmpty()) {
							keywords.add(terms);
						}
					}
					return String.join(";", keywords);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return KEY_KEYWORDS;
				}
			},
	MONTH() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					if (record.isSet("Issue Date")) {
						final String date = record.get("Issue Date").toLowerCase();
						// not very efficient, but eh... we're looking for the
						// first/earliest month mentioned (sometimes there is a range given)
						if (date.contains("jan")) {
							return "jan";
						}
						if (date.contains("feb")) {
							return "feb";
						}
						if (date.contains("mar")) {
							return "mar";
						}
						if (date.contains("apr")) {
							return "apr";
						}
						if (date.contains("may")) {
							return "may";
						}
						if (date.contains("jun")) {
							return "jun";
						}
						if (date.contains("jul")) {
							return "jul";
						}
						if (date.contains("aug")) {
							return "aug";
						}
						if (date.contains("sep")) {
							return "sep";
						}
						if (date.contains("oct")) {
							return "oct";
						}
						if (date.contains("nov")) {
							return "nov";
						}
						if (date.contains("dez")) {
							return "dez";
						}
					}
					return "";
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_MONTH;
				}
			},
	NUMBER() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					return getIfSet("Issue", record);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_NUMBER;
				}
			},
	PAGES() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					final String start = getIfSet("Start Page", record);
					final String end = getIfSet("End Page", record);
					if (end.isEmpty()) {
						return start;
					}
					return String.format("%s--%s", start, end);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_PAGES;
				}
			},
	PUBLISHER() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					return getIfSet("Publisher", record);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_PUBLISHER;
				}
			},
	URL() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					return getIfSet("PDF Link", record);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_URL;
				}
			},
	TITLE() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					return getIfSet("Document Title", record);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_TITLE;
				}
			},
	VOLUME() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					return getIfSet("Volume", record);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_VOLUME;
				}
			},
	YEAR() {
				@Override
				public String extract(org.jbibtex.Key type, CSVRecord record) {
					return getIfSet("Year", record);
				}

				@Override
				public org.jbibtex.Key getBibTeXField() {
					return BibTeXEntry.KEY_YEAR;
				}
			};

	private static final Pattern PATTERN_ARNUMBER = Pattern.compile("arnumber=([0-9]+)(.*)");
	private static final Pattern PATTERN_FILENAME = Pattern.compile("fileName=([0-9]+)\\.(.*)");
	/*
	 * Ugh.. here we go. It could have been all so nice and comfy, doing this with
	 * Enums, yet here we go, introducing state. My bad, I didn't really anticipate
	 * this. So before implementing another CSVToBibTeX thing, maybe refactor this
	 * into a proper class. Because now we basically ended up with a stupid
	 * Singleton. For no good reason at all. Congratulations. :|
	 */
	private static int autoid = 0;
	private static int rejected = 0;

	IEEE_CSVToBibTeX() {
	}

	private static String getNextId() {
		return String.format("ieee-autogen-%d", ++autoid);
	}

	@Override
	public int getNumAutoGeneratedKeys() {
		return autoid;
	}

	@Override
	public boolean isValidEntry(BibTeXEntry entry) {
		/*
		 * reject entries without an author. This catches entries with titles
		 * such as:

		 * "[Copyright notice]", "[Front cover]", "[Publishers´ information]",
		 * "Program Committee [...]", "Steering Committee", "[Title page]",
		 * "[Title page i]", "Author index", "Additional Reviewers", "Table of contents",
		 * "Message from the Chairs", "Message from the General Chair and Program Chairs",
		 * "Proceedings. [...]" (title pages)

		 * ...and so on. The false-positive rejection rate is next to zero.
		 */
		final org.jbibtex.Value author = entry.getField(BibTeXEntry.KEY_AUTHOR);
		if (author == null) {
			return reject();
		}

		return true;
	}

	private boolean reject() {
		rejected++;
		return false;
	}

	@Override
	public int getNumInvalidEntries() {
		return rejected;
	}

	@Override
	public String getBibTeXKey(org.jbibtex.Key type, CSVRecord record) {
		final String url = URL.extract(type, record);
		/*
		 * try to catch the article number out of url's like:
		 * http://ieeexplore.ieee.org/stamp/stamp.jsp?arnumber=6122637
		 */
		Matcher m = PATTERN_ARNUMBER.matcher(url);
		if (m.find()) {
			return m.group(1);
		}

		/*
		 * try to catch the fileName id out of url's like:
		 * http://ieeexplore.ieee.org/xpl/ebooks/bookPdfWithBanner.jsp?fileName=6285455.pdf&bkn=6267391&pdfType=chapter
		 */
		m = PATTERN_FILENAME.matcher(url);
		if (m.find()) {
			return m.group(1);
		}

		/*
		 * the upper methods return the same keys returned by IEEE if BibTeX
		 * entries are exported manually. What follows are fallback methods...
		 */
		final String issn = ISSN.extract(type, record);
		if (!issn.isEmpty()) {
			return issn;
		}

		final String doi = DOI.extract(type, record);
		if (!doi.isEmpty()) {
			return doi;
		}

		return getNextId();
	}

	@Override
	public org.jbibtex.Key getBibTeXEntryType(CSVRecord record) {
		final String docid = getIfSet("Document Identifier", record).toLowerCase();

		/*
		 * catch:
		 * [IEEE|IET|VDE|...] Conference Publications
		 */
		if (docid.contains("conference")) {
			return BibTeXEntry.TYPE_INPROCEEDINGS;
		}

		/*
		 * catch:
		 * MIT Press eBook Chapters
		 * Wiley-IEEE Press eBook Chapters
		 * IEEE USA Books & eBooks
		 */
		if (docid.contains("ebook")) {
			final String title = getIfSet("Document Title", record);
			if (title.isEmpty()) {
				return BibTeXEntry.TYPE_INBOOK;
			}
			return BibTeXEntry.TYPE_INCOLLECTION;
		}

		/*
		 * catch:
		 * [IEEE|IET|AIP|IBM|...] Journals & Magazines
		 * IEEE Early Access Articles
		 */
		// no need to check, since this is the default/fallback type anyways...
		// if (docid.contains("journals") || docid.contains("magazines") || docid.contains("articles")) {
		//  	return BibTeXEntry.TYPE_ARTICLE;
		// }
		return BibTeXEntry.TYPE_ARTICLE;
	}

}
