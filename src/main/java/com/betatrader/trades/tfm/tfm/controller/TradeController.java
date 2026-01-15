package com.betatrader.trades.tfm.tfm.controller;

import com.betatrader.trades.tfm.tfm.entity.Trade;
import com.betatrader.trades.tfm.tfm.repo.TradeRepository;
import com.betatrader.trades.tfm.tfm.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

   /* @GetMapping("/")
    public String home(){
        return "Hello World!";
    }

    @GetMapping("/secured")
    public String secured(){
        return "Hello secured World!";
    }

    @GetMapping("/user")
    public String welcome(){
        System.out.println("Getting Welcome");
        return "Welcome to Trade Page";
    }*/

    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_ATOM_XML_VALUE
            }

    )
    public List<Trade> findAllTrades(JwtAuthenticationToken token) {
        //inspect jwt claims
        String githubLogin = (String) token.getToken().getClaims().get("github_login");
        System.out.println("Getting all trades "+githubLogin);
        return tradeService.findAllTrades();
    }

    @GetMapping(path = "/page")
    //public Page<Trade> getTrades(JwtAuthenticationToken token,@RequestParam(defaultValue = "0") int page,
     //                            @RequestParam(defaultValue = "3") int size,
     //                           @RequestParam(defaultValue="id,desc") String[] sort) {
       public Page<Trade> findAllTrades(JwtAuthenticationToken token,Pageable pageable){
        //inspect jwt claims
        String githubLogin = (String) token.getToken().getClaims().get("github_login");
        System.out.println("Getting all trades "+githubLogin);
        //Parse sort oarams
        //Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        //Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        return tradeService.getTrades(pageable);
    }

    @PostMapping("/add")
    //@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML})
    public Trade addTrade(@RequestBody Trade trade){
        return tradeService.save(trade);
    }

    /*@PutMapping("/user/update/{id}")

    public Trade updateTrade(@RequestBody Trade trade){
        return tradeService.save(trade);
    }

    @DeleteMapping("/user")

    @GetMapping("/support")
    public String tradeSupport(){
        System.out.println("Getting support");
        return "Welcome to Trade Support Page";
    }*/

    @GetMapping("/upload")
    public String tradeUpload(){
        System.out.println("Getting trade upload");
        return "Welcome to Trade Upload Page";
    }

}
