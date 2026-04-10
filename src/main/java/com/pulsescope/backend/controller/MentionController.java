package com.pulsescope.backend.controller;

import com.pulsescope.backend.dto.MentionResponse;
import com.pulsescope.backend.service.MentionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mentions")
@Tag(name = "Mentions", description = "Consulta de mencoes coletadas")
public class MentionController {

    private final MentionService mentionService;

    public MentionController(MentionService mentionService) {
        this.mentionService = mentionService;
    }

    @GetMapping
    @Operation(
            summary = "Lista todas as mencoes",
            description = "Retorna as mencoes coletadas e persistidas no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mencoes retornadas com sucesso")
            }
    )
    public List<MentionResponse> findAll() {
        return mentionService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busca uma mencao pelo identificador",
            description = "Retorna uma mencao especifica junto com dados de analise quando houver.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mencao encontrada"),
                    @ApiResponse(responseCode = "404", description = "Mencao nao encontrada")
            }
    )
    public MentionResponse findById(@Parameter(description = "Identificador da mencao", example = "1") @PathVariable Long id) {
        return mentionService.findById(id);
    }
}
