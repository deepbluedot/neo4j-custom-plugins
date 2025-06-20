package app.syncly.neo4j.fulltext.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class PhonemeDecompositionCharFilterTest {
    @Test
    public void testDecomposition() throws IOException {
        // String input = "안녕하세요";
        String input = "ㅋㅗ사abc스!!!";
        PhonemeDecompositionCharFilter reader = new PhonemeDecompositionCharFilter(new StringReader(input));
        StringBuilder output = new StringBuilder();
        int c;
        while ((c = reader.read()) != -1) {
            output.append((char) c);
        }
        reader.close();
        // String expectedOutput = "ㅇㅏㄴㄴㅕㅇㅎㅏㅅㅔㅇㅛ"; // Expected decomposition result
        String expectedOutput = "ㅋㅗㅅㅏabcㅅㅡ!!!"; // Expected decomposition result
        System.out.println("Input: " + input);
        System.out.println("Output: " + output.toString());
        assert output.toString().equals(expectedOutput) : "Expected: " + expectedOutput + ", but got: " + output;
    }
}
