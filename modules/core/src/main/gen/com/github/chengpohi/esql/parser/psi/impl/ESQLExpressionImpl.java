// This is a generated file. Not intended for manual editing.
package com.github.chengpohi.esql.parser.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.chengpohi.esql.parser.psi.ESQLPsiTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.github.chengpohi.esql.parser.psi.*;

public abstract class ESQLExpressionImpl extends ASTWrapperPsiElement implements ESQLExpression {

  public ESQLExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ESQLVisitor visitor) {
    visitor.visitExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ESQLVisitor) accept((ESQLVisitor)visitor);
    else super.accept(visitor);
  }

}
