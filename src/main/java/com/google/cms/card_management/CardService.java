package com.google.cms.card_management;


import com.google.cms.utilities.Shared.AuditTrailService;
import com.google.cms.utilities.Shared.EntityResponse;
import com.google.cms.utilities.Shared.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CardService {
    private final ResponseService responseService;
    private final AuditTrailService auditTrailService;
    private final CardRepo cardRepo;

    public CardService(ResponseService responseService, AuditTrailService auditTrailService, CardRepo cardRepo) {
        this.responseService = responseService;
        this.auditTrailService = auditTrailService;
        this.cardRepo = cardRepo;
    }


    public EntityResponse createCard(Card card){
        try {
            return responseService.response(
                    HttpStatus.CREATED,
                    "CREATED SUCCESSFULLY!",
                    cardRepo.save(auditTrailService.POSTAudit(card))
            );
        }catch (Exception e){
            log.info(e.toString());
            return null;
        }
    }
    public EntityResponse findAllCard(){
        try {
            List<Card> cardsList = cardRepo.findByDeletedFlag('N');
            if (cardsList.size()>0){
                return responseService.response(
                        HttpStatus.FOUND,
                        "CARDS FOUND!",
                        cardsList
                );
            }else {
                return responseService.response(
                        HttpStatus.NOT_FOUND,
                        "CARDS NOT FOUND!",
                        null
                );
            }
        }catch (Exception e){
            log.info(e.toString());
            return null;
        }
    }
    public EntityResponse findCardById(Long id){
        try {
            Optional<Card> cardOptional = cardRepo.findById(id);
            if (cardOptional.isPresent()){
                return responseService.response(
                        HttpStatus.FOUND,
                        "CARD FOUND!",
                        cardOptional.get()
                );
            }else {
                return responseService.response(
                        HttpStatus.NOT_FOUND,
                        "CARD  NOT FOUND!",
                        null
                );
            }

        }catch (Exception e){
            log.info(e.toString());
            return null;
        }
    }
    public EntityResponse searchCards(String name, String color, String status, Pageable pageable) {
        try {
            Page<Card> cardPage = cardRepo.findByNameAndColorAndStatusOrderByPostedTimeDesc(
                    name, color, status, pageable);

            if (cardPage.hasContent()) {
                return responseService.response(
                        HttpStatus.FOUND,
                        "CARDS FOUND!",
                        cardPage
                );
            } else {
                return responseService.response(
                        HttpStatus.NOT_FOUND,
                        "CARDS NOT FOUND!",
                        null
                );
            }
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    public EntityResponse updateCard(Card card) {
        try {
            Optional<Card> cardOptional = cardRepo.findById(card.getId());
            if (cardOptional.isPresent()) {
                Card existingCard = cardOptional.get();
                if (card.getDescription() == null) {
                    existingCard.setDescription(null);
                } else {
                    existingCard.setDescription(card.getDescription());
                }
                if (card.getColor() == null) {
                    existingCard.setColor(null); // Clear out the color field
                } else {
                    existingCard.setColor(card.getColor());
                }
                if (Arrays.asList("To Do", "In Progress", "Done").contains(card.getStatus())) {
                    existingCard.setStatus(card.getStatus());
                }
                return responseService.response(
                        HttpStatus.OK,
                        "UPDATED SUCCESSFULLY!",
                        cardRepo.save(auditTrailService.MODIFYAudit(existingCard))
                );
            } else {
                return responseService.response(
                        HttpStatus.NOT_FOUND,
                        "CARD NOT FOUND!",
                        null
                );
            }
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    public EntityResponse deleteCard(Long id){
        try {
            Optional<Card> cardOptional = cardRepo.findById(id);
            if (cardOptional.isPresent()){
                Card card = cardOptional.get();
                return responseService.response(
                        HttpStatus.OK,
                        "DELETED SUCCESSFULLY!",
                        cardRepo.save(auditTrailService.DELETEAudit(card))
                );
            }else {
                return responseService.response(
                        HttpStatus.NOT_FOUND,
                        "CARD NOT FOUND!",
                        null
                );
            }
        }catch (Exception e){
            log.info(e.toString());
            return null;
        }
    }
}
