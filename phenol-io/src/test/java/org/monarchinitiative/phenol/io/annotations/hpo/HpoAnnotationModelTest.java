package org.monarchinitiative.phenol.io.annotations.hpo;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.monarchinitiative.phenol.annotations.hpo.HpoAnnotationModel;
import org.monarchinitiative.phenol.base.PhenolException;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


class HpoAnnotationModelTest {
    private static HpoAnnotationModel v2sf=null;


    @BeforeAll
    static void init() throws PhenolException {
        Path hpOboPath = Paths.get("src","test","resources","annotations","hp_head.obo");
        Ontology ontology = OntologyLoader.loadOntology(hpOboPath.toFile());
        Path omim123456path = Paths.get("src","test","resources","annotations","OMIM-123456.tab");
        String omim123456file = omim123456path.toAbsolutePath().toString();
        HpoAnnotationFileParser parser = new HpoAnnotationFileParser(omim123456file,ontology);
        v2sf = parser.parse();
    }

    @Test
    void testParse() {
       Assertions.assertNotNull(v2sf);
    }

    @Test
    void basenameTest() {
        Assertions.assertEquals("OMIM-123456.tab", v2sf.getBasename());
    }

    @Test
    void isOmimTest() {
        Assertions.assertTrue(v2sf.isOMIM());
        Assertions.assertFalse(v2sf.isDECIPHER());
    }

    /** Our test file has three annotation lines. */
    @Test
    void numberOfAnnotationsTest() {
        Assertions.assertEquals(3,v2sf.getNumberOfAnnotations());
    }

    @Test
  void testFrequencyMerge() {
     HpoAnnotationModel merged= v2sf.getMergedModel();
     assertEquals(2,merged.getNumberOfAnnotations());
    }

}
