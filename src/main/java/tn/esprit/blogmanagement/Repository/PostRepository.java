package tn.esprit.blogmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.blogmanagement.Entity.Post;


public interface PostRepository extends JpaRepository <Post, Long> {
}
