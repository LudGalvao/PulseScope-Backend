package com.pulsescope.backend.controller;

import com.pulsescope.backend.dto.MarketQuoteResponse;
import com.pulsescope.backend.service.MarketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/market")
@Tag(name = "Market", description = "Consulta de dados de mercado com Alpha Vantage")
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/quote")
    @Operation(
            summary = "Consulta cotacao por ticker",
            description = "Retorna dados de mercado para um simbolo, como preco atual, variacao e volume.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cotacao retornada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Ticker nao encontrado"),
                    @ApiResponse(responseCode = "503", description = "Alpha Vantage nao configurada")
            }
    )
    public MarketQuoteResponse getQuote(
            @Parameter(description = "Ticker da empresa", example = "AAPL")
            @RequestParam @NotBlank String symbol) {
        return marketService.getQuote(symbol);
    }
}
