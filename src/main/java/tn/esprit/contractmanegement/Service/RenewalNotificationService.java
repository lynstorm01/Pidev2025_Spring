package tn.esprit.contractmanegement.Service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.contractmanegement.Entity.Contract;
import tn.esprit.contractmanegement.Repository.ContractRepository;

@Service
public class RenewalNotificationService {

    private final ContractRepository contractRepository;
    private final EmailService emailService;
    private final UserService userService;

    public RenewalNotificationService(ContractRepository contractRepository,
                                      EmailService emailService, UserService userService) {
        this.contractRepository = contractRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    // Runs daily at 1 AM (adjust the cron expression as needed)
    @Scheduled(cron = "0 0 1 * * ?")
    public void sendRenewalReminders() {
        LocalDate today = LocalDate.now();
        LocalDate fiveDaysFromNow = today.plusDays(5);

        // Find contracts ending between today and 5 days from now
        List<Contract> expiringContracts = contractRepository.findByEndDateBetween(today, fiveDaysFromNow);

        for (Contract contract : expiringContracts) {
            String clientEmail = "yessine.blanco@esprit.tn"; // Ensure this getter exists
            String contractNumber = contract.getContractNumber();

            String subject = "Contract Renewal Reminder";
            String emailBody = "Dear Client, your contract " + contractNumber +
                    " is set to expire in 5 days. Please renew your contract.";

            if (clientEmail != null && !clientEmail.isEmpty()) {
                emailService.sendEmail(clientEmail, subject, emailBody);
            }
        }
    }
}
