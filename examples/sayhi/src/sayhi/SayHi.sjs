module sayhi/SayHi

import org/sugarj/languages/JavaScript
import concretesyntax/JavaScript

sugar {
  lexical restrictions
    "sayhi" -/- [A-Za-z0-9\$\_]

  context-free syntax
    "sayhi" JavaScriptExpression -> JavaScriptAutoSemiStatement {cons("SayHi")}
    "sayhi" -> JavaScriptReservedWord

  desugarings
    desugar-sayhi

  rules
    desugar-sayhi :
      SayHi(e) -> |[ alert("Hi there" + :e) ]| 

}