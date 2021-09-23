package com.rangerv.spotifywebapi.model;

import java.util.List;

/**
 * @param <T> expected object that is paged
 */
public class PagingObject<T> {
    public String href;
    public List<T> items;
    public int limit;
    public String next;
    public int offset;
    public String previous;
    public int total;
}
