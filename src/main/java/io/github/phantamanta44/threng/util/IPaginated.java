package io.github.phantamanta44.threng.util;

public interface IPaginated {

    int getPageCount();

    int getCurrentPage();

    void setPage(int pageNum);

}
