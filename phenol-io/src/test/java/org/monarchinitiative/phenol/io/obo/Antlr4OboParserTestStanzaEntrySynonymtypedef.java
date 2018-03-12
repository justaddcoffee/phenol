package org.monarchinitiative.phenol.io.obo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser;
import de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser.HeaderKeyValueContext;
import de.charite.compbio.ontolib.io.obo.parser.Antlr4OboParser.KeyValueSynonymtypedefContext;

import org.junit.Test;

import org.monarchinitiative.phenol.ontology.data.TermSynonymScope;

public class Antlr4OboParserTestStanzaEntrySynonymtypedef extends Antlr4OboParserTestBase {

  @Test
  public void testParseNoModifierNoCommentAsHeaderKeyValue() {
    final String text = "synonymtypedef: UK_SPELLING \"British spelling\" EXACT\n";
    final Antlr4OboParser parser = buildParser(text);
    final HeaderKeyValueContext ctx = parser.headerKeyValue();
    final StanzaEntry stanzaEntry = (StanzaEntry) getOuterListener().getValue(ctx);

    assertEquals(StanzaEntryType.SYNONYMTYPEDEF, stanzaEntry.getType());
    assertNull(stanzaEntry.getTrailingModifier());
    assertNull(stanzaEntry.getComment());
  }

  @Test
  public void testParseNoModifierNoCommentAsKeyValueSynonymtypedef() {
    final String text = "synonymtypedef: UK_SPELLING \"British spelling\" EXACT\n";
    final Antlr4OboParser parser = buildParser(text);
    final KeyValueSynonymtypedefContext ctx = parser.keyValueSynonymtypedef();
    final StanzaEntrySynonymtypedef stanzaEntry =
        (StanzaEntrySynonymtypedef) getOuterListener().getValue(ctx);

    assertEquals(StanzaEntryType.SYNONYMTYPEDEF, stanzaEntry.getType());
    assertEquals("UK_SPELLING", stanzaEntry.getTermSynonymScope());
    assertEquals("British spelling", stanzaEntry.getDescription());
    assertEquals(TermSynonymScope.EXACT, stanzaEntry.getSynonymScopeIdentifier());
    assertNull(stanzaEntry.getTrailingModifier());
    assertNull(stanzaEntry.getComment());
  }

  @Test
  public void testParseModifierNoCommentAsKeyValueSynonymtypedef() {
    final String text = "synonymtypedef: UK_SPELLING \"British spelling\" EXACT {key=value}\n";
    final Antlr4OboParser parser = buildParser(text);
    final KeyValueSynonymtypedefContext ctx = parser.keyValueSynonymtypedef();
    final StanzaEntrySynonymtypedef stanzaEntry =
        (StanzaEntrySynonymtypedef) getOuterListener().getValue(ctx);

    assertEquals(StanzaEntryType.SYNONYMTYPEDEF, stanzaEntry.getType());
    assertEquals("UK_SPELLING", stanzaEntry.getTermSynonymScope());
    assertEquals("British spelling", stanzaEntry.getDescription());
    assertEquals(TermSynonymScope.EXACT, stanzaEntry.getSynonymScopeIdentifier());
    assertEquals(
        "TrailingModifier [keyValue=[KeyValue [key=key, value=value]]]",
        stanzaEntry.getTrailingModifier().toString());
    assertNull(stanzaEntry.getComment());
  }

  @Test
  public void testParseNoModifierCommentAsKeyValueSynonymtypedef() {
    final String text = "synonymtypedef: UK_SPELLING \"British spelling\" EXACT ! comment\n";
    final Antlr4OboParser parser = buildParser(text);
    final KeyValueSynonymtypedefContext ctx = parser.keyValueSynonymtypedef();
    final StanzaEntrySynonymtypedef stanzaEntry =
        (StanzaEntrySynonymtypedef) getOuterListener().getValue(ctx);

    assertEquals(StanzaEntryType.SYNONYMTYPEDEF, stanzaEntry.getType());
    assertEquals("UK_SPELLING", stanzaEntry.getTermSynonymScope());
    assertEquals("British spelling", stanzaEntry.getDescription());
    assertEquals(TermSynonymScope.EXACT, stanzaEntry.getSynonymScopeIdentifier());
    assertNull(stanzaEntry.getTrailingModifier());
    assertEquals("comment", stanzaEntry.getComment().toString());
  }

  @Test
  public void testParseModifierCommentAsKeyValueSynonymtypedef() {
    final String text =
        "synonymtypedef: UK_SPELLING \"British spelling\" EXACT {key=value} ! comment\n";
    final Antlr4OboParser parser = buildParser(text);
    final KeyValueSynonymtypedefContext ctx = parser.keyValueSynonymtypedef();
    final StanzaEntrySynonymtypedef stanzaEntry =
        (StanzaEntrySynonymtypedef) getOuterListener().getValue(ctx);

    assertEquals(StanzaEntryType.SYNONYMTYPEDEF, stanzaEntry.getType());
    assertEquals("UK_SPELLING", stanzaEntry.getTermSynonymScope());
    assertEquals("British spelling", stanzaEntry.getDescription());
    assertEquals(TermSynonymScope.EXACT, stanzaEntry.getSynonymScopeIdentifier());
    assertEquals(
        "TrailingModifier [keyValue=[KeyValue [key=key, value=value]]]",
        stanzaEntry.getTrailingModifier().toString());
    assertEquals("comment", stanzaEntry.getComment());
  }
}
