package com.library.api.service.impl;

import com.library.api.domain.TrieNode;
import com.library.api.service.TrieSortingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Implementation of {@link TrieSortingService}
 */
@Service
public class TrieSortingServiceImpl implements TrieSortingService {

    @Override
    public <T> List<T> sort(Collection<T> collection, Function<T, String> toStringFunction) {
        TrieNode<T> root = new TrieNode<>(null, -1);

        collection.forEach(element -> {
            String key = toStringFunction.apply(element).toLowerCase();
            insert(element, key, 0, root);
        });

        List<T> sortedList = new ArrayList<>();
        addNode(sortedList, root);
        return sortedList;
    }

    /**
     * Adds the node content to the list and all its children
     * @param sortedList content will be added to this list
     * @param node the trie node
     */
    private <T> void addNode(List<T> sortedList, TrieNode<T> node) {
        sortedList.addAll(node.getContent());
        for (TrieNode<T> childNode : node.getChildren().values()) {
            addNode(sortedList, childNode);
        }
    }

    /**
     * Inserts an element into this node
     * @param element element
     * @param key key of the element
     * @param index index of the next level
     * @param node current node
     */
    private <T> void insert(T element, String key, int index, TrieNode<T> node) {
        if(index == key.length()) {
            //Current node matches the key exactly
            node.getContent().add(element);
        } else {
            char charKey = key.charAt(index);
            TrieNode<T> relevantChildNode = node.getChildren().computeIfAbsent(charKey, mapKey -> new TrieNode<>(mapKey, index));
            insert(element, key, index+1, relevantChildNode);
        }


    }
}
