package br.com.nb.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nb.environment.InstanceInformationService;
import br.com.nb.model.Exchange;
import br.com.nb.repository.ExchangeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Exchange Endpoint")
@RestController
@RequestMapping("exchange-service")
public class ExchangeController {

    @Autowired
    InstanceInformationService informationService;

    @Autowired
    ExchangeRepository repository;

    @Operation(summary = "Get an exchange form amount of currency")
   // http://localhost:8000/exchange-service/5/USD/BRL
   @GetMapping(value = "/{amount}/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Exchange getExchange(
            @PathVariable("amount") BigDecimal amount,
            @PathVariable("from") String from,
            @PathVariable("to") String to){

        Exchange exchange = repository.findByFromAndTo(from, to);

        if(exchange == null) throw new RuntimeException("Currency Unsupported!");

        BigDecimal conversionFactor = exchange.getConversionFactor();
        BigDecimal convertedValue = conversionFactor.multiply(amount);
        exchange.setConvertedValue(convertedValue);
        exchange.setEnvironment("PORT " + informationService.retrieveServerPort());

        return exchange;
    }


   /*// http://localhost:8000/exchange-service/5/USD/BRL
   @GetMapping(value = "/{amount}/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Exchange getExchange(
        @PathVariable("amount") BigDecimal amount,
        @PathVariable("from") String from,
        @PathVariable("to") String to){
        return new Exchange(1L, from, to, BigDecimal.ONE,
             BigDecimal.ONE, "PORT " + informationService.retrieveServerPort());
    }***/
}
