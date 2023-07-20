package pl.reportsController.users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    @Query("SELECT u.id FROM UserEntity u WHERE u.login = ?1 AND u.password = ?2")
    Long findIdByUsernameAndPassword(String login, String password);

    @Query("SELECT login FROM UserEntity u where u.login=?1")
    String checkLoginExists(String login);

    @Query("select email from UserEntity u where u.email=?1")
    String checkEmailExists(String email);

    @Query("SELECT u.id FROM UserEntity u WHERE u.login = ?1")
    String findByLogin(String login);
    @Query("SELECT u.password FROM UserEntity u WHERE u.login = ?1")
    String findPasswordByLogin(String login);
}
