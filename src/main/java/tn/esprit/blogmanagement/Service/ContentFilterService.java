package tn.esprit.blogmanagement.Service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.blogmanagement.Entity.Post;
import tn.esprit.blogmanagement.Repository.PostRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContentFilterService {

    private static final Set<String> BAD_WORDS = Set.of(
            "badword1", "badword2", "badword3", "hate", "violence"
    );

    private static final Set<String> INSURANCE_KEYWORDS = Set.of(
            "insurance", "policy", "premium", "coverage", "claim",
            "insure", "risk", "liability", "protection", "benefit"
    );

    @Autowired
    private PostRepository postRepository;

    public ContentFilterResult filterContent(String content, String title) {
        ContentFilterResult result = new ContentFilterResult();

        // Check for bad words
        Set<String> foundBadWords = new HashSet<>();
        String lowerContent = content.toLowerCase();

        for (String word : BAD_WORDS) {
            if (lowerContent.contains(word.toLowerCase())) {
                foundBadWords.add(word);
            }
        }

        // Check for insurance relevance
        boolean isInsuranceRelated = false;
        for (String keyword : INSURANCE_KEYWORDS) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                isInsuranceRelated = true;
                break;
            }
        }

        // Enhanced duplicate content check
        String contentSample = content.length() > 50
                ? content.substring(0, 50)
                : content;

        List<Post> similarPosts = postRepository.findByContentContaining(contentSample);
        int duplicateThreshold = 2;
        boolean isPotentialDuplicate = similarPosts.size() >= duplicateThreshold;

        result.setContainsBadWords(!foundBadWords.isEmpty());
        result.setBadWords(foundBadWords);
        result.setInsuranceRelated(isInsuranceRelated);
        result.setDuplicate(isPotentialDuplicate);
        result.setDuplicateCount(similarPosts.size());
        result.setSimilarPosts(similarPosts.stream()
                .limit(3)
                .collect(Collectors.toList()));

        return result;
    }

    @Data
    public static class ContentFilterResult {
        private boolean containsBadWords;
        private Set<String> badWords;
        private boolean insuranceRelated;
        private boolean duplicate;
        private int duplicateCount;
        private List<Post> similarPosts;
    }
}
