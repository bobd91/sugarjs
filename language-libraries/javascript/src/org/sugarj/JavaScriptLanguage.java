package org.sugarj;

import static org.sugarj.common.ATermCommands.isApplication;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.sugarj.common.path.Path;

public class JavaScriptLanguage extends AbstractBaseLanguage {

  private JavaScriptLanguage() { }
  
  private static JavaScriptLanguage instance = new JavaScriptLanguage();
  
  public static JavaScriptLanguage getInstance() {
    return instance;
  }
  
  @Override
  public AbstractBaseProcessor createNewProcessor() {
    return new JavaScriptProcessor();
  }

  @Override
  public String getBinaryFileExtension() {
    return null;
  }

  @Override
  public String getBaseFileExtension() {
    return "js";
  }

  @Override
  public String getSugarFileExtension() {
    return "sjs";
  }

  @Override
  public String getLanguageName() {
    return "JavaScript";
  }

  @Override
  public String getVersion() {
    return "javascript-0.1";
  }

  @Override
  public List<Path> getPackagedGrammars() {
    List<Path> grammars = new LinkedList<Path>(super.getPackagedGrammars());
    grammars.add(ensureFile("org/sugarj/languages/SugarJS.def"));
    grammars.add(ensureFile("org/sugarj/languages/JavaScript.def"));
    return Collections.unmodifiableList(grammars);
  }

  @Override
  public Path getInitEditor() {
    return ensureFile("org/sugarj/javascript/init/initEditor.serv");
  }

  @Override
  public String getInitEditorModuleName() {
    return "org/sugarj/javascript/init/initEditor";
  }

  @Override
  public Path getInitGrammar() {
    return ensureFile("org/sugarj/javascript/init/initGrammar.sdf");
  }

  @Override
  public String getInitGrammarModuleName() {
    return "org/sugarj/javascript/init/initGrammar";
  }

  @Override
  public Path getInitTrans() {
    return ensureFile("org/sugarj/javascript/init/InitTrans.str");
  }

  @Override
  public String getInitTransModuleName() {
    return "org/sugarj/javascript/init/InitTrans";
  }

  @Override
  public boolean isExtensionDecl(IStrategoTerm decl) {
    return isApplication(decl, "SugarDec");           
  }

  @Override
  public boolean isImportDecl(IStrategoTerm decl) {
    return isApplication(decl, "SugarImportDec");
  }

  @Override
  public boolean isBaseDecl(IStrategoTerm decl) {
  return isApplication(decl, "Program") || isNamespaceDec(decl);        
  }

  public boolean isNamespaceDec(IStrategoTerm decl) {
    return isApplication(decl, "SugarModuleDec");
  }

  @Override
  public boolean isPlainDecl(IStrategoTerm decl) {
    return isApplication(decl, "PlainDec");        
  }

}