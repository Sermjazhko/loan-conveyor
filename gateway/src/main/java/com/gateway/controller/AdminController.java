package com.gateway.controller;

import com.gateway.integration.DealService;
import com.gateway.model.Application;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Admin controller", description = "getting information about the application")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DealService dealService;

    @Operation(
            summary = "get applicationDTO by id",
            description = "getting information about the application by application id"
    )
    @GetMapping("/application/{applicationId}")
    public Application getApplicationById(@PathVariable(value = "applicationId") Long applicationId) {
        return dealService.getApplicationById(applicationId);
    }

    @Operation(
            summary = "get applications",
            description = "getting information about the applications"
    )
    @GetMapping("/application")
    public List<Application> getApplication() {
        return dealService.getApplications();
    }
}
