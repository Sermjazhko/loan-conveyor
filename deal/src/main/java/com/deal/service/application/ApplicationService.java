package com.deal.service.application;

import com.deal.enums.ApplicationStatus;
import com.deal.enums.ChangeType;
import com.deal.model.Application;
import com.deal.model.Client;

import java.util.Date;
import java.util.List;

public interface ApplicationService {

    void addApplicationToDB(Application application, Date date, ApplicationStatus applicationStatus);

    Application getApplicationById(Long id);

    List<Application> getApplications();

    Application createApplication(Client client);

    String createStatusHistory(List list, ApplicationStatus applicationStatus, ChangeType changeType,
                               Date date);

    Application updateApplicationStatusHistory(Application application, Date date,
                                               ApplicationStatus applicationStatus);
}
