package ru.buisnesslogiclab1.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.buisnesslogiclab1.entity.UserEntity;
import ru.buisnesslogiclab1.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findUserEntityForCurrentSession(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var name = auth.getName();
        var userOptional =userRepository.findByNickName(name);
        if (userOptional.isEmpty())
            log.error("user does not exists {}", auth);

        return userOptional.orElseGet(null);
    }

}
