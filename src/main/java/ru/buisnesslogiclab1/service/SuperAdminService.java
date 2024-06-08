package ru.buisnesslogiclab1.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.dto.OperationStatusDto;
import ru.buisnesslogiclab1.dto.Role;
import ru.buisnesslogiclab1.repository.UserRepository;
import ru.buisnesslogiclab1.security.MyUserDetailsService;
import ru.buisnesslogiclab1.security.XMLUserManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuperAdminService {


    private final UserRepository userRepository;
    private final XMLUserManager xmlUserManager;
    private final MyUserDetailsService myUserDetailsService;

    @Transactional(rollbackFor = Exception.class)
    public OperationStatusDto appointNewAdmin(UUID userId) throws Exception {
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            return new OperationStatusDto(false, "there is no user with user_id " + userId);

        var user = optionalUser.get();
        if (!user.getRole().equals(Role.USER))
            return new OperationStatusDto(false, "only users can be promoted to admin and user with id " +
                    userId + " have role other than USER. Current role is " + user.getRole());

        xmlUserManager.updateUserRole(user.getNickName(), "ADMIN");

        user.setRole(Role.ADMIN);
        userRepository.save(user);
        myUserDetailsService.reloadUserDetails(user.getNickName(), "ADMIN");
        return new OperationStatusDto(true, null);
    }
}
