package ru.minilan.vocabularytrainer;

public class WordCard {
    private long id;
    private String language1;
    private String language2;
    private String word1;
    private String word2;
    private long Learn_index;

    public long getId() {
        return id;
    }

    public String getLanguage1() {
        return language1;
    }

    public String getLanguage2() {
        return language2;
    }

    public String getWord1() {
        return word1;
    }

    public String getWord2() {
        return word2;
    }

    public long getLearn_index() {
        return Learn_index;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLanguage1(String language1) {
        this.language1 = language1;
    }

    public void setLanguage2(String language2) {
        this.language2 = language2;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public void setLearn_index(long learn_index) {
        Learn_index = learn_index;
    }
}
