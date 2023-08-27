package com.google.cms.card_management;


import com.google.cms.Users.Activeusers.User;
import com.google.cms.Users.Activeusers.UserRepository;
import com.google.cms.role.Role;
import com.google.cms.utilities.ColorFormatException;
import com.google.cms.utilities.ERole;
import com.google.cms.utilities.Shared.AuditTrailService;
import com.google.cms.utilities.Shared.EntityResponse;
import com.google.cms.utilities.Shared.ResponseService;
import com.google.cms.utilities.requests_proxy.UserRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CardService {
    private final UserRepository userRepository;
    private final ResponseService responseService;
    private final AuditTrailService auditTrailService;
    private final CardRepo cardRepo;

    public CardService(UserRepository userRepository, ResponseService responseService, AuditTrailService auditTrailService, CardRepo cardRepo) {
        this.userRepository = userRepository;
        this.responseService = responseService;
        this.auditTrailService = auditTrailService;
        this.cardRepo = cardRepo;
    }


    public EntityResponse createCard(Card card) {
        try {
            if (card.getColor() !=null){
                if (!card.getColor().matches("^#[A-Za-z0-9]{6}$")){
                    return responseService.response(HttpStatus.BAD_REQUEST, "Invalid Color code: Color should conform to the format '#RRGGBB'", null);
                }
            }
            Optional<User> user = userRepository.findByUsername(UserRequestContext.getCurrentUser());
            card.setUser(user.orElse(null));
            Card savedCard = cardRepo.save(auditTrailService.POSTAudit(card));
            return responseService.response(HttpStatus.CREATED, "CREATED SUCCESSFULLY!", savedCard);
        } catch (DataAccessException e) {
            log.error("Database access error: " + e.getMessage());
            return responseService.response(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred.", null);
        } catch (ColorFormatException e) {
            log.error("Invalid argument error: " + e.getMessage());
            return responseService.response(HttpStatus.BAD_REQUEST, "Invalid argument: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Unexpected error: " + e.toString());
            return responseService.response(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", null);
        }
    }
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.username == #user.username")
    public EntityResponse findAllCard(@AuthenticationPrincipal User user) {
        try {
            List<Card> cardsList = null;
            boolean containsRole = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN.toString()));
            if (containsRole==true){
                cardsList = cardRepo.findByDeletedFlag('N');
            }else {
                cardsList = cardRepo.findByDeletedFlag('N').stream()
                        .filter(card -> card.getUser().equals(user))
                        .collect(Collectors.toList());
            }
            if (!cardsList.isEmpty()) {
                return responseService.response(
                        HttpStatus.FOUND,
                        "CARDS FOUND!",
                        cardsList
                );
            } else {
                return responseService.response(
                        HttpStatus.NOT_FOUND,
                        "CARDS NOT FOUND!",
                        null
                );
            }
        } catch (Exception e) {
            log.error("An error occurred while processing the request.", e);
            return responseService.response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while processing the request.",
                    null
            );
        }
    }

    @PreAuthorize("hasRole('ADMIN') or authentication.principal.username == #user.username")
    public EntityResponse findCardById(@AuthenticationPrincipal User user, Long id){
        try {
            Optional<Card> cardOptional = cardRepo.findById(id);
            if (cardOptional.isPresent()){
                boolean containsRole = user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN.toString()));
                if (containsRole==true){
                    return responseService.response(
                            HttpStatus.FOUND,
                            "FOUND CARD!",
                            cardOptional
                    );
                }else if (cardOptional.get().getUser().equals(user)){
                    return responseService.response(
                            HttpStatus.FOUND,
                            "FOUND CARD!",
                            cardOptional
                    );
                }else {
                    return responseService.response(
                            HttpStatus.FOUND,
                            "UNAUTHORIZED!",
                            null
                    );
                }
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
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.username == #user.username")
    public EntityResponse searchCards(String name, String color, String status, @AuthenticationPrincipal User user, Pageable pageable) {
        try {
            Page<Card> cardPage = cardRepo.findByNameAndColorAndStatusAndDeletedFlagOrderByPostedTimeDesc(
                    name, color, status,'N', pageable);

            boolean containsRole = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN.toString()));
            if (containsRole==true){
                cardPage = cardPage;
            }else {
                List<Card> filteredCards = cardPage
                        .getContent()
                        .stream()
                        .filter(card -> card.getUser().equals(user))
                        .collect(Collectors.toList());
                cardPage = new PageImpl<>(filteredCards, pageable, filteredCards.size());
            }
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
