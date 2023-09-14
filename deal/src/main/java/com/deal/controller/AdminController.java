package com.deal.controller;


import com.deal.model.Application;
import com.deal.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Admin controller", description = "")
@RestController
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DealService dealService;

    @Operation(
            summary = "get applicationDTO by id",
            description = "getting information about the application by application id"
    )
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<Application> getApplicationById(@PathVariable(value = "applicationId") Long applicationId) {
        return dealService.getLoanApplicationByApplicationId(applicationId);
    }

    @Operation(
            summary = "get applications",
            description = "getting information about the application by application id"
    )
    @GetMapping("/application")
    public ResponseEntity<List<Application>> getApplication() {
        return dealService.getApplications();
    }
}
