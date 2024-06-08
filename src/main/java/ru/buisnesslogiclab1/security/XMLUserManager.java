package ru.buisnesslogiclab1.security;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class XMLUserManager {

    private final File xmlFile = new File("./src/main/resources/users.xml");

    @SneakyThrows
    public void updateUserRole(String username, String newRole) throws Exception {
        Document doc = getDocument();
        NodeList nodeList = doc.getElementsByTagName("user");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String user = element.getElementsByTagName("username").item(0).getTextContent();

                if (user.equals(username)) {
                    element.getElementsByTagName("role").item(0).setTextContent(newRole);
                    break;
                }
            }
        }

        saveDocument(doc);
    }

    public List<User> getAllUsers() throws ParserConfigurationException, IOException, SAXException {
        List<User> users = new ArrayList<>();
        Document doc = getDocument();
        NodeList nodeList = doc.getElementsByTagName("user");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String username = element.getElementsByTagName("username").item(0).getTextContent();
                String password = element.getElementsByTagName("password").item(0).getTextContent();
                String role = element.getElementsByTagName("role").item(0).getTextContent();

                users.add(new User(username, password, role));
            }
        }
        return users;
    }

    public void addUser(String username, String password, String role) throws Exception {
        Document doc = getDocument();
        Element root = doc.getDocumentElement();

        Element newUser = doc.createElement("user");

        Element usernameElement = doc.createElement("username");
        usernameElement.appendChild(doc.createTextNode(username));
        newUser.appendChild(usernameElement);

        Element passwordElement = doc.createElement("password");
        passwordElement.appendChild(doc.createTextNode(password));
        newUser.appendChild(passwordElement);

        Element roleElement = doc.createElement("role");
        roleElement.appendChild(doc.createTextNode(role));
        newUser.appendChild(roleElement);

        root.appendChild(newUser);

        saveDocument(doc);
    }

    private Document getDocument() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(xmlFile);
    }

    private void saveDocument(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(source, result);
    }

    public static class User {
        private String username;
        private String password;
        private String role;

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }
    }
}

