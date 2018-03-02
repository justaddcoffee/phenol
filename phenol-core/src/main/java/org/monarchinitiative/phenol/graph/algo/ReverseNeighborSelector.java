package org.monarchinitiative.phenol.graph.algo;

import java.util.Iterator;

import org.monarchinitiative.phenol.graph.data.DirectedGraph;
import org.monarchinitiative.phenol.graph.data.Edge;

/**
 * Implementation of {@link NeighborSelector} using in-edges.
 *
 * <p>
 * This is an implementation detail and should not be used in client code.
 * </p>
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
class ReverseNeighborSelector<V extends Comparable<V>, E extends Edge<V>>
    implements
      NeighborSelector<V, E> {

  @Override
  public Iterator<V> nextFrom(DirectedGraph<V, E> g, V v) {
    return g.viaInEdgeIterator(v);
  }

}