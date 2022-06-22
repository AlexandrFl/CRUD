package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private ConcurrentHashMap<Long, Post> list = new ConcurrentHashMap<>();
    private AtomicLong idCount = new AtomicLong(1);

    public List<Post> all() {
        return new LinkedList<>(list.values());
    }

    public Optional<Post> getById(long id) {
        if (!list.isEmpty()) {
            if (list.containsKey(id)) {
                return Optional.of(list.get(id));
            }
        }
        return Optional.empty();
    }


    public Post save(Post post) {
        if (post.getId() == 0) {
            while (list.containsKey(idCount.get())) {
                idCount.incrementAndGet();
            }
            post.setId(idCount.get());
            list.put(post.getId(), post);
            return post;
        }
        if (list.containsKey(post.getId())) {
            Post changingPost = list.get(post.getId());
            changingPost.setContent(post.getContent());
            return changingPost;
        }
        list.put(post.getId(), post);
        return post;
    }

    public void removeById(long id) {
        if (list.containsKey(id)) {
            list.remove(id);
        } else {
            throw new NotFoundException();
        }
    }
}
