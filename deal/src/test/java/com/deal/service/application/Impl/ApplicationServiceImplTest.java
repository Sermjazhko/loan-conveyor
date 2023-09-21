package com.deal.service.application.Impl;

import com.deal.enums.ApplicationStatus;
import com.deal.enums.ChangeType;
import com.deal.model.Application;
import com.deal.model.StatusHistory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(
        locations = "classpath:application-local.properties")
class ApplicationServiceImplTest {

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Disabled
    @Test
    void testCreateStatusHistory() {
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

    @Disabled
    @Test
    void testUpdateStatusHistory() {
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
                .applicationStatus(ApplicationStatus.PREAPPROVAL)
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