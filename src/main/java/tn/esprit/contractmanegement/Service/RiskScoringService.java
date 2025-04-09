package tn.esprit.contractmanegement.Service;


import org.springframework.stereotype.Service;
import tn.esprit.contractmanegement.Entity.Devis;
import tn.esprit.contractmanegement.Entity.TypeAssurance;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
public class RiskScoringService {

    public BigDecimal calculateRiskScore(Devis devis) {
        BigDecimal riskScore = BigDecimal.ZERO;

        if (devis.getTypeAssurance() == TypeAssurance.HABITATION) {
            riskScore = riskScore.add(new BigDecimal("10"));
        } else if (devis.getTypeAssurance() == TypeAssurance.VOYAGE) {
            riskScore = riskScore.add(new BigDecimal("5"));
        }

        long daysCovered = ChronoUnit.DAYS.between(devis.getDateDebut(), devis.getDateFin());
        if (daysCovered < 30) {
            riskScore = riskScore.add(new BigDecimal("15"));
        } else if (daysCovered > 30) {
            riskScore = riskScore.add(new BigDecimal("5"));
        }

        if (devis.getUser() != null && devis.getUser().getRiskScore() != null) {
            riskScore = riskScore.add(devis.getUser().getRiskScore().multiply(new BigDecimal("0.5")));
        }

        return riskScore;
    }
}
