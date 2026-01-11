package com.intellij.psi.stubs;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Experimental;

public interface Stub {
    Stub getParentStub();

    @NotNull List<? extends Stub> getChildrenStubs();

    /** @deprecated */
    @Deprecated
    ObjectStubSerializer<?, ? extends Stub> getStubType();

    @Experimental
    default ObjectStubSerializer<?, ? extends Stub> getStubSerializer() {
        return this.getStubType();
    }
}
