package com.library.api.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Node of a Trie
 */
public class TrieNode<T> {

    private NavigableMap<Character, TrieNode<T>> children = new TreeMap<>();
    private List<T> content = new ArrayList<>();
    private Character key;
    private int index;

    /**
     * Constructor
     * @param key key of this node
     * @param index index of this node
     */
    public TrieNode(Character key, int index) {
        this.key = key;
        this.index = index;
    }

    public NavigableMap<Character, TrieNode<T>> getChildren() {
        return children;
    }

    public List<T> getContent() {
        return content;
    }

    public Character getKey() {
        return key;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object object) {
        if(object == this) {
            return true;
        }

        if (object == null || !getClass().isAssignableFrom(object.getClass())) {
            return false;
        }

        TrieNode<T> rhs = (TrieNode<T>) object;
        return new EqualsBuilder()
                .append(this.getKey(), rhs.getKey())
                .append(this.getIndex(), rhs.getIndex())
                .isEquals();
    }

    @Override
    public int hashCode()  {
        return new HashCodeBuilder()
                .append(this.getKey())
                .append(this.getIndex())
                .toHashCode();
    }
}
