package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.UserRegistrationDTO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakUserService {
    private final Keycloak keycloak;
    private final String realm;

    public KeycloakUserService(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    public String createUser(UserRegistrationDTO userDTO) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        CredentialRepresentation credential = makeCredentialForUser(userDTO);

        UserRepresentation userRepresentation = makeUserRepresentation(userDTO, credential);

        Response response = usersResource.create(userRepresentation);

        return getMessageFromResponse(response);

    }

    private String getMessageFromResponse(Response response) {
        Response.Status status = response.getStatusInfo().toEnum();
        switch (status) {
            case Response.Status.CREATED -> {
                return response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            }
            case Response.Status.CONFLICT -> {
                throw new RuntimeException("Username or email already exists.");
            }

            default -> throw new RuntimeException("Error creating user: " + response.getStatusInfo());
        }
    }

    private static UserRepresentation makeUserRepresentation(UserRegistrationDTO userDTO, CredentialRepresentation credential) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setEmailVerified(false);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        return user;
    }

    private static CredentialRepresentation makeCredentialForUser(UserRegistrationDTO userDTO) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDTO.getPassword());
        credential.setTemporary(false);
        return credential;
    }
}

