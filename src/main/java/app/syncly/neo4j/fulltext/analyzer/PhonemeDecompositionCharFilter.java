package app.syncly.neo4j.fulltext.analyzer;

import java.io.Reader;
import java.io.IOException;

import org.apache.lucene.analysis.charfilter.BaseCharFilter;

public class PhonemeDecompositionCharFilter extends BaseCharFilter {
    private final static char[] CHOSEONG = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    private final static char[] JUNGSEONG = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
            'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    };

    private final static char[] JONGSEONG = {
            '\0', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ',
            'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    private final StringBuilder buffer = new StringBuilder();
    private int nextCharIdx = 0;

    private int inputOffset = 0; // 원본 위치
    private int cumulativeDiff = 0; // 총 길이 차이 누적

    public PhonemeDecompositionCharFilter(Reader input) {
        super(input);
    }

    @Override
    public int read() throws IOException {
        if (nextCharIdx < buffer.length()) {
            return buffer.charAt(nextCharIdx++);
        }

        int c = input.read();
        if (c == -1) {
            return -1;
        }

        char ch = (char) c;
        inputOffset++; // 원본 위치 1 증가

        if (isHangulSyllable(ch)) {
            String decomposed = decomposeHangul(ch);
            int addedLength = decomposed.length() - 1; // 원본은 1자, 분해 후 3자면 +2

            // 오프셋 보정 추가
            cumulativeDiff += addedLength;
            addOffCorrectMap(inputOffset, cumulativeDiff);

            buffer.setLength(0);
            buffer.append(decomposed);
            nextCharIdx = 1;
            return decomposed.charAt(0);
        } else {
            return ch;
        }
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int numRead = 0;
        for (int i = 0; i < len; i++) {
            int ch = read();
            if (ch == -1) {
                return numRead == 0 ? -1 : numRead;
            }
            cbuf[off + i] = (char) ch;
            numRead++;
        }
        return numRead;
    }

    private boolean isHangulSyllable(char ch) {
        return ch >= 0xAC00 && ch <= 0xD7A3;
    }

    private String decomposeHangul(char syllable) {
        int syllableIndex = syllable - 0xAC00;
        int choseongIndex = syllableIndex / (21 * 28);
        int jungseongIndex = (syllableIndex % (21 * 28)) / 28;
        int jongseongIndex = syllableIndex % 28;

        StringBuilder result = new StringBuilder();
        result.append(CHOSEONG[choseongIndex]);
        result.append(JUNGSEONG[jungseongIndex]);
        if (jongseongIndex != 0) {
            result.append(JONGSEONG[jongseongIndex]);
        }

        return result.toString();
    }

    @Override
    protected int correct(int currentOff) {
        return super.correct(currentOff);
    }
}
