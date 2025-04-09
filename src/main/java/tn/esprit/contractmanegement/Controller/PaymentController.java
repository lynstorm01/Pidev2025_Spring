package tn.esprit.contractmanegement.Controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping("/create-payment-intent")
    public Map<String, Object> createPaymentIntent(@RequestBody Map<String, Object> paymentData) {
        try {

            // Récupérer le montant (en centimes)
            long amount = ((Number) paymentData.get("amount")).longValue();
            long test = ((Number) paymentData.get("amount")).longValue();

            // Créer un PaymentIntent
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(amount)
                            .setCurrency("eur") // Changer selon le pays
                            .addPaymentMethodType("card")
                            .build();

            PaymentIntent intent = PaymentIntent.create(params);

            // Retourner le clientSecret au frontend
            Map<String, Object> response = new HashMap<>();
            response.put("clientSecret", intent.getClientSecret());
            return response;
        } catch (StripeException e) {
            throw new RuntimeException("Erreur Stripe : " + e.getMessage());
        }
    }
}
