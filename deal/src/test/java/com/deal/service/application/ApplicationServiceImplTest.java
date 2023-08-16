package com.deal.service.application;

import com.deal.entities.ApplicationStatus;
import com.deal.entities.ChangeType;
import com.deal.model.Application;
import com.deal.model.StatusHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Autowired
    private ApplicationRepository applicationRepository;


    @Test
    public void testAddAndGetApplication() {
        //первые два метода через repository, а не сервис
        Application application = Application.builder()
                .applicationStatus(ApplicationStatus.APPROVED)
                .creationDate(new Date())
                .sesCode("OK")
                .signDate(new Date())
                .build();

        applicationRepository.save(application);
        Long id = application.getId();
        Application testApplication = applicationRepository.findById(id).get();

        assertEquals(testApplication.getId(), id);
        assertEquals(testApplication.getApplicationStatus(), ApplicationStatus.APPROVED);
        assertEquals(testApplication.getSesCode(), "OK");

        applicationRepository.deleteById(id);
    }

    @Test
    public void testCreateApplication() {
        Jsonb jsonb = JsonbBuilder.create();
        StatusHistory statusHistory = StatusHistory.builder()
                .status(ApplicationStatus.APPROVED) //??
                .changeType(ChangeType.AUTOMATIC)
                .date(new Date())
                .build();
        String strStatusHistory = jsonb.toJson(statusHistory);

        Application application = applicationService.createApplication(1L, strStatusHistory);

        assertEquals(application.getClientId().longValue(), 1L);
        assertEquals(application.getSesCode(), "OK");
        assertEquals(application.getApplicationStatus(), ApplicationStatus.PREAPPROVAL);
        assertEquals(application.getStatusHistory(), strStatusHistory);
    }

    @Test
    public void testCreateStatusHistory() {
        Date date = new Date();
        Jsonb jsonb = JsonbBuilder.create();
        String dateJsonb = jsonb.toJson(date);

        String realStatus = "[{\"changeType\":\"AUTOMATIC\",\"date\":" + dateJsonb + ",\"status\":\"APPROVED\"}]";
        List list = new ArrayList<>();
        ApplicationStatus applicationStatus = ApplicationStatus.APPROVED;
        ChangeType changeType = ChangeType.AUTOMATIC;
        String statusTest = applicationService.createStatusHistory(list,
                applicationStatus, changeType, date);

        assertEquals(statusTest, realStatus);
    }

    @Test
    public void testUpdateStatusHistory() {
        Date date = new Date();

        Jsonb jsonb = JsonbBuilder.create();
        String dateJsonb = jsonb.toJson(date);
        StatusHistory statusHistory = StatusHistory.builder()
                .status(ApplicationStatus.APPROVED) //??
                .changeType(ChangeType.AUTOMATIC)
                .date(date)
                .build();
        List list = new ArrayList<>();
        list.add(statusHistory);
        String strStatusHistory = jsonb.toJson(list);

        Application application = Application.builder()
                .id(1L)
                .applicationStatus(ApplicationStatus.PREAPPROVAL)
                .clientId(1L)
                .creditId(1L)
                .creationDate(date)
                .sesCode("OK")
                .signDate(date)
                .statusHistory(strStatusHistory)
                .build();

        application = applicationService.updateApplicationStatusHistory(application, date, ApplicationStatus.APPROVED);

        String realStatus = "[{\"date\":" + dateJsonb + "," +
                "\"changeType\":\"AUTOMATIC\",\"status\":\"APPROVED\"}," +
                "{\"changeType\":\"AUTOMATIC\",\"date\":" + dateJsonb +
                ",\"status\":\"APPROVED\"}]";

        assertEquals(application.getStatusHistory(), realStatus);
    }
}