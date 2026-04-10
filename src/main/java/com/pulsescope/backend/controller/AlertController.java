package com.pulsescope.backend.controller;

import com.pulsescope.backend.dto.AlertResponse;
import com.pulsescope.backend.dto.UpdateAlertStatusRequest;
import com.pulsescope.backend.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "Alerts", description = "Gerenciamento de alertas")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    @Operation(
            summary = "Lista todos os alertas",
            description = "Retorna os alertas gerados pelo sistema com severidade e status atual.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Alertas retornados com sucesso")
            }
    )
    public List<AlertResponse> findAll() {
        return alertService.findAll();
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Atualiza o status de um alerta",
            description = "Permite alterar o status de um alerta para OPEN, ACKNOWLEDGED ou RESOLVED.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Alerta nao encontrado")
            }
    )
    public AlertResponse updateStatus(
            @Parameter(description = "Identificador do alerta", example = "1")
            @PathVariable Long id,
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novo status do alerta",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateAlertStatusRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "status": "ACKNOWLEDGED"
                                    }
                                    """)
                    )
            )
            @RequestBody UpdateAlertStatusRequest request) {
        return alertService.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Remove um alerta",
            description = "Exclui definitivamente um alerta pelo identificador.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Alerta removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Alerta nao encontrado")
            }
    )
    public void delete(
            @Parameter(description = "Identificador do alerta", example = "1")
            @PathVariable Long id) {
        alertService.delete(id);
    }
}
