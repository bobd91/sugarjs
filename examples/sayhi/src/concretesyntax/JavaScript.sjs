module concretesyntax/JavaScript

import org/sugarj/languages/JavaScript
import org/sugarj/languages/Stratego
import concretesyntax/MetaExplode

sugar {
  context-free syntax
    "|[" JavaScriptAutoSemiStatement "]|" -> StrategoTerm {cons("ToMetaExpr")}

    ":" StrategoTerm  -> JavaScriptExpression {cons("FromMetaExpr")}
    ":*" StrategoTerm -> {JavaScriptExpression ","}+ {cons("FromMetaExpr")}
    ":*" StrategoTerm -> {JavaScriptVarDeclaration ","}+ {cons("FromMetaExpr")}
}