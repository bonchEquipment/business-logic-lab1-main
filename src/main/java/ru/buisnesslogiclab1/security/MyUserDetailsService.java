package ru.buisnesslogiclab1.security;

import java.io.IOException;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.xml.sax.SAXException;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final XMLUserManager xmlUserManager = new XMLUserManager();

    private List<User> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(getClass().getClassLoader().getResourceAsStream("users.xml"));
            NodeList nodeList = document.getElementsByTagName("user");
            for (int i = 0; i < nodeList.getLength(); i++) {
                String username = document.getElementsByTagName("username").item(i).getTextContent();
                String password = document.getElementsByTagName("password").item(i).getTextContent();
                String role = document.getElementsByTagName("role").item(i).getTextContent();
                users.add(new User(username, password, List.of(() -> role)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadUserDetails(String username, String newRole) {
        // Invalidate the current authentication
        SecurityContextHolder.getContext().setAuthentication(null);


        // Load new details
        var user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().get();
        var newUser = new User(user.getUsername(), user.getPassword(), List.of(() -> newRole));
        users.remove(user);
        users.add(newUser);
        UserDetails userDetails = loadUserByUsername(username);
        UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<XMLUserManager.User> users;
        try {
            users = xmlUserManager.getAllUsers();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new UsernameNotFoundException("User not found", e);
        }

        for (XMLUserManager.User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().isEmpty()) {
                    throw new UsernameNotFoundException("Empty encoded password");
                }
                return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build();
            }
        }

        throw new UsernameNotFoundException("User not found");
    }*/


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}


