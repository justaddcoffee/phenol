package com.github.phenomics.ontolib.formats.hpo;

import com.github.phenomics.ontolib.ontology.data.TermId;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Model of a disease from the HPO annotations. This is an extension of HpoDisease and will be replaced in ontolib
 *
 * <p>
 * The main purpose here is to separate phenotypic abnormalities from mode of inheritance and other
 * annotations.
 * </p>
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 * @author <a href="mailto:sebastian.koehler@charite.de">Sebastian Koehler</a>
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 * @version 0.2.1 (2017-11-16)
 */
public final class HpoDiseaseWithMetadata {
    /** Name of the disease from annotation. */
    private final String name;
    /** Name of the database, e.g., OMIM, DECIPHER, ORPHANET */
    private final String database;

    private final String diseaseDatabaseId;

    /** {@link TermId}s with phenotypic abnormalities and their frequencies. */
    private final List<TermIdWithMetadata> phenotypicAbnormalities;

    /** {@link TermId}s with mode of inheritance and their frequencies. */
    private final List<TermId> modesOfInheritance;


    private final List<TermId> negativeAnnotations;

    public String getDiseaseDatabaseId() {
        return diseaseDatabaseId;
    }

    /**
     * Constructor.
     *
     * @param name Name of the disease.
     * @param phenotypicAbnormalities {@link List} of phenotypic abnormalities with their frequencies.
     * @param modesOfInheritance {@link List} of modes of inheritance with their frequencies.
     */
    public HpoDiseaseWithMetadata(String name,
                                  String dbase,
                                  String databaseId,
                                  List<TermIdWithMetadata> phenotypicAbnormalities,
                                  List<TermId> modesOfInheritance,
                                  List<TermId> notTerms) {
        this.name = name;
        this.database=dbase;
        this.diseaseDatabaseId=databaseId;
        this.phenotypicAbnormalities = ImmutableList.copyOf(phenotypicAbnormalities);
        this.modesOfInheritance = ImmutableList.copyOf(modesOfInheritance);
        this.negativeAnnotations = ImmutableList.copyOf(notTerms);
    }

    /**
     * @return The name of the disease.
     */
    public String getName() {
        return name;
    }

    /**@return the count of the non-negated annotations excluding mode of inheritance. */
    public int getNumberOfPhenotypeAnnotations() { return this.phenotypicAbnormalities.size(); }

    /**
     * @return The list of frequency-annotated phenotypic abnormalities.
     */
    public List<TermIdWithMetadata> getPhenotypicAbnormalities() {
        return phenotypicAbnormalities;
    }




    /**
     * @return The list of frequency-annotated modes of inheritance.
     */
    public List<TermId> getModesOfInheritance() {
        return modesOfInheritance;
    }


    public List<TermId> getNegativeAnnotations() { return this.negativeAnnotations;}

    /**
     * Users can user this function to get the TermIdWithMetadata corresponding to a TermId
     * @param id id of the plain {@link TermId} for which we want to have the {@link TermIdWithMetadata}.
     * @return corresponding {@link TermIdWithMetadata} or null if not present.
     */
    public TermIdWithMetadata getTermIdWithMetadata(TermId id) {
        return phenotypicAbnormalities.stream().filter( timd -> timd.getTermId().equals(id)).findAny().orElse(null);
    }

  /**
   * @param tid
   * @return true if there is a direct annotation to tid. Does not include indirect annotations from annotation propagation rule.
   */
  public boolean isDirectlyAnnotatedTo(TermId tid) {
      boolean matches=false;
      for (TermIdWithMetadata tiwm : phenotypicAbnormalities) {
        if (tiwm.getTermId().equals(tid)) return true;
      }
      return false;
    }
  /**
   * @param tidset
   * @return true if there is a direct annotation to any of the terms in tidset. Does not include indirect annotations from annotation propagation rule.
   */
  public boolean isDirectlyAnnotatedToAnyOf(Set<TermId> tidset) {
    boolean matches=false;
    for (TermIdWithMetadata tiwm : phenotypicAbnormalities) {
      if (tidset.contains(tiwm.getTermId())) return true;
    }
    return false;
  }

  /**
   * Returns the mean frequency of the feature in the disease.
   * @param tid
   * @return
   */
  public double getFrequencyOfTermInDisease(TermId tid) {
    TermIdWithMetadata tiwm = phenotypicAbnormalities.
      stream().
      filter(twm -> twm.getTermId().equals(tid)).
      findFirst().orElse(null);
    if (tiwm==null) {
      return 0D; // term not annotated to disease so frequency is zero
    }
    else return tiwm.getFrequency().mean();
  }


    @Override
    public String toString() {
        String abnormalityList=phenotypicAbnormalities.
          stream().
          map(TermIdWithMetadata::getIdWithPrefix).
          collect(Collectors.joining(";"));
        return String.format("HpoDisease [name=%s;%s:%s] phenotypicAbnormalities=\n%s" +
                ", modesOfInheritance=%s",name,database,diseaseDatabaseId,abnormalityList,modesOfInheritance);
    }

}

