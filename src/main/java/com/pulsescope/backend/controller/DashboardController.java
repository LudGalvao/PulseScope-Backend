package com.pulsescope.backend.controller;

import com.pulsescope.backend.dto.DashboardSummaryResponse;
import com.pulsescope.backend.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Indicadores consolidados do sistema")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @Operation(
            summary = "Retorna um resumo consolidado do dashboard",
            description = "Entrega totais de mencoes, sentimentos e alertas abertos para alimentar o dashboard.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso")
            }
    )
    public DashboardSummaryResponse getSummary() {
        return dashboardService.getSummary();
    }
}
