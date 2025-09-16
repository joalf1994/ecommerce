package com.escuelajavag4.catalogservice.controller;

import com.escuelajavag4.catalogservice.model.dto.request.MarcaCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.MarcaResponseDto;
import com.escuelajavag4.catalogservice.service.MarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    @PostMapping
    public ResponseEntity<MarcaResponseDto> createMarca(
            @Valid @RequestBody MarcaCreateRequestDto createRequestDto) {
        MarcaResponseDto response = marcaService.createMarca(createRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MarcaResponseDto>> getAllMarcas() {
        return ResponseEntity.ok(marcaService.getAllMarcas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponseDto> getMarcaById(@PathVariable Long id) {
        return ResponseEntity.ok(marcaService.getMarcaById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<MarcaResponseDto> getMarcaByName(@RequestParam String name) {
        return ResponseEntity.ok(marcaService.getMarcaByName(name));
    }

    @GetMapping("/active")
    public ResponseEntity<List<MarcaResponseDto>> getActiveMarcas() {
        return ResponseEntity.ok(marcaService.getActiveMarcas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponseDto> updateMarca(
            @PathVariable Long id,
            @Valid @RequestBody MarcaUpdateRequestDto updateRequestDto) {
        return ResponseEntity.ok(marcaService.updateMarca(id, updateRequestDto));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<MarcaResponseDto> activateMarca(@PathVariable Long id) {
        return ResponseEntity.ok(marcaService.activateMarca(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateMarca(@PathVariable Long id) {
        marcaService.deactivateMarca(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarca(@PathVariable Long id) {
        marcaService.deleteMarca(id);
        return ResponseEntity.noContent().build();
    }
}
