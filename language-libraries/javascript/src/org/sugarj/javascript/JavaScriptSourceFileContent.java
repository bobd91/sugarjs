package org.sugarj.javascript;

import java.util.Set;

import org.strategoxt.HybridInterpreter;
import org.sugarj.common.path.Path;
import org.sugarj.common.path.RelativePath;
import org.sugarj.languagelib.SourceFileContent;

public class JavaScriptSourceFileContent extends SourceFileContent {
  
  private static final long serialVersionUID = 1718569642175495936L;

  // The program code is collected in sections, usually just one
  // but could be more if sugar is mixed in with the JavaScript
  StringBuilder code = new StringBuilder();

  @Override
  public boolean isEmpty() {
    return 0 == code.length();
  }

  // Add a new section of program 
  public void addProgram(String program) {
    if(!isEmpty()) {
      code.append('\n');
    }
    code.append(program);
  }

  public String getCode(Set<RelativePath> generatedFiles, HybridInterpreter interp, Path outFile) {
    return code.toString();
  }
  
  public int hashCode() {
    return code.hashCode();
  }

  public boolean equals(Object o) {
    if (!(o instanceof JavaScriptSourceFileContent))
      return false;

    JavaScriptSourceFileContent other = (JavaScriptSourceFileContent) o;
    return other.isEmpty()
           ? isEmpty()
           : other.code.equals(code);
  }
  
}
