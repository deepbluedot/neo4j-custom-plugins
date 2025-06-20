package app.syncly.neo4j.fulltext.analyzer;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import java.io.IOException;

public class ConcatenatingTokenFilter extends TokenFilter {
    private final CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);
    private boolean done = false;

    public ConcatenatingTokenFilter(TokenStream input) {
        super(input);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (done) {
            return false;
        }

        StringBuilder sb = new StringBuilder();
        while (input.incrementToken()) {
            sb.append(termAttr.toString());
        }

        if (sb.length() > 0) {
            clearAttributes();
            termAttr.append(sb.toString());
            done = true;
            return true;
        }

        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        done = false;
    }
}
