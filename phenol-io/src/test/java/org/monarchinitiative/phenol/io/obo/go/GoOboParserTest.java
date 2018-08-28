package org.monarchinitiative.phenol.io.obo.go;

import static org.junit.Assert.assertEquals;
import static org.monarchinitiative.phenol.ontology.algo.OntologyAlgorithm.getAncestorTerms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.monarchinitiative.phenol.base.PhenolException;
import org.monarchinitiative.phenol.formats.go.GoOntology;
import org.monarchinitiative.phenol.graph.IdLabeledEdge;

import org.monarchinitiative.phenol.io.utils.ResourceUtils;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;


/**
 * Testcases that verify whether obo-formatted Go ontology is properly parsed and loaded.
 *
 * @author <a href="mailto:HyeongSikKim@lbl.gov">HyeongSik Kim</a>
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class GoOboParserTest {

  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();

  private Ontology ontology;

  @Before
  public void setUp() throws IOException, PhenolException {

    File goHeadFile;
    goHeadFile = tmpFolder.newFile("go_head.obo");
    ResourceUtils.copyResourceToFile("/go/go_head.obo", goHeadFile);
    GoOboParser parser = new GoOboParser(goHeadFile);

    this.ontology = parser.parse();
  }

  /** The obo file has the three top-level GO terms and one child each (i.e., three asserted is_a links).
   * The input of the GO file should create a new artificial root and should attach each of the top level terms,
   * and thus we expect 3+3=6 edges.
   */
  @Test
  public void testEdgeSetSize() {
    int expected=6;
    final DefaultDirectedGraph<TermId, IdLabeledEdge> graph = ontology.getGraph();
    assertEquals(expected, graph.edgeSet().size());
  }

  @Test
  public void testArtificialRootTerm() {
    TermId tid = ontology.getRootTermId();
    TermId expected = TermId.constructWithPrefix("GO:0000000");
    assertEquals(expected,tid);
  }


  @Test public void testReal() throws FileNotFoundException, PhenolException {
    String localpath="/home/peter/data/mgi/go.obo";
    GoOboParser parser = new GoOboParser(localpath);
    GoOntology gontology=parser.parse();
    Map<TermId,Term> termmap =  gontology.getTermMap();
    for (TermId tid : termmap.keySet()) {
      String name = termmap.get(tid).getName();
      tid=termmap.get(tid).getId();
      System.out.println("Retrieving ancestors for " + name +"[" + tid.getIdWithPrefix() +"]");
      Set<TermId> ancs = getAncestorTerms(gontology,tid,true);
      System.out.println(String.format("%s: ancestors-n=%s",tid,ancs.size()));
    }
  }



}
