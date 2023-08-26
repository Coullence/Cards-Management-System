package com.google.cms.card_management;

import com.google.cms.utilities.Shared.EntityResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    @PostMapping("/add")
    public EntityResponse createCard(@RequestBody Card card){ return cardService.createCard(card);}
    @GetMapping("/all")
    public EntityResponse findAllCard()
    {
        return cardService.findAllCard();
    }
    @GetMapping("/find/card")
    public EntityResponse findById(@RequestParam Long id)
    {
        return cardService.findCardById(id);
    }
    @GetMapping("/search")
    public EntityResponse searchCards(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String status,
            Pageable pageable
    ) {
       return cardService.searchCards(name, color, status, pageable);
    }
    @PutMapping("/update")
    public EntityResponse updateCard(@RequestBody Card card)
    {
        return cardService.updateCard(card);
    }
    @DeleteMapping("/delete")
    public EntityResponse deleteCard(@RequestParam Long id)
    {
        return cardService.deleteCard(id);
    }
}