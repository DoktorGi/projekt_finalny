package pl.sda.final_project.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sda.final_project.dto.RegistrationDto;
import pl.sda.final_project.model.user.User;
import pl.sda.final_project.model.user.UserRole;
import pl.sda.final_project.repo.UserRepo;
import pl.sda.final_project.repo.UserRoleRepo;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final UserRoleRepo userRoleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, UserRoleRepo userRoleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userRoleRepo = userRoleRepo;
         this.passwordEncoder = passwordEncoder;
    }


    public void registerUser(RegistrationDto registrationDto) {
        String pass = passwordEncoder.encode(registrationDto.getPassword());
        if (userWithEmailExists(registrationDto.getLogin())) {
            throw new RuntimeException("Użytkownik o emailu " + registrationDto.getLogin() + "istnieje");
        }
        User userToSave = User.apply(registrationDto, pass);
        userToSave.addRole(userRoleRepo.findByRoleName(UserRole.Roles.USER.name()));
        userRepo.save(userToSave);

    }

    private boolean userWithEmailExists(String login) {
        return userRepo.existsByLogin(login);
    }


}


