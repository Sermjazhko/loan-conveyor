package com.deal.service.application;

import com.deal.enums.ApplicationStatus;
import com.deal.enums.ChangeType;
import com.deal.model.Application;

import java.util.Date;
import java.util.List;

public interface ApplicationService {

    void addApplicationToDB(Application application);

    Application getApplicationById(Long id);

    Application createApplication(Long id, String statusHistory);

    String createStatusHistory(List list, ApplicationStatus applicationStatus, ChangeType changeType,
                               Date date);

    Application updateApplicationStatusHistory(Application application, Date date,
                                               ApplicationStatus applicationStatus);
}
