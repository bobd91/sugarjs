package org.sugarj.javascript;

import java.util.LinkedList;

import org.sugarj.common.path.Path;
import org.sugarj.languagelib.SourceFileContent;

public class JavaScriptSourceFileContent extends SourceFileContent {

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

  public SourceFileContent.Generated getCode(Path outFile) {
    return new SourceFileContent.Generated(code.toString(), new LinkedList<String>());
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
