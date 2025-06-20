package app.syncly.neo4j.fulltext.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class NamedEntityAnalyzer extends StopwordAnalyzerBase {
    /*
     * NOTE: This is a complete re-implementation of the following class:
     * `org.apache.lucene.analysis.standard.StandardAnalyzer`
     */

    public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

    private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

    public NamedEntityAnalyzer(CharArraySet stopWords) {
        super(stopWords);
    }

    public NamedEntityAnalyzer() {
        this(CharArraySet.EMPTY_SET);
    }

    public NamedEntityAnalyzer(Reader stopWords) throws IOException {
        this(loadStopwordSet(stopWords));
    }

    public void setMaxTokenLength(int length) {
        maxTokenLength = length;
    }

    public int getMaxTokenLength() {
        return maxTokenLength;
    }

    @Override
    protected TokenStreamComponents createComponents(final String fieldName) {
        final StandardTokenizer src = new StandardTokenizer(); // 1. split text into words first
        src.setMaxTokenLength(maxTokenLength);
        TokenStream tok = new LowerCaseFilter(src); // 2. make it case-insensitive
        tok = new StopFilter(tok, stopwords); // 3. remove stop words
        tok = new PatternReplaceFilter(
                tok,
                Pattern.compile("[^\\p{L}\\p{N}]"),
                "",
                true); // 4. remove characters that are not letters or numbers
        tok = new ConcatenatingTokenFilter(tok); // 5.. concatenate tokens into a single token
        return new TokenStreamComponents(r -> {
            src.setMaxTokenLength(NamedEntityAnalyzer.this.maxTokenLength);
            src.setReader(r);
        }, tok);
    }

    @Override
    protected Reader initReader(String fieldName, Reader reader) {
        return new PhonemeDecompositionCharFilter(reader);
    }
}
