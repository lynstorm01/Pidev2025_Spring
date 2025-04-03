package tn.esprit.blogmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.blogmanagement.Entity.Post;

import java.util.List;


public interface PostRepository extends JpaRepository <Post, Long> {
    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(concat('%', :contentSample, '%'))")
    List<Post> findByContentContaining(@Param("contentSample") String contentSample);
}
