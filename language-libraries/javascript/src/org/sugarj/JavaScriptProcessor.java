package org.sugarj;

import static org.sugarj.common.ATermCommands.getApplicationSubterm;
import static org.sugarj.common.Log.log;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.lang.Context;
import org.strategoxt.stratego_gpp.parse_pptable_file_0_0;
import org.sugarj.common.ATermCommands;
import org.sugarj.common.Environment;
import org.sugarj.common.FileCommands;
import org.sugarj.common.Log;
import org.sugarj.common.StringCommands;
import org.sugarj.common.path.Path;
import org.sugarj.common.path.RelativePath;

public class JavaScriptProcessor extends AbstractBaseProcessor implements Serializable {

  private static final long serialVersionUID = -8431879767852508991L;

  private String moduleHeader;
  private List<String> imports = new LinkedList<String>();
  private List<String> body = new LinkedList<String>();

  private RelativePath sourceFile;
  private Path jsOutFile;

  private static final String MODULE_SEP = "/";
  private String decName;
  private String relNamespaceName;
    
  private IStrategoTerm pptable = null;
  private Path prettyPrint = null;

//  static {
//    log.setLoggingLevel(Log.ALWAYS);
//  }
  
  private Path getPrettyPrint() {
    if (prettyPrint == null)
      prettyPrint = getLanguage().ensureFile("org/sugarj/languages/JavaScript.pp");
    
    return prettyPrint;
  }
    
  @Override
  public String getGeneratedSource() {
    return moduleHeader + "\n"
         + StringCommands.printListSeparated(imports, "\n") + "\n"
         + StringCommands.printListSeparated(body, "\n");
  }

  @Override
  public Path getGeneratedSourceFile() {
    return jsOutFile;
  }

  @Override
  public List<String> processBaseDecl(IStrategoTerm toplevelDecl) throws IOException {
    if (getLanguage().isNamespaceDec(toplevelDecl)) {
      processNamespaceDecl(toplevelDecl);
      return Collections.emptyList();
    }

    body.add(prettyPrint(toplevelDecl));
    return Collections.emptyList();
  }

  
  private IStrategoTerm initializePrettyPrinter(Context ctx) {
    if (pptable == null) {
      IStrategoTerm pptable_file = ATermCommands.makeString(getPrettyPrint().getAbsolutePath());
      pptable = parse_pptable_file_0_0.instance.invoke(org.strategoxt.stratego_gpp.stratego_gpp.init(), pptable_file);
    }
    
    return pptable;
  }
  
  private String prettyPrint(IStrategoTerm term) throws IOException {
    IStrategoTerm ppTable = initializePrettyPrinter(interp.getCompiledContext());
    return ATermCommands.prettyPrint(ppTable, term, interp);
  }

  @Override
  public void init(RelativePath sourceFile, Environment environment) {
    this.sourceFile = sourceFile;
    jsOutFile = environment.createOutPath(FileCommands.dropExtension(sourceFile.getRelativePath()) + "." + getLanguage().getBinaryFileExtension());
    relNamespaceName = FileCommands.dropFilename(sourceFile.getRelativePath());
    decName = getRelativeModulePath(
                FileCommands.dropExtension(
                FileCommands.fileName(
                sourceFile.getRelativePath())));
  }

  @Override
  public String getNamespace() {
    return relNamespaceName;
  } 
  
  private void processNamespaceDecl(IStrategoTerm toplevelDecl) throws IOException {
    String modPath = checkModulePath(toplevelDecl, sourceFile);
    relNamespaceName = extractRelativeNamespace(modPath);
    decName = extractSugarName(modPath);
    log.log("The SDF / Stratego package name is '" + relNamespaceName + "'.", Log.DETAIL);
    
  }  
  
  @Override
  public JavaScriptLanguage getLanguage() {
    return JavaScriptLanguage.getInstance();
  }

  @Override
  public List<Path> compile(List<Path> sourceFiles, Path bin, List<Path> path) {
    // No compilation for JavaScript
    return Collections.emptyList();
  }
  
  @Override
  public String getModulePathOfImport(IStrategoTerm toplevelDecl) {
    String modulePath = extractModulePath(toplevelDecl);
    return getRelativeModulePath(modulePath);    
  }
  
  // Check that the declared module name matches the source relative path and file name
  private String checkModulePath(IStrategoTerm modDecl, RelativePath sourceFile) throws IOException {
    String declaredPath = extractModulePath(modDecl); 
    if (sourceFile != null) {
      String expectedPath = FileCommands.dropExtension(sourceFile.getRelativePath());
      if (!declaredPath.equals(expectedPath))
        throw new RuntimeException("The declared module '" + declaredPath + "'" + " does not match the expected package '" + expectedPath + "'.");
    }
    return declaredPath;
  }

  private String extractModulePath(IStrategoTerm modDecl) {
    // can't pretty print as 'import ...' is not part of JavaScript
    return stripQuotes(modDecl.getSubterm(0).toString());
    
  }
  
  private String stripQuotes(String quoted) {
    return (2 <= quoted.length()
                  && '\"' == quoted.charAt(0)
                  && '\"' == quoted.charAt(quoted.length() - 1))
             ? quoted.substring(1, quoted.length()-1)
             : quoted;        	
  }
  
  // The path up to the last "/"
  private String extractRelativeNamespace(String modulePath) {
    int pos = modulePath.lastIndexOf(MODULE_SEP);
    return (pos != -1)
             ? modulePath.substring(0, pos)
             : "";
  }
  
  // The end of the path, after the last "/"
  private String extractSugarName(String modulePath) {
    int pos = modulePath.lastIndexOf(MODULE_SEP);
    return (pos != -1)
             ? modulePath.substring(pos + 1)
             : modulePath;
  }
  
  private String getRelativeModulePath(String moduleName) {
    return moduleName.replace(MODULE_SEP, Environment.sep);
  }
  
  @Override
  public void processModuleImport(IStrategoTerm toplevelDecl) {
    // All imports are pure sugar so no need to add to source file  
  }

  @Override
  public String getExtensionName(IStrategoTerm decl) throws IOException {
        return decName;
  }

  @Override
  public boolean isModuleExternallyResolvable(String relModulePath) {
      return false;
  }

  @Override
  public IStrategoTerm getExtensionBody(IStrategoTerm decl) {
    IStrategoTerm sdec = getApplicationSubterm(decl, "SugarDec", 0);
    return getApplicationSubterm(sdec, "SugarBody", 0);
  }
}