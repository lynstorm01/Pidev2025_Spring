package tn.esprit.contractmanegement.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

    @Scheduled(cron = "0 0 7 * * ?") // runs every day at 7 AM, for example
    public void sendRenewalReminders() {
        LocalDate fiveDaysFromNow = LocalDate.now().plusDays(5);
        Date endDate = Date.from(fiveDaysFromNow.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Query contracts that end exactly on `fiveDaysFromNow`
        List<Contract> expiringInFiveDays = contractRepository.findByEndDate(endDate);
        for (Contract contract : expiringInFiveDays) {
            // The client’s email - adapt to your actual data
            String clientEmail = "yessine.blanco@esprit.tn";
            String contractNumber = contract.getContractNumber();

            String subject = "Contract Renewal Reminder";
            String emailBody = "Dear Client, your contract " + contractNumber +
                    " will expire in exactly 5 days. Please renew soon!";

            if (clientEmail != null && !clientEmail.isEmpty()) {
                emailService.sendEmail(clientEmail, subject, emailBody);
            }
        }
    }

}
