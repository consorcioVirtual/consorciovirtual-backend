package ar.edu.unsam.consorciovirtual.controller;


import ar.edu.unsam.consorciovirtual.service.ExpensaDeDepartamentoService;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.PaymentMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.mercadopago.MercadoPago;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class CheckoutRestController {

    private final ExpensaDeDepartamentoService expensaDeDepartamentoService;

    @GetMapping("/checkout")
    public String assignPreference(@RequestParam Long expensaId) throws MPException {
        MercadoPago.SDK.setAccessToken("TEST-3185770547101963-051220-0721d6ba1b67d0dc39a596ef1ba248ea-93142664");

        var expensa = expensaDeDepartamentoService.buscarPorId(expensaId);
        Preference preference = new Preference();

        Item item = new Item();
        item.setTitle("Expensa de departamento " + expensa.getUnidad() + " correspondiente a " + expensa.getPeriodo())
                .setQuantity(1)
                .setUnitPrice(expensa.getMontoAPagar().floatValue())
                .setCurrencyId("ARS");
        preference.appendItem(item);

        BackUrls backUrls = new BackUrls(
                "localhost:3000/payment/success/"+expensaId,
                "localhost:3000/expensa/"+expensaId,
                "localhost:3000/");

        PaymentMethods paymentMethods = new PaymentMethods();
        paymentMethods.setInstallments(1);

        preference.setPaymentMethods(paymentMethods);
        preference.setBackUrls(backUrls);
        preference.save();
        return preference.getId();
    }
}
