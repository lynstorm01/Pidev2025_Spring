package com.assurance.demo.web.controller;

import com.assurance.demo.web.service.TwilioSmsService;
import org.springframework.web.bind.annotation.*;

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

