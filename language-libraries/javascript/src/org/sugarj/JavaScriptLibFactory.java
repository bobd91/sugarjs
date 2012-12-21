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

}