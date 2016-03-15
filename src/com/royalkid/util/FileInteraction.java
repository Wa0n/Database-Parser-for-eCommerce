package com.royalkid.util;

/**
 * Interface uses to maintain separate operations with data and output file.
 *
 * @author Vladimir Pantasenko
 * @since JDK1.8
 */
public interface FileInteraction {
    /**
     * Creates a categories markup in the output file. Adds e-shop name.
     */
    void addCategories();

    /**
     * Creates a item list markup in the output file.
     */
    void addItemlist();

    /**
     * Creates a closing markup in the output file.
     */
    void addEndings();

    /**
     * Creates a initial markup in the output file. Adds e-shop name.
     */
    void addHeadings();
}
