package io.github.phantamanta44.threng.util;

import javax.annotation.Nullable;

public interface ISearchable {

    void updateSearchQuery(@Nullable String query);

    boolean matchesQuery();

}
