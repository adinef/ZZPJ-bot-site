package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.VerificationTokenInfoNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.repositories.VerificationTokenRepository;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private UserService userService;
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenServiceImpl(
            UserService userService,
            VerificationTokenRepository verificationTokenRepository
    ) {
        this.userService = userService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public void saveToken(User user, String token) throws UserRetrievalException {

        userService.findByLogin(user.getLogin());

        this.verificationTokenRepository.save(new VerificationTokenInfo(
                user,
                token
        ));
    }

    @Override
    public VerificationTokenInfo findVerificationTokenInfo(String token) throws VerificationTokenInfoNotFoundException {
        return this.verificationTokenRepository.findByToken(token).orElseThrow(() ->
                new VerificationTokenInfoNotFoundException());
    }
}
