package com.assurance.demo.web.service;

import com.assurance.demo.web.dto.DevisResponseDTO;
import com.assurance.demo.web.model.Devis;
import com.assurance.demo.web.model.DevisVoyage;
import com.assurance.demo.web.model.Paiement;
import com.assurance.demo.web.model.User;
import com.assurance.demo.web.repository.DevisRepository;
import com.assurance.demo.web.repository.PaiementRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    // Créer un devis
    @Override
    public DevisResponseDTO  createDevis(Devis devis) throws MessagingException {
        User staticUser = new User();
        staticUser.setId(100L);
        staticUser.setUsername("staticUser");
        staticUser.setFirstName("Test");
        staticUser.setLastName("User");
        staticUser.setPassword("password");
        staticUser.setDateOfRegistration(new Date());
        staticUser.setEmail("staticuser@example.com");
        staticUser.setPhoneNumber("1234567890");

        devis.setUser(staticUser);
        Devis savedDevis = devisRepository.save(devis);

        // Now, send an email to the client or an admin after adding the devis
        String subject = "New Devis "+ savedDevis.getId() +"Created";
        String text = generateHtmlEmail(devis);

        // Send email (you can replace this with the client's email or an admin email)
        String clientEmail = savedDevis.getEmailClient();
        emailService.sendEmail(clientEmail, subject, text);
        return new DevisResponseDTO(savedDevis.getId(), "Devis with ID " + savedDevis.getId() + " has been added and email sent.");


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
            User staticUser = new User();
            staticUser.setId(100L);
            staticUser.setUsername("staticUser");
            staticUser.setFirstName("Test");
            staticUser.setLastName("User");
            staticUser.setPassword("password");
            staticUser.setDateOfRegistration(new Date());
            staticUser.setEmail("staticuser@example.com");
            staticUser.setPhoneNumber("1234567890");

            devisToUpdate.setUser(staticUser);
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



}
