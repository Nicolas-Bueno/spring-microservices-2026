package br.com.nb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nb.dto.ExchangeDto;
import br.com.nb.environment.InstanceInformationService;
import br.com.nb.model.Book;
import br.com.nb.proxy.ExchangeProxy;
import br.com.nb.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Book Endpoint")
@RestController
@RequestMapping("book-service")
public class BookController {

    @Autowired
    InstanceInformationService informationService;
    @Autowired
    BookRepository repository;
    @Autowired
    private ExchangeProxy proxy;

    @Operation(summary = "Find a specific book by your ID")
    //http://localhost:8100/book-service/1/BRL
    @GetMapping(value="/{id}/{currency}", 
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findBook(@PathVariable("id")Long id,
                         @PathVariable("currency")String currency){

        String port = informationService.retrieveServerPort();

        var book = repository.findById(id).orElseThrow();

        ExchangeDto exchange = proxy.getExchange(book.getPrice(), "USD", currency);

        //book.setEnviroment(port + "Feign");
        book.setEnviroment("BOOK PORT: " + port + " EXCHANGE PORT: " + exchange.getEnvironment());
        book.setPrice(exchange.getConvertedValue());
        book.setCurrency(currency);
        return book;
    }

   /** @GetMapping(value="/{id}/{currency}", 
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findBook(@PathVariable("id")Long id,
                         @PathVariable("currency")String currency){

        String port = informationService.retrieveServerPort();

        var book = repository.findById(id).orElseThrow();

        HashMap<String, String> params= new HashMap<>();
        params.put("amount", book.getPrice().toString());
        params.put("from", "USD");
        params.put("to", currency);

        var response = new RestTemplate()
                        .getForEntity("http://localhost:8000/exchange-service/" +
                            "{amount}/{from}/{to}", ExchangeDto.class, params);

        ExchangeDto exchange = response.getBody();

        book.setEnviroment(port);
        book.setPrice(exchange.getConvertedValue());
        book.setCurrency(currency);
        return book;
    }

    @GetMapping(value="/{id}/{currency}", 
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findBook(@PathVariable("id")Long id,
                         @PathVariable("currency")String currency){

        String port = informationService.retrieveServerPort();

        var book = repository.findById(id).orElseThrow();
        book.setEnviroment(port);
        book.setCurrency(currency);
        return book;
    }

    
    @GetMapping(value="/{id}/{currency}", 
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findBook(@PathVariable("id")Long id,
                         @PathVariable("currency")String currency){

        String port = informationService.retrieveServerPort();

        return new Book(
            1L,
            "Erudio",
            "Spring",
            new Date(),
            15.8,
            "BRL",
            port
        );
    }*****/

}
