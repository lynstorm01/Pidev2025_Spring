package tn.esprit.contractmanegement.Controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.contractmanegement.Service.TwilioSmsService;

@RestController
@RequestMapping("/sms")
public class TwilioSmsController {

    private final TwilioSmsService twilioSmsService;

    public TwilioSmsController(TwilioSmsService twilioSmsService) {
        this.twilioSmsService = twilioSmsService;
    }

    @PostMapping("/send")
    public String sendSms(@RequestParam String to, @RequestParam String message) {
        twilioSmsService.sendSms(to, message);
        return "SMS envoyé à " + to;
    }
}

