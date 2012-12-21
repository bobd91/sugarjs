package org.sugarj.javascript;

// TODO: Code copied from Prolog impl and only changed enough to compile

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.HybridInterpreter;
import org.sugarj.JavaScriptLib;
import org.sugarj.common.Environment;
import org.sugarj.common.FileCommands;
import org.sugarj.common.path.Path;
import org.sugarj.common.path.RelativePath;
import org.sugarj.languagelib.SourceFileContent;

public class JavaScriptSourceFileContent extends SourceFileContent {
  
  private static final long serialVersionUID = 1718569642175495936L;

  public class JavaScriptModuleImport {
    String importName;
    IStrategoTerm productionDecl;
  }
  
  String moduleDecl;
  List<JavaScriptModuleImport> imports = new LinkedList<JavaScriptModuleImport>();
  List<JavaScriptModuleImport> checkedImports = new LinkedList<JavaScriptModuleImport>();
  boolean importsOptional;
  List<String> bodyDecls = new LinkedList<String>();
  List<String> reexports = new LinkedList<String>();
  JavaScriptLib lib;
  

  @Override
  public boolean isEmpty() {
    return true;
  }
  
  public JavaScriptSourceFileContent(JavaScriptLib javascriptLib) {
    lib = javascriptLib;
  }

  public void setModuleDecl(String moduleDecl) {
    this.moduleDecl = moduleDecl;
  }

  public void addImport(JavaScriptModuleImport imp) {
    imports.add(imp);
  }

  public void addCheckedImport(JavaScriptModuleImport imp) {
    checkedImports.add(imp);
  }

  public void setOptionalImport(boolean isOptional) {
    this.importsOptional = isOptional;
  }

  public void addBodyDecl(String bodyDecl) {
    bodyDecls.add(bodyDecl);
  }

  public String getCode(Set<RelativePath> generatedFiles, HybridInterpreter interp, Path outFile) throws ClassNotFoundException, IOException {
    List<String> files = new LinkedList<String>();
    for (RelativePath p : generatedFiles)
      files.add(FileCommands.dropExtension(p.getRelativePath()).replace(Environment.sep, "/"));

    StringBuilder code = new StringBuilder();
    code.append(moduleDecl);
    code.append('\n');
    
    for (JavaScriptModuleImport imp : checkedImports)                 
      code.append(getImportedModuleString(imp, interp)).append("\n");

    for (JavaScriptModuleImport imp : imports)
      if (files.contains(imp.importName))
        code.append(getImportedModuleString(imp, interp)).append("\n");
      else if (!importsOptional)
        throw new ClassNotFoundException(imp.importName);

    for (String bodyDecl : bodyDecls) {
      code.append(bodyDecl);
      code.append("\n");
    }
      
    return code.toString();
  }
  
  private String getImportedModuleString(JavaScriptModuleImport module, HybridInterpreter interp) throws IOException {
    IStrategoTerm trm = module.productionDecl;
    String importString = ":- use_module(";
    importString += module.importName;
    if (trm.getSubtermCount() > 1) {  // :- use_module(foo, bar/1).
      importString += getImportedModulePredicateList(module);
    }
    return importString + ").";
  }

  private String getImportedModulePredicateList(JavaScriptModuleImport module) throws IOException {
    if (module.productionDecl == null) 
      return "";
    
    String code = ", " + lib.prettyPrint(module.productionDecl.getSubterm(1));
    
    return code;
  }
  
  public JavaScriptModuleImport getImport(String importName, IStrategoTerm decl) {
    JavaScriptModuleImport imp = new JavaScriptModuleImport();
    imp.importName = importName.substring(importName.indexOf("/") + 1); // XXX: hacky, remove first directory. Should be replaced by a more robust implementation.
    imp.productionDecl = decl;
    
    return imp;
  }
  
  
  public int hashCode() {
    return moduleDecl.hashCode() + imports.hashCode() + bodyDecls.hashCode();
  }

  public boolean equals(Object o) {
    if (!(o instanceof JavaScriptSourceFileContent))
      return false;

    JavaScriptSourceFileContent other = (JavaScriptSourceFileContent) o;
    return other.moduleDecl.equals(moduleDecl) &&
        other.imports.equals(imports) &&
        other.importsOptional == importsOptional &&
        other.bodyDecls.equals(bodyDecls);
  }

  
}
