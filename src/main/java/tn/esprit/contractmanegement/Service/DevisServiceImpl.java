package tn.esprit.contractmanegement.Service;


import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.contractmanegement.Entity.*;
import tn.esprit.contractmanegement.Repository.DevisRepository;
import tn.esprit.contractmanegement.Repository.PaiementRepository;
import tn.esprit.contractmanegement.Repository.UserRepository;
import tn.esprit.contractmanegement.dto.DevisResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DevisServiceImpl  implements  DevisService {
    @Autowired
    private DevisRepository devisRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private RiskScoringService riskScoringService;

    @Autowired
    private PricingAdjustmentService pricingAdjustmentService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    // Créer un devis
    @Override
    public DevisResponseDTO createDevis(Devis devis) throws MessagingException {

        User currentUser = userService.getCurrentUser();
        devis.setUser(currentUser);
        // Save the devis.
        Devis savedDevis = devisRepository.save(devis);

        // If a user is associated with the devis, update their risk score.
        if (devis.getUser() != null) {
            // Calculate the composite risk score based on devis details.
            BigDecimal updatedRiskScore = riskScoringService.calculateRiskScore(devis);
            // Update the user's risk score.
            devis.getUser().setRiskScore(updatedRiskScore);
            // Persist the risk score update.
            userRepository.save(devis.getUser());
        }

        // Adjust the premium based on the user's risk score.
        BigDecimal adjustedPremium = pricingAdjustmentService.adjustPremium(devis);
        devis.setMontantTotal(adjustedPremium);
        // Optionally, update the devis record with the new montantTotal.
        devisRepository.save(devis);

        // Prepare email details.
        String subject = "New Devis " + savedDevis.getId() + " Created";
        String text = generateHtmlEmail(devis);
        String clientEmail = savedDevis.getEmailClient();

        // Send email to the client.
        emailService.sendEmail(clientEmail, subject, text);

        return new DevisResponseDTO(savedDevis.getId(), "Devis with ID "
                + savedDevis.getId() + " has been added and email sent.");
    }
    // Generate HTML content for the email
    // Generate HTML content for the email
    private String generateHtmlEmail(Devis devis) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                "table { width: 100%; max-width: 600px; margin: auto; padding: 20px; border-collapse: collapse; background-color: #ffffff; }" +
                "td { padding: 10px; border: 1px solid #ddd; text-align: left; }" +
                "th { background-color: #4CAF50; color: white; text-align: left; padding: 10px; }" +
                "h1 { color: #333; }" +
                "p { color: #555; font-size: 16px; }" +
                "a { color: #ffffff; text-decoration: none; background-color: #4CAF50; padding: 10px 20px; border-radius: 5px; display: inline-block; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<table>" +
                "<tr><th colspan='2'><h1>Your Devis Details</h1></th></tr>" +
                "<tr><td colspan='2'><p>Dear " + devis.getNomClient() + ",</p>" +
                "<p>Thank you for choosing our service! Below are the details of your recently created Devis:</p></td></tr>" +
                "<tr><td><strong>Type of Assurance:</strong></td><td>" + devis.getTypeAssurance() + "</td></tr>" +
                "<tr><td><strong>Status:</strong></td><td>" + devis.getStatut() + "</td></tr>" +  // Displaying StatutDevis
                "<tr><td><strong>Start Date:</strong></td><td>" + devis.getDateDebut() + "</td></tr>" +
                "<tr><td><strong>End Date:</strong></td><td>" + devis.getDateFin() + "</td></tr>" +
                "</table>" +
                "<p style='text-align: center;'>Click the button below to view more details:</p>" +
                "<p style='text-align: center;'><a href='http://yourwebsite.com'>View Your Devis</a></p>" +
                "<p style='text-align: center;'>If you have any questions, feel free to contact us.</p>" +
                "</body>" +
                "</html>";
    }




    @Override
    public Optional<Devis> getDevisById(Long id) {
      return  devisRepository.findById(id);

    }

    @Override
    public List<Devis> getAllDevis() {
        // Query to get all Devis entities from the database
        List<Devis> devisList = devisRepository.findAll();
        return devisList;
    }

    @Override
    public DevisResponseDTO updateDevis(Long id, Devis devis) {
        Optional<Devis> devisToUpdateOptional = devisRepository.findById(id);
        if (devisToUpdateOptional.isPresent()) {
            Devis devisToUpdate = devisToUpdateOptional.get();

            BeanUtils.copyProperties(devis, devisToUpdate, "id");
            User currentUser = userService.getCurrentUser();
            devis.setUser(currentUser);

            devisRepository.save(devisToUpdate);
            return new DevisResponseDTO(devisToUpdate.getId(), "Devis with ID " + devisToUpdate.getId() + " updated successfully.");
        } else {
            return new DevisResponseDTO(id, "Devis with ID " + id + " not found.");
        }
    }

    @Override
    public DevisResponseDTO deleteDevis(Long id) {
        Optional<Devis> devisToDeleteOptional = devisRepository.findById(id);
        if (devisToDeleteOptional.isPresent()) {
            Devis devisToDelete = devisToDeleteOptional.get();
            for (Paiement paiement : devisToDelete.getPaiements()) {
                paiementRepository.delete(paiement);  // Ensure paiement is deleted
            }
            devisRepository.delete(devisToDelete);
            return new DevisResponseDTO(id, "Devis with ID " + id + " has been deleted successfully.");
        } else {
            return new DevisResponseDTO(id, "Devis with ID " + id + " not found.");
        }
    }


    public Devis signerDevis(Long devisId, byte[] signature) {
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new RuntimeException("Devis non trouvé"));

        if (devis.isSigne()) {
            throw new RuntimeException("Le devis est déjà signé et ne peut plus être modifié.");
        }

        devis.setSignature(signature);
        devis.setSigne(true);
        return devisRepository.save(devis);
    }



    // Cette méthode est spécifique aux devis voyage et calcul la prime
    public void calculerPrimeDevisVoyage(DevisVoyage devisVoyage) {
        BigDecimal primeBase = new BigDecimal(100); // Exemple de prime de base

        if (devisVoyage.getTrancheAge().equals("18-30")) {
            primeBase = primeBase.multiply(new BigDecimal(1.2));
        }

        // Calcul de la prime selon la destination et d'autres critères
        if (devisVoyage.getDestination().equals("europe")) {
            primeBase = primeBase.multiply(new BigDecimal(1.3));
        }

        devisVoyage.setMontantTotal(primeBase);
    }


    @Scheduled(cron = "0 0 12 * * ?")
    public void checkDevisExpiry() throws MessagingException {
        LocalDate now = LocalDate.now();
        List<Devis> allDevis = devisRepository.findAll();

        for (Devis devis : allDevis) {
            if (devis.getDateFin() != null && devis.getDateFin().isBefore(now.plusDays(3)) && devis.getDateFin().isAfter(now)) {
                System.out.println("this is the expired devis " +devis);
                emailService.sendEmail(devis.getEmailClient(), "Reminder: Your insurance is about to expire", "Your insurance with ID " + devis.getId() + " is expiring soon on " + devis.getDateFin());
            }
        }
    }
    @Scheduled(cron = "0 0 12 * * ?")
    @Transactional
    public void processExpiringDevis() throws MessagingException {
        LocalDate today = LocalDate.now();
        List<Devis> allDevis = devisRepository.findAll();

        for (Devis devis : allDevis) {
            if (devis.getDateFin() != null && devis.getDateFin().isEqual(today) && devis.getStatut() != StatutDevis.EXPIRE) {
                devis.setStatut(StatutDevis.EXPIRE);
                devisRepository.save(devis);
                System.out.println("devis " +devis + "changed status to EXPIRE");
                emailService.sendEmail(devis.getEmailClient(), "Your Insurance Has Expired", "Your insurance with ID " + devis.getId() + " has expired today. Please contact us for renewal.");
            }
        }
    }
}
