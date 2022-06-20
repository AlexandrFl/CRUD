package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {
    private List<Post> list = new LinkedList<>();
    private long idCount = 1;

    public List<Post> all() {
        return list;
    }

    public Optional<Post> getById(long id) {
        if (!list.isEmpty()) {
            for (Post post : list) {
                if (post.getId() == id) {
                    return Optional.of(post);
                }
            }
        }
        return Optional.empty();
    }


    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(idCount++);
            list.add(post);
            return post;
        }
        for (Post posts : list) {
            if (posts.getId() == post.getId()) {
                posts.setId(post.getId());
                posts.setContent(post.getContent());
                return posts;
            }
        }
        post.setId(idCount++);
        list.add(post);
        return post;
    }

    public void removeById(long id) {
        list.removeIf(posts -> posts.getId() == id);
    }
}
