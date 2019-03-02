package org.monarchinitiative.phenol.stats;

import java.util.Collections;
import java.util.List;

public class BenjaminiYekutieli<T> implements MultipleTestingCorrection<T> {

  @Override
  public void adjustPvals(List<? extends Item2PValue<T>> pvals) {
    Collections.sort(pvals);
    int N = pvals.size();
    double h = 0.0;
    for (int l = 1; l <= N; l++) {
      h += 1.0 / l;
    }
    /* Adjust the p values according to BY. Note that all object
     * within relevantP also are objects within p!
     */
    for (int r = 0; r < N; r++) {
      Item2PValue item = pvals.get(r);
      double raw_p = item.getRawPValue();
      double adj_p = raw_p * N * h / (r + 1);
      item.setAdjustedPValue(adj_p);
    }
    enforcePValueMonotony(pvals);
  }


  public String getName() {
    return "Benjamini-Yekutieli";
  }
}
