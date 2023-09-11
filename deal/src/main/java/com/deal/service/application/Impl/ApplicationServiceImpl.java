package com.deal.service.application.Impl;

import com.deal.enums.ApplicationStatus;
import com.deal.enums.ChangeType;
import com.deal.model.Application;
import com.deal.model.Client;
import com.deal.model.StatusHistory;
import com.deal.repository.ApplicationRepository;
import com.deal.service.application.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void addApplicationToDB(Application application, Date date, ApplicationStatus applicationStatus) {
        Application application1 = updateApplicationStatusHistory(application, date, applicationStatus);
        applicationRepository.save(application1);
    }

    @Override
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id).get();
    }

    @Override
    public Application createApplication(Client client) {
        Date date = new Date();
        Jsonb jsonb = JsonbBuilder.create();
        List<StatusHistory> list = new ArrayList<>();
        return Application.builder()
                .client(client)
                .creationDate(date)
                .sesCode(generationSesCode())
                .signDate(date)
                .applicationStatus(ApplicationStatus.PREAPPROVAL)
                .statusHistory(jsonb.toJson(list))
                .build();
    }

    private String generationSesCode() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    @Override
    public String createStatusHistory(List list, ApplicationStatus applicationStatus, ChangeType changeType,
                                      Date date) {
        Jsonb jsonb = JsonbBuilder.create();
        StatusHistory statusHistory = StatusHistory.builder()
                .status(applicationStatus)
                .changeType(changeType)
                .date(date)
                .build();

        list.add(statusHistory);
        return jsonb.toJson(list);
    }

    @Override
    public Application updateApplicationStatusHistory(Application application, Date date,
                                                      ApplicationStatus applicationStatus) {
        Jsonb jsonb = JsonbBuilder.create();
        List<StatusHistory> list =
                jsonb.fromJson(application.getStatusHistory(), List.class);
        log.info("Status History: " + list);

        application.setApplicationStatus(applicationStatus);
        String resultListHistory = createStatusHistory(list, applicationStatus,
                ChangeType.AUTOMATIC, date);
        log.info("list history status: " + resultListHistory);

        application.setStatusHistory(resultListHistory);
        return application;
    }
}
