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

public class ESQLSignedNumberImpl extends ASTWrapperPsiElement implements ESQLSignedNumber {

  public ESQLSignedNumberImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ESQLVisitor visitor) {
    visitor.visitSignedNumber(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ESQLVisitor) accept((ESQLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getNumericLiteral() {
    return findNotNullChildByType(NUMERIC_LITERAL);
  }

}
