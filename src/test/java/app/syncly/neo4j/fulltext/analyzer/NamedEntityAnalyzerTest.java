package app.syncly.neo4j.fulltext.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.TokenStream;
import org.junit.Test;

import static org.apache.lucene.analysis.en.EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NamedEntityAnalyzerTest {
    @Test
    public void testAnalyzerOutput() throws IOException {
        String[] inputs = { "Hello World!", "A.B#C", "안녕ㅎ ㅏ세?요!", "TeSt\t123갉낪닳" };
        Analyzer analyzer = new NamedEntityAnalyzer(ENGLISH_STOP_WORDS_SET);
        List<String> tokenList = new ArrayList<>();
        for (String input : inputs) {
            List<String> tokens = analyzeText(analyzer, input);
            tokenList.add(tokens.get(0));
            System.out.println("Input: " + input);
            System.out.println("Tokens: " + tokens);
        }
        assertArrayEquals(tokenList.toArray(),
                new String[] { "helloworld", "abc", "ㅇㅏㄴㄴㅕㅇㅎㅏㅅㅔㅇㅛ", "test123ㄱㅏㄺㄴㅏㅄㄷㅏㅀ" });
    }

    private List<String> analyzeText(Analyzer analyzer, String text) throws IOException {
        List<String> result = new ArrayList<>();
        try (TokenStream tokenStream = analyzer.tokenStream("field", text)) {
            tokenStream.reset();
            CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
            while (tokenStream.incrementToken()) {
                result.add(attr.toString());
            }
            tokenStream.end();
        }
        return result;
    }
}
