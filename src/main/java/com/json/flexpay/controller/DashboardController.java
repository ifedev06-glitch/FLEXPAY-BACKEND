package com.json.flexpay.controller;

import com.json.flexpay.dto.DashboardResponse;
import com.json.flexpay.entity.User;
import com.json.flexpay.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard(@AuthenticationPrincipal User user) {
        return dashboardService.getDashboardInfo(user);
    }
}
