package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.BaseCaffData;
import hu.bme.szgbizt.secushop.dto.User;
import hu.bme.szgbizt.secushop.exception.UserNotFoundException;
import hu.bme.szgbizt.secushop.persistence.entity.ShopUserEntity;
import hu.bme.szgbizt.secushop.persistence.repository.ShopUserRepository;
import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import hu.bme.szgbizt.secushop.security.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ShopUserRepository shopUserRepository;

    /**
     * @param userRepository     Repository for {@link UserEntity}.
     * @param shopUserRepository Repository for {@link ShopUserEntity}.
     */
    public UserService(UserRepository userRepository, ShopUserRepository shopUserRepository) {
        this.userRepository = userRepository;
        this.shopUserRepository = shopUserRepository;
    }

    @Transactional
    public User getUser(UUID userId) {
        var userEntity = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        var shopUserEntity = shopUserRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        var caffData = shopUserEntity.getCaffData().stream()
                .map(caffDataEntity -> new BaseCaffData(
                        caffDataEntity.getId(),
                        caffDataEntity.getName(),
                        caffDataEntity.getDescription()
                ))
                .collect(Collectors.toList());

        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getRoles(),
                shopUserEntity.getBalance(),
                caffData
        );
    }
}
