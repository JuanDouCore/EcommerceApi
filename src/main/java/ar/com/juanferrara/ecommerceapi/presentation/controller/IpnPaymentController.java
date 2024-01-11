package ar.com.juanferrara.ecommerceapi.presentation.controller;

import ar.com.juanferrara.ecommerceapi.business.service.impl.MercadoPagoService;
import com.amazonaws.Response;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ipn")
@Hidden
public class IpnPaymentController {

    Logger logger = LoggerFactory.getLogger(IpnPaymentController.class);

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping("mercadopago/notify")
    public ResponseEntity<String> mercadoPagoNotificationPayment(@RequestBody Map<String, Object> values) {
        if(mercadoPagoService.processNotificationWebhook(values))
            return ResponseEntity.ok("ACCEPTED");
        else
            return new ResponseEntity<>(  "PAYMENT REQUIERED", HttpStatus.PAYMENT_REQUIRED);
    }
}
