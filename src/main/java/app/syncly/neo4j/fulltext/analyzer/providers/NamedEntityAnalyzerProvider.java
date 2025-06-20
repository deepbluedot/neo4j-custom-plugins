package app.syncly.neo4j.fulltext.analyzer.providers;

import static org.apache.lucene.analysis.en.EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.neo4j.annotations.service.ServiceProvider;
import org.neo4j.graphdb.schema.AnalyzerProvider;

@ServiceProvider
public class NamedEntityAnalyzerProvider extends AnalyzerProvider {
    public NamedEntityAnalyzerProvider() {
        super("named-entity-analyzer");
    }

    @Override
    public Analyzer createAnalyzer() {
        // FIXME(Kyle): Implement custom analyzer
        return new StandardAnalyzer(ENGLISH_STOP_WORDS_SET);
    }

    @Override
    public String description() {
        return "The Named Entity Analyzer applied to the properties of named entitities "
                + "where the text is tokenized, lowercased, and special characters are removed.";
    }
}
