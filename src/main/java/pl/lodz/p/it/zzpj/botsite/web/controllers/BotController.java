package pl.lodz.p.it.zzpj.botsite.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
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
import pl.lodz.p.it.zzpj.botsite.web.dto.MyUserDetails;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotCreationDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotEditDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotViewDTO;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bot")
@Slf4j
public class BotController {

    private final ModelMapper modelMapper;
    private final BotService botService;
    private final PrincipalProvider principalProvider;
    private final UserService userService;

    @Autowired
    public BotController(ModelMapper modelMapper, BotService botService, PrincipalProvider principalProvider, UserService userService) {
        this.modelMapper = modelMapper;
        this.botService = botService;
        this.principalProvider = principalProvider;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(
            value = "user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public BotCreationDTO createBot(@RequestBody BotCreationDTO dto) throws BotAlreadyExistsException, BotAdditionException, UserRetrievalException {
        Bot bot = this.modelMapper.map(dto, Bot.class);
        bot.setUser(userService.findByLogin(principalProvider.getName()));
        Bot addedBot = this.botService.addBot(bot);
        return this.modelMapper.map(addedBot, BotCreationDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(
            value = "user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<BotViewDTO> getAllBotsForUser() throws BotRetrievalException, UserRetrievalException {
        User user = this.userService.findByLogin(principalProvider.getName());
        List<Bot> bots = this.botService.findAllForUserId(user.getId());
        return mapList(bots);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(
            value = "user/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public BotViewDTO editBot(@PathVariable("id") Long id, @RequestBody BotEditDTO dto)
            throws BotNotFoundException, BotRetrievalException, BotUpdateException {
        Bot botById = this.botService.findById(id);
        if (!botById.getUser().getLogin().equals(principalProvider.getName())) {
            throw new BotUpdateException("Can't update somebody else's bot.");
        }
        this.botService.updateBotChannel(botById.getId(), dto.getChannel());
        Bot updated = this.botService.updateBotName(botById.getId(), dto.getName());
        return this.modelMapper.map(updated, BotViewDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(
            value = "user/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void deleteBot(@PathVariable("id") Long id)
            throws BotRetrievalException, BotDeletionException, BotNotFoundException {
        Bot botById = this.botService.findById(id);
        if (!botById.getUser().getLogin().equals(principalProvider.getName())) {
            throw new BotDeletionException("Can't delete somebody's else bot.");
        }
        this.botService.deleteBot(botById.getId());
    }

    private List<BotViewDTO> mapList(List<Bot> bots) {
        List<BotViewDTO> dtoList = new ArrayList<>();
        bots.forEach( b -> dtoList.add(this.modelMapper.map(b, BotViewDTO.class)));
        return dtoList;
    }
}