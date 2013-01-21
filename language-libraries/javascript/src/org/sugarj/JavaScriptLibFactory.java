package org.sugarj;

public class JavaScriptLibFactory extends LanguageLibFactory {

  private JavaScriptLibFactory() { }
  
  private static JavaScriptLibFactory instance = new JavaScriptLibFactory();
  
  public static JavaScriptLibFactory getInstance() {
    return instance;
  }
  
  @Override
  public LanguageLib createLanguageLibrary() {
    return new JavaScriptLib();
  }

  @Override
  public String getGeneratedFileExtension() {
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

}