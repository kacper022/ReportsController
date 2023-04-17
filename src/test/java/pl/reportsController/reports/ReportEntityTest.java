package pl.reportsController.reports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ReportEntityTest {

    ReportEntity report;
    Date date1;
    Date date2;
    @BeforeEach
    void createReport(){
        date1 = new Date();
        date2 = new Date(2023,11,2);
        report = new ReportEntity();
        report.setId(1L);
        report.setName("Ustawienie TV");
        report.setUpdateDate(date1);
        report.setCreateDate(date1);
        report.setDescription("Nastrojenie telewizora");
        report.setEndDate(date2);
    }

    @Test
    void getId() {
        assertAll("ID",
                () -> assertEquals(1L, report.getId()),
                () -> assertNotEquals("1", report.getId()),
                () -> assertInstanceOf(Long.class, report.getId())
        );
    }

    @Test
    void getName(){
        assertAll("Nazwa usterki",
                () -> assertEquals("Ustawienie TV", report.getName()),
                () -> assertNotEquals("", report.getName()),
                () -> assertInstanceOf(String.class, report.getName())
        );
    }

    @Test
    void getUpdateDate(){
        assertEquals(date1, report.getUpdateDate());
        assertNotNull(report.getUpdateDate());
    }

    @Test
    void getCreateDate(){
        assertEquals(date1, report.getCreateDate());
        assertNotNull(report.getCreateDate());
    }

    @Test
    void getEndDate(){
        assertEquals(date2, report.getEndDate());
    }

    @Test
    void getDescription(){
        assertEquals("Nastrojenie telewizora", report.getDescription());
    }

    @Test
    void isDescriptionEmpty(){
        //given
        //when
        report.setDescription("");

        //then
        assertEquals("", report.getDescription());
    }
}