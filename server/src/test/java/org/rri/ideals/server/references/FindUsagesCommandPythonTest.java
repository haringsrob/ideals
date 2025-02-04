package org.rri.ideals.server.references;

import org.eclipse.lsp4j.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.rri.ideals.server.LspPath;
import org.rri.ideals.server.TestUtil;

import java.util.HashSet;
import java.util.List;

@RunWith(JUnit4.class)
public class FindUsagesCommandPythonTest extends ReferencesCommandTestBase {
  @Before
  public void copyDirectoryToProject() {
    projectFile = myFixture.copyDirectoryToProject("python/project1", "");
  }

  @Test
  public void testFindUsagesPythonVariable() {
    final var file = projectFile.findChild("findUsagesPythonVariable.py");
    assertNotNull(file);
    final var path = LspPath.fromVirtualFile(file);
    final var findUsagesPythonVariableUri = path.toLspUri();

    var answers = new HashSet<>(List.of(
            location(findUsagesPythonVariableUri, TestUtil.newRange(0, 0, 0, 1)),
            location(findUsagesPythonVariableUri, TestUtil.newRange(1, 4, 1, 5)),
            location(findUsagesPythonVariableUri, TestUtil.newRange(2, 4, 2, 5))));
    var pos = new Position(0, 0);
    check(answers, pos, path);

    answers = new HashSet<>(List.of(
            location(findUsagesPythonVariableUri, TestUtil.newRange(7, 13, 7, 14)),
            location(findUsagesPythonVariableUri, TestUtil.newRange(10, 13, 10, 14)),
            location(findUsagesPythonVariableUri, TestUtil.newRange(13, 15, 13, 21))));
    pos = new Position(7, 13);
    check(answers, pos, path);
  }

  @Test
  public void testFindUsagesPythonMethod() {
    final var findUsagesPythonMethodFile = projectFile.findChild("findUsagesPythonMethod.py");
    assertNotNull(findUsagesPythonMethodFile);
    final var definitionPythonFile = projectFile.findChild("definitionPython.py");
    assertNotNull(definitionPythonFile);
    final var class1File = projectFile.findChild("class1.py");
    assertNotNull(class1File);
    final var findUsagesPythonMethodPath = LspPath.fromVirtualFile(findUsagesPythonMethodFile);
    final var definitionPythonPath = LspPath.fromVirtualFile(definitionPythonFile);
    final var class1Path = LspPath.fromVirtualFile(class1File);

    final var findUsagesPythonMethodUri = findUsagesPythonMethodPath.toLspUri();
    final var definitionPythonUri = definitionPythonPath.toLspUri();

    var answers = new HashSet<>(List.of(
            location(definitionPythonUri, TestUtil.newRange(10, 0, 10, 4)),
            location(findUsagesPythonMethodUri, TestUtil.newRange(3, 0, 3, 4)),
            location(findUsagesPythonMethodUri, TestUtil.newRange( 1, 29, 1, 33))));

    check(answers, new Position(3, 0), findUsagesPythonMethodPath);

    answers = new HashSet<>(List.of(location(findUsagesPythonMethodUri, TestUtil.newRange(5, 0, 5, 6))));
    check(answers, new Position(1, 8), class1Path);
  }

  @Test
  public void testFindUsagesPythonClass() {
    final var class1File = projectFile.findChild("class1.py");
    assertNotNull(class1File);
    final var definitionPythonFile = projectFile.findChild("definitionPython.py");
    assertNotNull(definitionPythonFile);
    final var typeDefinitionPythonFile = projectFile.findChild("typeDefinitionPython.py");
    assertNotNull(typeDefinitionPythonFile);
    final var findUsagesPythonMethodFile = projectFile.findChild("findUsagesPythonMethod.py");
    assertNotNull(findUsagesPythonMethodFile);
    final var class1Path = LspPath.fromVirtualFile(class1File);
    final var definitionPythonPath = LspPath.fromVirtualFile(definitionPythonFile);
    final var typeDefinitionPythonPath = LspPath.fromVirtualFile(typeDefinitionPythonFile);
    final var findUsagesPythonMethodPath = LspPath.fromVirtualFile(findUsagesPythonMethodFile);

    final var definitionPythonUri = definitionPythonPath.toLspUri();
    final var typeDefinitionPythonUri = typeDefinitionPythonPath.toLspUri();
    final var findUsagesPythonMethodUri = findUsagesPythonMethodPath.toLspUri();

    final var answers = new HashSet<>(List.of(
            location(definitionPythonUri, TestUtil.newRange(12, 5, 12, 13)),
            location(findUsagesPythonMethodUri, TestUtil.newRange(4, 4, 4, 12)),
            location(typeDefinitionPythonUri, TestUtil.newRange(6, 4, 6, 12))));

    check(answers, new Position(12, 12), definitionPythonPath);
    check(answers, new Position(0, 6), class1Path);
  }
}
