package app.syncly.neo4j.fulltext.analyzer.providers;

import static org.apache.lucene.analysis.en.EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;

import org.apache.lucene.analysis.Analyzer;
import org.neo4j.annotations.service.ServiceProvider;
import org.neo4j.graphdb.schema.AnalyzerProvider;
import app.syncly.neo4j.fulltext.analyzer.NamedEntityAnalyzer;

@ServiceProvider
public class NamedEntity extends AnalyzerProvider {
    public static final String NAMED_ENTITY_ANALYZER_NAME = "named-entity-analyzer";

    public NamedEntity() {
        super(NAMED_ENTITY_ANALYZER_NAME);
    }

    @Override
    public Analyzer createAnalyzer() {
        return new NamedEntityAnalyzer(ENGLISH_STOP_WORDS_SET);
    }

    @Override
    public String description() {
        return "The Named Entity Analyzer applied to the properties of named entitities. "
                + "Lowercases the text and filters out all non-letter and non-number characters. "
                + "Note that a single token is created from the input text.";
    }
}
