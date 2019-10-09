package com.library.api.service;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Service to sort a collection based on a trie
 */
public interface TrieSortingService {

    /**
     * Returns a sorted list by the given property
     * @param collection collection to sort
     * @param toStringFunction function that provides the string
     * @return new sorted list
     */
    <T> List<T> sort(Collection<T> collection, Function<T, String> toStringFunction);

}
