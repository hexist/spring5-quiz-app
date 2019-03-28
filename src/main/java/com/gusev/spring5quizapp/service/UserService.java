package com.gusev.spring5quizapp.service;

import com.gusev.spring5quizapp.model.Role;
import com.gusev.spring5quizapp.model.User;
import com.gusev.spring5quizapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MailSender mailSender;

    public UserService(UserRepository userRepository, MailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        sendActivationCode(user);

        return true;
    }

    private void sendActivationCode(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String text = String.format("Hello, %s! \n" +
                            "Welcome to Quiz! \n" +
                            "Please, go to this link to activate your account:" +
                            "http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation code", text);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roleSet = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roleSet.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public void updateProfile(User user, String password, String email) {
        final String userEmail = user.getEmail();


        boolean isEmailChanged = ((email != null && !email.equals(userEmail))
                || (userEmail != null && !userEmail.equals(email)));

        if (isEmailChanged) {
            user.setEmail(email);

            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        userRepository.save(user);

        if (isEmailChanged) {
            sendActivationCode(user);
        }
    }
}
