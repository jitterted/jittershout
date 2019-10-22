package com.jitterted.jittershout;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.jitterted.jittershout")
public class HexArchTest {

  @Test
  public void domainMustNotDependOnAdapter() throws Exception {
    JavaClasses importedClasses = new ClassFileImporter().importPackages("com.jitterted.jittershout");

    ArchRule myRule = noClasses().that().resideInAPackage("..domain..")
                                 .should().dependOnClassesThat().resideInAPackage("..adapter..");

    myRule.check(importedClasses);
  }

  @Test
  public void domainMustNotDependOnTwitch4J() throws Exception {
    JavaClasses importedClasses = new ClassFileImporter().importPackages("com.jitterted.jittershout");

    ArchRule myRule = noClasses().that().resideInAPackage("..domain..")
                                 .should().dependOnClassesThat().resideInAPackage("com.github.twitch4j..");

    myRule.check(importedClasses);
  }

}
