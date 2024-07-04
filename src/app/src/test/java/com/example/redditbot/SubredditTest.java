package com.example.redditbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.redditbot.DataHolders.Subreddit;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SubredditTest {
    @Test
    void testNoArgConstructor() {
        Subreddit subreddit = new Subreddit();
        assertEquals("N/A", subreddit.getName());
        assertEquals(1, subreddit.getMaxPosts());
        assertTrue(subreddit.getTerms().isEmpty());
    }

    @Test
    void testParameterizedConstructor() {
        String name = "subreddit-name";
        Integer maxPosts = 10;
        ArrayList<String> terms = new ArrayList<>();
        terms.add("term1");
        terms.add("term2");

        Subreddit subreddit = new Subreddit(name, maxPosts, terms);

        assertEquals(name, subreddit.getName());
        assertEquals(maxPosts, subreddit.getMaxPosts());
        assertEquals(terms, subreddit.getTerms());
    }

    @Test
    void testGetters() {
        String name = "subreddit-name";
        Integer maxPosts = 10;
        ArrayList<String> terms = new ArrayList<>();
        terms.add("term1");
        terms.add("term2");

        Subreddit subreddit = new Subreddit(name, maxPosts, terms);

        assertEquals(name, subreddit.getName());
        assertEquals(maxPosts, subreddit.getMaxPosts());
        assertEquals(terms, subreddit.getTerms());
    }

    @Test
    void testSetters() {
        Subreddit subreddit = new Subreddit();

        String name = "subreddit-name";
        Integer maxPosts = 10;
        ArrayList<String> terms = new ArrayList<>();
        terms.add("term1");
        terms.add("term2");

        subreddit.setName(name);
        subreddit.setMaxPosts(maxPosts);
        subreddit.setTerms(terms);

        assertEquals(name, subreddit.getName());
        assertEquals(maxPosts, subreddit.getMaxPosts());
        assertEquals(terms, subreddit.getTerms());
    }

    @Test
    void testGetNameDefault() {
        Subreddit subreddit = new Subreddit();
        assertEquals("N/A", subreddit.getName());
    }

    @Test
    void testGetMaxPostsDefault() {
        Subreddit subreddit = new Subreddit();
        assertEquals(1, subreddit.getMaxPosts());
    }

    @Test
    void testGetTermsDefault() {
        Subreddit subreddit = new Subreddit();
        assertTrue(subreddit.getTerms().isEmpty());
    }
}
