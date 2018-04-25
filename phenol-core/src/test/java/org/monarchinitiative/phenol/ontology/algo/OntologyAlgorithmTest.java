package org.monarchinitiative.phenol.ontology.algo;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Before;
import org.junit.Test;
import org.monarchinitiative.phenol.graph.IdLabeledEdge;
import org.monarchinitiative.phenol.graph.util.GraphUtil;
import org.monarchinitiative.phenol.ontology.data.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.monarchinitiative.phenol.ontology.algo.OntologyAlgorithm.*;
import static org.junit.Assert.*;

public class OntologyAlgorithmTest {

  private ImmutableSortedMap<String, String> metaInfo;
  private DefaultDirectedGraph<TermId, IdLabeledEdge> graph;

  private TermId rootTermId;
  private ImmutableMap<TermId, TestTerm> termMap;
  private ImmutableMap<TermId, TestTerm> obsoleteTermMap;
  private ImmutableMap<Integer, TestRelationship> relationMap;
  private ImmutableOntology<TestTerm, TestRelationship> ontology;

  private ImmutableTermId id1;
  private ImmutableTermId id2;
  private ImmutableTermId id3;
  private ImmutableTermId id4;
  private ImmutableTermId id5;

  @Before
  public void setUp() {
    metaInfo = ImmutableSortedMap.of();

    id1 = ImmutableTermId.constructWithPrefix("HP:0000001");
    id2 = ImmutableTermId.constructWithPrefix("HP:0000002");
    id3 = ImmutableTermId.constructWithPrefix("HP:0000003");
    id4 = ImmutableTermId.constructWithPrefix("HP:0000004");
    id5 = ImmutableTermId.constructWithPrefix("HP:0000005");

    graph = new DefaultDirectedGraph<TermId, IdLabeledEdge>(IdLabeledEdge.class);
    GraphUtil.addEdgeToGraph(graph, id1, id2, 1);
    GraphUtil.addEdgeToGraph(graph, id1, id3, 2);
    GraphUtil.addEdgeToGraph(graph, id1, id4, 3);
    GraphUtil.addEdgeToGraph(graph, id2, id5, 4);
    GraphUtil.addEdgeToGraph(graph, id3, id5, 5);
    GraphUtil.addEdgeToGraph(graph, id4, id5, 6);

    rootTermId = id5;

    ImmutableMap.Builder<TermId, TestTerm> termMapBuilder = ImmutableMap.builder();
    termMapBuilder.put(
        id1,
        new TestTerm(
            id1,
            new ArrayList<>(),
            "term1",
            "some definition 1",
            null,
            new ArrayList<>(),
            new ArrayList<>(),
            false,
            null,
            null,
            new ArrayList<>()));
    termMapBuilder.put(
        id2,
        new TestTerm(
            id2,
            new ArrayList<>(),
            "term2",
            "some definition 2",
            null,
            new ArrayList<>(),
            new ArrayList<>(),
            false,
            null,
            null,
            new ArrayList<>()));
    termMapBuilder.put(
        id3,
        new TestTerm(
            id3,
            new ArrayList<>(),
            "term3",
            "some definition 3",
            null,
            new ArrayList<>(),
            new ArrayList<>(),
            false,
            null,
            null,
            new ArrayList<>()));
    termMapBuilder.put(
        id4,
        new TestTerm(
            id4,
            new ArrayList<>(),
            "term4",
            "some definition 4",
            null,
            new ArrayList<>(),
            new ArrayList<>(),
            false,
            null,
            null,
            new ArrayList<>()));
    termMapBuilder.put(
        id5,
        new TestTerm(
            id5,
            new ArrayList<>(),
            "term5",
            "some definition 5",
            null,
            new ArrayList<>(),
            new ArrayList<>(),
            false,
            null,
            null,
            new ArrayList<>()));
    termMap = termMapBuilder.build();

    obsoleteTermMap = ImmutableMap.of();

    ImmutableMap.Builder<Integer, TestRelationship> relationMapBuilder = ImmutableMap.builder();
    relationMapBuilder.put(1, new TestRelationship(id1, id2, 1));
    relationMapBuilder.put(2, new TestRelationship(id1, id3, 2));
    relationMapBuilder.put(3, new TestRelationship(id1, id4, 3));
    relationMapBuilder.put(4, new TestRelationship(id2, id5, 4));
    relationMapBuilder.put(5, new TestRelationship(id3, id5, 5));
    relationMapBuilder.put(6, new TestRelationship(id4, id5, 6));
    relationMap = relationMapBuilder.build();

    ontology =
        new ImmutableOntology<>(
            metaInfo,
            graph,
            rootTermId,
            termMap.keySet(),
            obsoleteTermMap.keySet(),
            termMap,
            relationMap);
  }

  /** The example graph has id1->id2, id1->id3, id1->id4, id2->id5, id3-> id5, id4->id5 */
  @Test
  public void testPathExists() {
    assertTrue(OntologyAlgorithm.existsPath(ontology, id1, id2));
    assertFalse(OntologyAlgorithm.existsPath(ontology, id2, id1));
    assertTrue(OntologyAlgorithm.existsPath(ontology, id1, id3));
    assertFalse(OntologyAlgorithm.existsPath(ontology, id3, id1));
    assertTrue(OntologyAlgorithm.existsPath(ontology, id1, id4));
    assertFalse(OntologyAlgorithm.existsPath(ontology, id4, id1));
    assertTrue(OntologyAlgorithm.existsPath(ontology, id1, id5));
    assertFalse(OntologyAlgorithm.existsPath(ontology, id5, id1));
    assertTrue(OntologyAlgorithm.existsPath(ontology, id2, id5));
    assertFalse(OntologyAlgorithm.existsPath(ontology, id5, id2));
    assertTrue(OntologyAlgorithm.existsPath(ontology, id4, id5));
    assertFalse(OntologyAlgorithm.existsPath(ontology, id5, id4));
    // test that a term cannot have a path to itself.
    assertFalse(OntologyAlgorithm.existsPath(ontology, id5, id5));
  }

  /** Test the default function, which includes the term itself in the set of returned terms */
  @Test
  public void testGetTermChildrenId4andId1() {
    // id4 has only one child term, id1: id1->id4
    Set<TermId> expected = ImmutableSet.of(id4, id1);
    assertEquals(expected, getChildTerms(ontology, id4));
    // id1 is a leaf term and thus has no children
    expected = ImmutableSet.of(id1);
    assertEquals(expected, getChildTerms(ontology, id1));
  }

  /** Test the default function, which includes the term itself in the set of returned terms */
  @Test
  public void testGetTermChildrenId5() {
    // id5 has 3 children: id2->id5, id3-> id5, id4->id5
    Set<TermId> expected = ImmutableSet.of(id2, id3, id4, id5);
    assertEquals(expected, getChildTerms(ontology, id5));
  }

  @Test
  public void testGetChildrenOfSet() {
    // the child of both id2 and id3 is id1
    Set<TermId> queryTerms = ImmutableSet.of(id2, id3);
    Set<TermId> expected = ImmutableSet.of(id1, id2, id3);
    assertEquals(expected, OntologyAlgorithm.getChildTerms(ontology, queryTerms));
  }

  /**
   * We are using a version of the function getChildTerms that does not return the query (parent)
   * term.
   */
  @Test
  public void testReturnChildrenWithoutOriginalTerm() {
    // id5 has 3 children: id2->id5, id3-> id5, id4->id5
    Set<TermId> expected = ImmutableSet.of(id2, id3, id4);
    assertEquals(expected, OntologyAlgorithm.getChildTerms(ontology, id5, false));
  }

  /**
   * getDescendents returns not only children but all descendents. id1 is a child of id3, which is a
   * child of id5, so id1 is a descendent but not a child of id5
   */
  @Test
  public void testGetDescendents() {
    Set<TermId> expected = ImmutableSet.of(id1, id2, id3, id4, id5);
    assertEquals(expected, OntologyAlgorithm.getDescendents(ontology, id5));
  }

  @Test
  public void testGetParentsId2() {
    // the only ancestor of id2 is id5: id2->id5
    Set<TermId> expected = ImmutableSet.of(id2, id5);
    assertEquals(expected, getParentTerms(ontology, id2));
    // id2 is not an ancestor of id5
    assertNotEquals(expected, getParentTerms(ontology, id5));
    // instead, only id5 is an ancestor of id5
    expected = ImmutableSet.of(id5);
    assertEquals(expected, getParentTerms(ontology, id5));
  }

  @Test
  public void testGetParentsId1() {
    // id1 has three parents. Since id5 is a parent of both id2 ans id1, id1 has three ancestors (four including id1)
    //id1->id2, id1->id3, id1 -> id4; id2->id5, id3-> id5,
    // id5 is not a parent of id1, though!
    Set<TermId> expected = ImmutableSet.of(id1, id2, id3, id4);
    assertEquals(expected, getParentTerms(ontology, id1));
  }

  @Test
  public void testGetParentsOfSet() {
    //id3-> id5, id4->id5
    Set<TermId> queryTerms = ImmutableSet.of(id3, id4);
    Set<TermId> expected = ImmutableSet.of(id3, id4, id5);
    assertEquals(expected, OntologyAlgorithm.getParentTerms(ontology, queryTerms));
  }

  @Test
  public void testGetAncestorsId1() {
    // id1 has id2, id3, id4m and id5 as ancestors
    Set<TermId> expected = ImmutableSet.of(id1, id2, id3, id4, id5);
    assertEquals(expected, OntologyAlgorithm.getAncestorTerms(ontology, id1));
  }

  @Test
  public void testGetAncestorsFromSubOntology() {
    // We first try with id5 as a new root term; the resulting subontology is identical to the original one. 
    Set<TermId> expected1 = ImmutableSet.of(id1, id2, id3, id4, id5);
    assertEquals(expected1, OntologyAlgorithm.getAncestorTerms(ontology, id5, id1, true));

    Set<TermId> expected2 = ImmutableSet.of(id2, id3, id4, id5);
    assertEquals(expected2, OntologyAlgorithm.getAncestorTerms(ontology, id5, id1, false));

    // We then try with id2 as a new root term; the resulting subontology is the one with two terms: id1 and id2.
    Set<TermId> expected3 = ImmutableSet.of(id1, id2);
    assertEquals(expected3, OntologyAlgorithm.getAncestorTerms(ontology, id2, id1, true));

    Set<TermId> expected4 = ImmutableSet.of(id2);
    assertEquals(expected4, OntologyAlgorithm.getAncestorTerms(ontology, id2, id1, false));
  }

  @Test
  public void testRootHasNoParent() {
    // id5 is the root of our graph and does not have a parent term other than itself
    Set<TermId> expected = ImmutableSet.of(id5);
    assertEquals(expected, getParentTerms(ontology, id5));
  }

  /**
   * We are using a version of the function getChildTerms that does not return the query (parent)
   * term.
   */
  @Test
  public void testReturnParentWithoutOriginalTerm() {
    // id1 has three parents. Since id5 is a parent of both id2 ans id1, id1 has three ancestors (four including id1)
    //id1->id2, id1->id3, id1 -> id4; id2->id5, id3-> id5,
    // id5 is not a parent of id1, though!
    Set<TermId> expected = ImmutableSet.of(id2, id3, id4);
    assertEquals(expected, getParentTerms(ontology, id1, false));
    // The root has no parent, we expect the empty set
    expected = new HashSet<>();
    assertEquals(expected, getParentTerms(ontology, id5, false));
  }
}