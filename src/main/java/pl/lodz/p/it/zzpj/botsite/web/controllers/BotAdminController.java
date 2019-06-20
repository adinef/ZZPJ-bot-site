package pl.lodz.p.it.zzpj.botsite.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.BotRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotCreationDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotViewDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bot")
@Slf4j
public class BotAdminController {

    private final ModelMapper modelMapper;
    private final BotService botService;

    @Autowired
    public BotAdminController(ModelMapper modelMapper, BotService botService) {
        this.modelMapper = modelMapper;
        this.botService = botService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(
            value = "user/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<BotViewDTO> getAllBotsForUser(@PathVariable("id") Long id) throws BotRetrievalException {
        List<Bot> bots = this.botService.findAllForUserId(id);
        return mapList(bots);
    }

    private List<BotViewDTO> mapList(List<Bot> bots) {
        List<BotViewDTO> dtoList = new ArrayList<>();
        bots.forEach( b -> this.modelMapper.map(b, BotViewDTO.class));
        return dtoList;
    }
}