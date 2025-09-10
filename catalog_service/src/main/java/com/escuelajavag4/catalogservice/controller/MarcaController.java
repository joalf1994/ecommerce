package com.escuelajavag4.catalogservice.controller;

import com.escuelajavag4.catalogservice.model.dto.request.MarcaCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.MarcaResponseDto;
import com.escuelajavag4.catalogservice.service.MarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    @PostMapping
    public MarcaResponseDto createMarca(@Valid @RequestBody MarcaCreateRequestDto createRequestDto) {
        return marcaService.createMarca(createRequestDto);
    }

    @GetMapping
    public List<MarcaResponseDto> getAllMarcas() {
        return marcaService.getAllMarcas();
    }

    @GetMapping("/{id}")
    public MarcaResponseDto getMarcaById(@PathVariable Long id) {
        return marcaService.getMarcaById(id);
    }

    @GetMapping("/search")
    public MarcaResponseDto getMarcaByName(@RequestParam String name) {
        return marcaService.getMarcaByName(name);
    }

    @GetMapping("/active")
    public List<MarcaResponseDto> getActiveMarcas() {
        return marcaService.getActiveMarcas();
    }

    @PutMapping("/{id}")
    public MarcaResponseDto updateMarca(
            @PathVariable Long id,
            @Valid @RequestBody MarcaUpdateRequestDto updateRequestDto) {
        return marcaService.updateMarca(id, updateRequestDto);
    }

    @PatchMapping("/{id}/activate")
    public MarcaResponseDto activateMarca(@PathVariable Long id) {
        return marcaService.activateMarca(id);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivateMarca(@PathVariable Long id) {
        marcaService.deactivateMarca(id);
    }

    @DeleteMapping("/{id}")
    public void deleteMarca(@PathVariable Long id) {
        marcaService.deleteMarca(id);
    }
}
