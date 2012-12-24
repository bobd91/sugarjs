package org.sugarj.javascript;

import java.util.Set;

import org.strategoxt.HybridInterpreter;
import org.sugarj.common.path.Path;
import org.sugarj.common.path.RelativePath;
import org.sugarj.languagelib.SourceFileContent;

public class JavaScriptSourceFileContent extends SourceFileContent {
  
  private static final long serialVersionUID = 1718569642175495936L;

  String program;

  @Override
  public boolean isEmpty() {
    return null == program;
  }

  public void setProgram(String program) {
    this.program = program;
  }

  public String getCode(Set<RelativePath> generatedFiles, HybridInterpreter interp, Path outFile) {
    return program;
  }
  
  public int hashCode() {
    return program.hashCode();
  }

  public boolean equals(Object o) {
    if (!(o instanceof JavaScriptSourceFileContent))
      return false;

    JavaScriptSourceFileContent other = (JavaScriptSourceFileContent) o;
    return other.isEmpty()
           ? isEmpty()
           : other.program.equals(program);
  }

  
}
