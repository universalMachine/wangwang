package com.wang.crawler.wordpress;


import java.net.URLEncoder;
import java.util.*;
import java.util.function.Consumer;

public class Post {

    private Date date;
    private String title;
    private Integer author;
    private String featured_image;
    private String status = "publish";
    private String content;
    private Optional<Set<Integer>> categories  ;
    private Optional<Set<String>> tags;
    private final HashMap<String,Integer> categoryHash = new HashMap<String,Integer>(){{put("beauty asian girl",2);put("hot asian girl",3);}};

    public Post() {
        categories = Optional.of(new HashSet<>(Arrays.asList(categoryHash.get("beauty asian girl"),categoryHash.get("hot asian girl"))));
        tags = Optional.of(new HashSet<>(Arrays.asList("beauty","asian","hot","girl","woman")));
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Optional<Set<Integer>> getCategories() {
        return categories;
    }

    public void setCategories(Optional<Set<Integer>> categories) {
        this.categories = categories;
    }

    public void addCategories(String category) {
        this.categories.orElse(new HashSet<>()).add(categoryHash.get(category));
    }


    public Optional<Set<String>> getTags() {
        return tags;
    }

    public void setTags(Optional<Set<String>> tags) {
        this.tags = tags;
    }

    public void addTags(String tag) {
        this.tags.orElse(new HashSet<String>()).add(tag);
    }


    public String toFormData() throws Exception{
        String data = URLEncoder.encode("data", "UTF-8")
                + "=" + URLEncoder.encode(date.toString(), "UTF-8");

        data += "&" + URLEncoder.encode("title", "UTF-8") + "="
                + URLEncoder.encode(title, "UTF-8");

        data += "&" + URLEncoder.encode("content", "UTF-8") + "="
                + URLEncoder.encode(content, "UTF-8");

       if(categories.isPresent()){

           Iterator<Integer> iterator = categories.get().iterator();
           while(iterator.hasNext()){
               data += "&" + URLEncoder.encode("categories[]", "UTF-8") + "="
                       + URLEncoder.encode(iterator.next().toString(), "UTF-8");
           } }

       /* if(tags.isPresent()){

            Iterator<String> iterator = tags.get().iterator();
            while(iterator.hasNext()){
                data += "&" + URLEncoder.encode("tags[]", "UTF-8") + "="
                        + URLEncoder.encode(iterator.next(), "UTF-8");
            }}*/








  /*      data += "&" + URLEncoder.encode("author", "UTF-8") + "="
                + URLEncoder.encode(author.toString(), "UTF-8");
        data += "&" + URLEncoder.encode("featured_image", "UTF-8") + "="
                + URLEncoder.encode(featured_image, "UTF-8");*/
        data += "&" + URLEncoder.encode("status", "UTF-8") + "="
                + URLEncoder.encode(status, "UTF-8");
        return data;
    }
}
