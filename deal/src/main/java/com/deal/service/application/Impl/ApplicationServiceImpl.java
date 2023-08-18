package com.deal.service.application.Impl;

import com.deal.controller.DealController;
import com.deal.enums.ApplicationStatus;
import com.deal.enums.ChangeType;
import com.deal.model.Application;
import com.deal.model.StatusHistory;
import com.deal.repository.ApplicationRepository;
import com.deal.service.application.ApplicationService;
import org.springframework.stereotype.Service;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static Logger log = Logger.getLogger(DealController.class.getName());

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void addApplicationToDB(Application application) {
        applicationRepository.save(application);
    }

    @Override
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id).get();
    }

    @Override
    public Application createApplication(Long id, String statusHistory) {
        Date date = new Date();
        return Application.builder()
                .clientId(id)
                .creationDate(date)
                .sesCode("OK")
                .signDate(date)
                .applicationStatus(ApplicationStatus.PREAPPROVAL)
                .statusHistory(statusHistory)
                .build();
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
