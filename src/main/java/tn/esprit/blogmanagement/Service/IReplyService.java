package tn.esprit.blogmanagement.Service;

import tn.esprit.blogmanagement.Entity.Reply;

import java.util.List;
import java.util.Optional;

public interface IReplyService {
    Reply registerReply(Reply post);
    Optional<Reply> getReplyById(Long id);
    List<Reply> getAllReplys();
    Reply updateReply(Long id,Reply updatedReply);
    void deleteReply(Long id);
}
