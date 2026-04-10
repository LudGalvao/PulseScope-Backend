package com.pulsescope.backend.controller;

import com.pulsescope.backend.domain.SearchHistory;
import com.pulsescope.backend.dto.SearchRequest;
import com.pulsescope.backend.dto.SearchResponse;
import com.pulsescope.backend.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/searches")
@Tag(name = "Searches", description = "Buscas e historico de consultas")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Realiza uma busca por palavra-chave",
            description = "Consulta a API externa configurada, persiste mencoes encontradas e registra o historico da busca.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Busca processada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Payload invalido")
            }
    )
    public SearchResponse search(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Palavra-chave a ser pesquisada",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SearchRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "keyword": "nike"
                                    }
                                    """)
                    )
            )
            @RequestBody SearchRequest request) {
        return searchService.search(request);
    }

    @GetMapping
    @Operation(
            summary = "Lista o historico de buscas",
            description = "Retorna as buscas ja realizadas pela API principal.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Historico retornado com sucesso")
            }
    )
    public List<SearchHistory> findSearchHistory() {
        return searchService.findSearchHistory();
    }
}
