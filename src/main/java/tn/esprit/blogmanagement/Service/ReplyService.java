package tn.esprit.blogmanagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.blogmanagement.Entity.Reply;
import tn.esprit.blogmanagement.Repository.ReplyRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReplyService implements IReplyService {

    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    public Reply registerReply(Reply reply) {
        return replyRepository.save(reply);
    }

    @Override
    public Optional<Reply> getReplyById(Long id) {
        return replyRepository.findById(id);
    }

    @Override
    public List<Reply> getAllReplys() {
        return replyRepository.findAll();
    }

    @Override
    public Reply updateReply(Long id, Reply updatedReply) {
        return replyRepository.findById(id).map(reply -> {
            reply.setContent(updatedReply.getContent());
            reply.setUser(updatedReply.getUser()); // Ensure user is correctly set
            reply.setLastUpdatedAt(new Date());  // Update only the 'lastUpdatedAt' field
            reply.setIsEdited(true); // Mark the reply as edited
            return replyRepository.save(reply);
        }).orElseThrow(() -> new RuntimeException("Reply not found"));
    }

    @Override
    public void deleteReply(Long id) {
        replyRepository.deleteById(id);
    }

}
