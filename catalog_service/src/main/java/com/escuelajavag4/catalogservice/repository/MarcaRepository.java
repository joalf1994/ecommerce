package com.escuelajavag4.catalogservice.repository;

import com.escuelajavag4.catalogservice.model.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca,Long> {
    Optional<Marca> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Marca> findByActiveTrue();
}
