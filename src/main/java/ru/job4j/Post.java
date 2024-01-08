package ru.job4j;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    Integer id;
    String title;
    String link;
    String description;
    LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id + ", title='" + title + '\'' + ", link='" + link + '\'' + ", description='" + description + '\'' + ", created=" + created + '}';
    }
}
