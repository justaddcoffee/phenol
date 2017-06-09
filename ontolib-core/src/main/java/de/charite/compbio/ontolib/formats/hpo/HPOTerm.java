package de.charite.compbio.ontolib.formats.hpo;

import de.charite.compbio.ontolib.ontology.data.Term;
import de.charite.compbio.ontolib.ontology.data.TermID;
import de.charite.compbio.ontolib.ontology.data.TermSynonym;
import java.util.List;

/**
 * Representation of a term in the HPO.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 * @author <a href="mailto:sebastian.koehler@charite.de">Sebastian Koehler</a>
 */
public class HPOTerm implements Term {

  /** The HPO term's ID. */
  private final TermID id;

  /** Alternative term IDs. */
  private final List<TermID> altTermIDs;

  /** The human-readable name of the term. */
  private final String name;

  /** The term's definition. */
  private final String definition;

  /** The term's comment string. */
  private final String comment;

  /** The subset that the term is in, <code>null</code> if none. */
  private final String subset;

  /** The list of term synonyms. */
  private final List<TermSynonym> synonyms;

  /** Whether or not the term is obsolete. */
  private final boolean obsolete;

  /** The term's author name. */
  private final String createdBy;

  /** The term's creation date. */
  private final String creationDate; // TODO: replace by Date?

  /**
   * Constructor.
   *
   * @param id The term's ID.
   * @param altTermIDs Alternative term IDs.
   * @param name Human-readable term name.
   * @param definition Term definition.
   * @param comment Term comment.
   * @param subset The subset that the term is in.
   * @param synonyms The synonyms for the term.
   * @param obsolete Whether or not the term is obsolete.
   * @param createdBy Author of the term.
   * @param creationDate Date of creation of the term.
   */
  public HPOTerm(TermID id, List<TermID> altTermIDs, String name, String definition, String comment,
      String subset, List<TermSynonym> synonyms, boolean obsolete, String createdBy,
      String creationDate) {
    this.id = id;
    this.altTermIDs = altTermIDs;
    this.name = name;
    this.definition = definition;
    this.comment = comment;
    this.subset = subset;
    this.synonyms = synonyms;
    this.obsolete = obsolete;
    this.createdBy = createdBy;
    this.creationDate = creationDate;
  }

  @Override
  public TermID getID() {
    return id;
  }

  @Override
  public List<TermID> getAltTermIDs() {
    return altTermIDs;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDefinition() {
    return definition;
  }

  @Override
  public String getComment() {
    return comment;
  }

  @Override
  public String getSubset() {
    return subset;
  }

  @Override
  public List<TermSynonym> getSynonyms() {
    return synonyms;
  }

  @Override
  public boolean isObsolete() {
    return obsolete;
  }

  @Override
  public String getCreatedBy() {
    return createdBy;
  }

  @Override
  public String getCreationDate() {
    return creationDate;
  }

}