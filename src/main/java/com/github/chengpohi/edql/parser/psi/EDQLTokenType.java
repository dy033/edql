package com.github.chengpohi.edql.parser.psi;

import com.github.chengpohi.edql.EDQLLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class EDQLTokenType extends IElementType {
    public EDQLTokenType(@NotNull @NonNls String debugName) {
        super(debugName, EDQLLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "EDQLTokenType." + super.toString();
    }
}
