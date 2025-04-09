package tn.esprit.contractmanegement.Service;


import org.springframework.stereotype.Service;
import tn.esprit.contractmanegement.Entity.Devis;

import java.math.BigDecimal;

@Service
public class PricingAdjustmentService {

    public BigDecimal adjustPremium(Devis devis) {
        BigDecimal basePremium = devis.getMontantTotal();
        BigDecimal userRiskScore = BigDecimal.ZERO;

        if (devis.getUser() != null && devis.getUser().getRiskScore() != null) {
            userRiskScore = devis.getUser().getRiskScore();
        }

        BigDecimal adjustedPremium = basePremium;
        BigDecimal highRiskThreshold = new BigDecimal("15");
        BigDecimal lowRiskThreshold = new BigDecimal("5");

        if (userRiskScore.compareTo(highRiskThreshold) > 0) {
            adjustedPremium = basePremium.multiply(new BigDecimal("1.08"));
        } else if (userRiskScore.compareTo(lowRiskThreshold) < 0) {
            adjustedPremium = basePremium.multiply(new BigDecimal("0.92"));
        }

        return adjustedPremium.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
