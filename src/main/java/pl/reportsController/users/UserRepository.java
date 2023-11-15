package pl.reportsController.users;

import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.reportsController.roles.ERole;
import pl.reportsController.roles.RoleEntity;

import java.util.Optional;
import java.util.Set;


public interface UserRepository extends CrudRepository<UserEntity, Long> {
    @Query("SELECT u.id FROM UserEntity u WHERE u.login = ?1 AND u.password = ?2 AND u.isUserActive=true")
    Long findIdByUsernameAndPassword(String login, String password);

    @Query("SELECT login FROM UserEntity u where u.login=?1 AND u.isUserActive=true")
    String checkLoginExists(String login);

    @Query("select email from UserEntity u where u.email=?1 AND u.isUserActive=true")
    String checkEmailExists(String email);

    @Query("SELECT u.id FROM UserEntity u WHERE u.login = ?1 AND u.isUserActive=true")
    String findByLogin(String login);

    @Query("SELECT u.password FROM UserEntity u WHERE u.login = ?1 AND u.isUserActive=true")
    String findPasswordByLogin(String login);

    @Query("SELECT u.id FROM UserEntity u WHERE u.email = ?1 AND u.isUserActive=true")
    Long findIdByEmail(String email);
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.password = ?3, u.lastPasswordReset = ?4 WHERE u.id = ?1 AND u.email = ?2")
    int updateUserPassword(Long id, String email, String randomPassword, LocalDateTime resetDate);

    @Query("SELECT u.lastPasswordReset FROM UserEntity u where u.id = ?1 AND u.isUserActive=true")
    LocalDateTime getUserLastResetPasswordDate(Long idUser);

    @Query("SELECT r FROM UserEntity u JOIN u.roles r WHERE u.idUser = :userId AND u.isUserActive=true")
    Set<RoleEntity> findRolesByUserId(@Param("userId") Long userId);

    UserEntity getById(Long idUser);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.customerEntity WHERE u.idUser = :id AND u.isUserActive=true")
    Optional<UserEntity> findUserWithCustomerById(@Param("id") Long id);

    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r = :role AND u.isUserActive=true")
    Iterable<UserEntity> findAllUsersWithRoles(@Param("role") RoleEntity role);

}
