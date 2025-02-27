package tn.esprit.blogmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.blogmanagement.Entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
