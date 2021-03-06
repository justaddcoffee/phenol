package org.monarchinitiative.phenol.io.utils;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import org.prefixcommons.CurieUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jules Jacobsen <j.jacobsen@qmul.ac.uk>
 */
class CurieUtilBuilderTest {

  @Test
  void defaultCurieMap() {
    assertFalse(CurieUtilBuilder.defaultCurieMap().isEmpty());
  }

  @Test
  void defaultCurieUtil() {
    CurieUtil instance = CurieUtilBuilder.defaultCurieUtil();
    assertEquals(Optional.of("HP:0000001"), instance.getCurie("http://purl.obolibrary.org/obo/HP_0000001"));
  }

  @Test
  void defaultCurieUtilWithDefaultsAndUserDefinedResource() {
    CurieUtil defaultCurieUtil = CurieUtilBuilder.defaultCurieUtil();
    assertFalse(defaultCurieUtil.getCurieMap().containsKey("WIBBLE"));

    CurieUtil instance = CurieUtilBuilder.withDefaultsAnd(ImmutableMap.of("WIBBLE", "http://purl.obolibrary.org/obo/WIBBLE_"));
    assertTrue(instance.getCurieMap().containsKey("WIBBLE"));
    assertEquals(Optional.of("WIBBLE:0000001"), instance.getCurie("http://purl.obolibrary.org/obo/WIBBLE_0000001"));
    assertEquals("http://purl.obolibrary.org/obo/WIBBLE_", instance.getExpansion("WIBBLE"));
    assertEquals(Optional.of("http://purl.obolibrary.org/obo/WIBBLE_0000001"), instance.getIri("WIBBLE:0000001"));
  }

  @Test
  void curieUtilWithUserSpecifiedMappingsOnly() {
    ImmutableMap<String, String> userDefinedCurieMap = ImmutableMap.of(
      "WIBBLE", "http://purl.obolibrary.org/obo/WIBBLE_",
      "FLUFFY", "http://purl.obolibrary.org/obo/FLUFFY_"
    );

    CurieUtil instance = CurieUtilBuilder.just(userDefinedCurieMap);

    assertEquals(userDefinedCurieMap, instance.getCurieMap());
    assertEquals(Optional.of("WIBBLE:0000001"), instance.getCurie("http://purl.obolibrary.org/obo/WIBBLE_0000001"));
    assertEquals(Optional.of("FLUFFY:0000001"), instance.getCurie("http://purl.obolibrary.org/obo/FLUFFY_0000001"));
  }
}
