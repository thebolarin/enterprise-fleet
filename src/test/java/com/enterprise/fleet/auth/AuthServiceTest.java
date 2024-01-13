package com.enterprise.fleet.auth;

import com.enterprise.fleet.auth.dto.RegisterRequestDTO;
import com.enterprise.fleet.user.types.Role;
import com.enterprise.fleet.user.entity.User;
import com.enterprise.fleet.user.repository.UserRepository;
import com.enterprise.fleet.user.types.UserStatusType;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();
    @Mock
    private UserRepository mockUserRepository;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void whenRegisteringUserWithZeroExistingUsersExpectOneCallToUserRepositorySave() {
        String password = faker.internet().password();
        User user = buildUser(password);
        RegisterRequestDTO registerRequestDTO = createRegisterRequestDTO(user, password);

        when(mockUserRepository.save(user)).thenReturn(user);

        when(authenticationService.register(registerRequestDTO)).thenReturn(user);

        authenticationService.register(registerRequestDTO);
        verify(mockUserRepository, times(1)).save(user);
    }

    private User buildUser(String password) {

        return User.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .status(UserStatusType.ACTIVE)
                .email(faker.internet().emailAddress())
                .password(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .build();
    }

    private RegisterRequestDTO createRegisterRequestDTO(User user, String password) {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setFirstName(user.getFirstName());
        registerRequestDTO.setLastName(user.getLastName());
        registerRequestDTO.setEmail(user.getEmail());
        registerRequestDTO.setPassword(password);
        registerRequestDTO.setRole(user.getRole());
        return registerRequestDTO;
    }
}
