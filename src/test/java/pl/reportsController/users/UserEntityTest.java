package pl.reportsController.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {
    UserEntity userEntity;

    @BeforeEach
    void createNewUser(){
        userEntity = new UserEntity();
        userEntity.setFirstName("Jan");
        userEntity.setLastName("Kowalski");
        userEntity.setPassword("Password1!");
        userEntity.setIsUserActive(0);
    }

    @Test
    void getFirstName() {
        assertAll("Imie",
                () -> assertEquals("Jan", userEntity.getFirstName()),
                () -> assertNotEquals("JAn", userEntity.getFirstName()),
                () -> assertNotEquals("Jan1", userEntity.getFirstName()),
                () -> assertNotEquals("", userEntity.getFirstName())
        );
    }

    @Test
    void getLastName(){
        assertAll("Nazwisko",
                () -> assertEquals("Kowalski", userEntity.getLastName()),
                () -> assertNotEquals("KOwalski", userEntity.getLastName()),
                () -> assertNotEquals("1241", userEntity.getLastName()),
                () -> assertNotEquals("", userEntity.getLastName())
        );
    }

    @Test
    void setPassword(){
        assertAll("Haslo po zakodowaniu",
                () -> assertEquals("1d707811988069ca760826861d6d63a10e8c3b7f171c4441a6472ea58c11711b", userEntity.getPassword()),
                () -> assertNotEquals("", userEntity.getPassword()),
                () -> assertNotEquals("Password1!", userEntity.getPassword())
        );
    }

    @Test
    void isUserActive(){

        assertAll("Stan uzytkownika",
                () -> assertEquals(0, userEntity.getIsUserActive()),
                () -> assertNotEquals(1, userEntity.getIsUserActive()),
                () -> assertNotEquals("", userEntity.getIsUserActive()),
                () -> assertInstanceOf(Integer.class, userEntity.getIsUserActive())

        );
    }
}