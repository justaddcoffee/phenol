package de.charite.compbio.ontolib.io.obo;

import java.util.ArrayList;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.junit.Before;

import de.charite.compbio.ontolib.io.obo.parser.Antlr4OBOParser;

/**
 * Test class base for Antlr4 OBO parsers.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class Antlr4OBOParserTestBase {

  /** The parse result listener to use. */
  private final TestListener innerListener = new TestListener();

  /** The parser base listener to use. */
  protected final OBOParserListener outerListener = new OBOParserListener(innerListener);

  @Before
  public void setUp() {
    setupStanzaKeyValues();
  }
  
  /**
   * Build and return {@link Antlr4OBOParser} for a given <code>text</code>.
   * 
   * @param text String with the text to parse.
   * @param mode Name of the mode to use.
   * @return {@link Antlr4OBOParser}, readily setup for parsing the OBO file.
   */
  protected Antlr4OBOParser buildParser(String text, String mode) {
    final CodePointCharStream inputStream = CharStreams.fromString(text);
    final OBOLexer l = new OBOLexer(inputStream);

    for (int i = 0; i < l.getModeNames().length; ++i) {
      if (mode.equals(l.getModeNames()[i])) {
        l.mode(i);
      }
    }

    Antlr4OBOParser p = new Antlr4OBOParser(new CommonTokenStream(l));

    p.addErrorListener(new BaseErrorListener() {
      @Override
      public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
          int charPositionInLine, String msg, RecognitionException e) {
        throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
      }
    });

    p.addErrorListener(new DiagnosticErrorListener());

    p.addParseListener(outerListener);

    return p;
  }

  protected Antlr4OBOParser buildParser(String text) {
    return buildParser(text, "DEFAULT_MODE");
  }

  /**
   * @return The inner listener, used for coarser-granular access.
   */
  public TestListener getInnerListener() {
    return innerListener;
  }

  /**
   * @return The listener, e.g., stores temporary values.
   */
  public OBOParserListener getOuterListener() {
    return outerListener;
  }

  protected static final class TestListener implements OBOParseResultListener {

    private Header header = null;
    private Stanza stanza = null;
    private boolean parsingComplete = false;

    @Override
    public void parsedHeader(Header header) {
      this.header = header;
    }

    @Override
    public void parsedStanza(Stanza stanza) {
      this.stanza = stanza;
    }

    @Override
    public void parsedFile() {
      this.parsingComplete = true;
    }

    public Header getHeader() {
      return header;
    }

    public Stanza getStanza() {
      return stanza;
    }

    public boolean isParsingComplete() {
      return parsingComplete;
    }

  }

  /**
   * Setup <code>stanzaKeyValues</code> member of <code>outerListener</code> for testing.
   */
  protected void setupStanzaKeyValues() {
    outerListener.stanzaKeyValues = new ArrayList<>();
  }

}

