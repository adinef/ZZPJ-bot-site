package pl.lodz.p.it.zzpj.botsite.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.BotDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.BotNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.BotRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotCreationDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotEditDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotViewDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/bot")
@Slf4j
public class BotAdminController {

    private final ModelMapper modelMapper;
    private final BotService botService;
    private final UserService userService;

    @Autowired
    public BotAdminController(ModelMapper modelMapper, BotService botService, UserService userService) {
        this.modelMapper = modelMapper;
        this.botService = botService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<BotViewDTO> getAllBots() throws BotRetrievalException {
        List<Bot> bots = this.botService.findAll();
        return mapList(bots);
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void deleteBot(@PathVariable("id") Long id)
            throws BotDeletionException {
        this.botService.deleteBot(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(
            value = "user/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public BotCreationDTO createBot(@PathVariable("id") Long userId, @RequestBody BotCreationDTO dto)
            throws BotAlreadyExistsException, BotAdditionException, UserRetrievalException {
        Bot bot = this.modelMapper.map(dto, Bot.class);
        User user = userService.findById(userId);
        bot.setUser(user);
        Bot addedBot = this.botService.addBot(bot);
        return this.modelMapper.map(addedBot, BotCreationDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public BotViewDTO editBot(@PathVariable("id") Long id, @RequestBody BotEditDTO dto)
            throws BotRetrievalException, BotUpdateException {
        Bot botById = this.botService.findById(id);
        this.botService.updateBotChannel(botById.getId(), dto.getChannel());
        Bot updated = this.botService.updateBotName(botById.getId(), dto.getName());
        return this.modelMapper.map(updated, BotViewDTO.class);
    }

    private List<BotViewDTO> mapList(List<Bot> bots) {
        List<BotViewDTO> dtoList = new ArrayList<>();
        bots.forEach( b -> dtoList.add(this.modelMapper.map(b, BotViewDTO.class)));
        return dtoList;
    }
}