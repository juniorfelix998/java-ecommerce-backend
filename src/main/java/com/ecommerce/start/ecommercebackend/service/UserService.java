package com.ecommerce.start.ecommercebackend.service;

import com.ecommerce.start.ecommercebackend.api.model.LoginBody;
import com.ecommerce.start.ecommercebackend.api.model.RegistrationBody;
import com.ecommerce.start.ecommercebackend.exception.UserAlreadyExistsException;
import com.ecommerce.start.ecommercebackend.model.LocalUser;
import com.ecommerce.start.ecommercebackend.model.dao.LocalUserDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;
    private JWTService jwtService;

    public UserService(LocalUserDAO localUserDAO ,EncryptionService encryptionService, JWTService jwtService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {
        if (localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        user = localUserDAO.save(user);
        return user;
    }

    public String loginUser(LoginBody loginBody){
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()){
            LocalUser user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(),user.getPassword())){
                System.out.println("data");
                System.out.println(jwtService.generateJWT(user));
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }
}
